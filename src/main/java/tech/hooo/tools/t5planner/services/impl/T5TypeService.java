package tech.hooo.tools.t5planner.services.impl;

import org.springframework.stereotype.Service;
import tech.hooo.tools.t5planner.models.*;
import tech.hooo.tools.t5planner.services.IT5TypeService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class T5TypeService implements IT5TypeService {
    //模拟数据库读取的数据结果, 数据使用爬虫获得
    private List<IT5Module> t5Modules = new ArrayList<>(
            Arrays.asList(
                    new T5LightTube(1, "0.3m", 0.30, 16.42, 1),
                    new T5LightTube(2, "0.6m", 0.60, 21.28, 1),
                    new T5LightTube(3, "0.9m", 0.90, 29.85, 1),
                    new T5LightTube(4, "1.0m", 1.00, 31.28, 1),
                    new T5LightTube(5, "1.2m", 1.20, 34.14, 1),
                    new T5Adapter(6, "普通连接器", 4.2, 1),
                    new T5LightTube(7, "0.3m", 0.30, 11.5, 2),
                    new T5LightTube(8, "0.6m", 0.60, 12.5, 2),
                    new T5LightTube(10, "1.0m", 1.00, 14.0, 2),
                    new T5LightTube(11, "1.2m", 1.20, 14.0, 2),
                    new T5Adapter(12, "普通连接器", 4.9, 2)
            )
    );
    private List<BrandModel> brands = new ArrayList<>(
            Arrays.asList(
                    new BrandModel(1, "NVC"),
                    new BrandModel(2, "OPPLE")
            )
    );


    @Override
    public List<T5LightTube> lightTubeList(T5TypeRequest param) {
        return t5Modules.stream()
                .filter(o -> o instanceof T5LightTube && (param.getBrandId() == null || o.getBrandId() == param.getBrandId()))
                .map(o -> (T5LightTube) o)
                .collect(Collectors.toList());
    }

    @Override
    public T5Adapter t5Adapter(T5TypeRequest param) {
        return t5Modules.stream()
                .filter(o -> o instanceof T5Adapter && (param.getBrandId() == null || o.getBrandId() == param.getBrandId()))
                .map(o -> (T5Adapter) o)
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<BrandModel> brandList(PageRequest param) {
        return brands;
    }
}
