package tech.hooo.tools.t5planner.services.impl;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tech.hooo.tools.t5planner.models.*;
import tech.hooo.tools.t5planner.services.IPlanService;
import tech.hooo.tools.t5planner.services.IT5TypeService;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PlanService implements IPlanService {

    @Autowired
    private IT5TypeService typeService;

    @Override
    public List<PlanModel> calculate(PlanRequest param) throws BizException {
        //过滤掉连接器
        List<T5LightTube> t5LightTubes = typeService.lightTubeList(
                T5TypeRequest.builder()
                        .brandId(param.getBrandId())
                        .build()
        );
        if (t5LightTubes == null || t5LightTubes.size() < 1) {
            throw new BizException("未发现该品牌的T5灯管");
        }

        //吊顶深度
        int threshold = m2mm(param.getThreshold());

        //排序
        t5LightTubes.sort((o1, o2) -> (int) (o1.getLength() - o2.getLength()));

        //获取最小长度, 用于计算最后是否还有可以匹配(因为已经排序, 所以只要获取第一个)
        int minLightTubeLength = m2mm(t5LightTubes.get(0).getLength());

        List<PlanModel> result = new ArrayList<>();

        for (Double item : param.getItems()) {
            if (item != null) {
                int sum = m2mm(item);
                List<CalculateModel> combinationPossibilities = calculate(sum,
                        new HashMap<>(),
                        new ArrayList<>(),
                        t5LightTubes,
                        t5LightTubes.size() - 1,
                        minLightTubeLength);
                //去除全部是0的记录
                combinationPossibilities = combinationPossibilities.stream().filter(
                        o -> o.getMap().values().stream().mapToInt(oo -> oo).sum() > 0
                ).collect(Collectors.toList());

                if (combinationPossibilities.size() < 1) {
                    //封装成返回类型
                    result.add(PlanModel.builder()
                            .adapterCount(0) //每一条直线上的灯管配一个连接器
                            .totalLength(item)
                            .combination(null)
                            .build());
                } else {
                    combinationPossibilities.sort(Comparator.comparingInt(CalculateModel::getRemaining));

                    //取在阈值范围内的
                    List<Integer> minRemainings = combinationPossibilities.stream()
                            .map(CalculateModel::getRemaining)
                            .filter(o -> o >= -threshold && o <= 0)//最好的灯管长度为"总长度"至"总长度+吊顶深度"之间
                            .distinct()
                            .collect(Collectors.toList());
                    //在阈值范围之内的记录
                    List<CalculateModel> bestList = combinationPossibilities.stream()
                            .filter(o -> minRemainings.contains(o.getRemaining())).collect(Collectors.toList());

                    //如果有多个, 比较价格性价比
                    if (bestList.size() > 1) {
                        bestList = getBestByRate(bestList, t5LightTubes);
                    }

                    //如果还有多个best,总体数量越少越好
                    Map<Integer, Integer> best = null;
                    if (bestList.size() > 1) {
                        int minCount = Integer.MAX_VALUE; //数量
                        for (val map : bestList) {
                            int typeCount = map.getMap().values().stream().mapToInt(o -> o).sum();
                            if (typeCount < minCount) {
                                best = map.getMap();
                            }
                        }
                    } else {
                        best = bestList.get(0).getMap();
                    }
                    assert best != null;
                    //为了前端方便, 没有的型号,数量设置为0
                    List<Integer> ids = t5LightTubes.stream().map(T5LightTube::getId).collect(Collectors.toList());
                    for (int id : ids) {
                        if (!best.containsKey(id)) {
                            best.put(id, 0);
                        }
                    }
                    //封装成返回类型
                    result.add(PlanModel.builder()
                            .adapterCount(2) //每条直线两头配2个连接器
//                            .adapterCount(best.values().stream().mapToInt(o -> o).sum()) //每根灯管配一个连接器
                            .totalLength(item)
                            .combination(best)
                            .build());
                }
            }
        }

        return result;
    }

    /**
     * @param remaining                剩余总长
     * @param level                    t5types的第几个
     * @param countMap                 key: t5typeId, value: count
     * @param t5LightTubes             所有的灯管类型
     * @param combinationPossibilities 可能的组合
     * @return
     */
    private List<CalculateModel> calculate(int remaining,
                                           Map<Integer, Integer> countMap,
                                           List<CalculateModel> combinationPossibilities,
                                           final List<T5LightTube> t5LightTubes,
                                           final int level,
                                           final int minLightTubeLength) {
        log.info("这是第" + level + "层");
        if (level >= 0) {
            T5LightTube t5LightTube = t5LightTubes.get(level);
            int lightTubeId = t5LightTube.getId();
            int lightTubeLength = m2mm(t5LightTube.getLength()); //灯管长度, 米转换为毫米
            int max = (int) Math.ceil((double) remaining / lightTubeLength);

            for (int i = max; i >= 0; i--) {
                int count = i;
                int remainingNext = remaining - lightTubeLength * count;
                countMap.put(lightTubeId, count); //数量为i+1
                Map<Integer, Integer> cloneMap = clone(countMap);
                if (remainingNext <= minLightTubeLength) {
                    StringBuilder sb = new StringBuilder();
                    for (val item : cloneMap.entrySet()) {
                        sb.append(item.getKey()).append("->").append(item.getValue()).append(", ");
                    }
                    sb.append("剩余:").append(remainingNext);

                    log.info(sb.toString());
                    combinationPossibilities.add(new CalculateModel(cloneMap, remainingNext));
                } else {
                    combinationPossibilities = calculate(remainingNext, countMap, combinationPossibilities, t5LightTubes, level - 1, minLightTubeLength);
                }
            }
        }
        return combinationPossibilities;
    }

    /**
     * 根据价格获取最优性价比的组合
     *
     * @param combinationPossibilities 所有可能的组合
     * @param t5LightTubes             该品牌T5灯管的品类
     * @return
     */
    private List<CalculateModel> getBestByRate(List<CalculateModel> combinationPossibilities, List<T5LightTube> t5LightTubes) {
        List<CalculateModel> bestList = new ArrayList<>();
        //如果有多个, 比较价格性价比
        if (combinationPossibilities.size() > 0) {
            double minPriceRate = Double.MAX_VALUE; //单价
            for (val map : combinationPossibilities) {
                double priceRate = getMapPriceRate(map.getMap(), t5LightTubes);

//                StringBuilder sb = new StringBuilder();
//                for (val item : map.getMap().entrySet()) {
//                    sb.append(item.getKey()).append("->").append(item.getValue()).append(", ");
//                }
//                sb.append("性价比:").append(priceRate);
//                log.info(sb.toString());

                if (priceRate < minPriceRate) {
                    bestList.clear();
                    minPriceRate = priceRate;
                    bestList.add(map);
                } else if (priceRate == minPriceRate) {
                    bestList.add(map);
                }
            }
        }
        return bestList;
    }

    /**
     * @param map          (id,个数)
     * @param t5LightTubes 所有T5灯管品类
     * @return 获取平均单价, 越低越好
     */
    private double getMapPriceRate(Map<Integer, Integer> map, List<T5LightTube> t5LightTubes) {
        double price = 0;
        double length = 0;
        for (val item : map.entrySet()) {
            T5LightTube t5LightTube = t5LightTubes.stream().filter(o -> o.getId() == item.getKey()).findFirst().orElse(null);
            if (t5LightTube != null) {
                price += (t5LightTube.getPrice() * item.getValue());
                length += (t5LightTube.getLength() * item.getValue());
            }
        }
        return price / length;
    }

    /**
     * 米 转成 毫米
     *
     * @param v
     * @return
     */
    private int m2mm(double v) {
        return (int) Math.round(v * 100);
    }

    private <T, K> Map<T, K> clone(Map<T, K> map) {
        Map<T, K> result = null;
        if (map != null) {
            result = new HashMap<>();
            for (Map.Entry<T, K> entry : map.entrySet()) {
                result.put(entry.getKey(), entry.getValue());
            }
        }
        return result;
    }
}




