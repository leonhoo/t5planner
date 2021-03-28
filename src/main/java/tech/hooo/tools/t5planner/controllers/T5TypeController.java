package tech.hooo.tools.t5planner.controllers;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tech.hooo.tools.t5planner.models.*;
import tech.hooo.tools.t5planner.services.IT5TypeService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class T5TypeController {

    @Autowired
    private IT5TypeService service;

    /**
     * 获取品牌列表
     *
     * @param param
     * @return
     */
    @ApiOperation("获取品牌列表")
    @PostMapping("/brandList")
    public List<BrandModel> brandList(@RequestBody PageRequest param) {
        if (param == null) {
            param = new PageRequest();
        }
        return service.brandList(param);
    }

    /**
     * 获取灯管品类列表
     *
     * @param param
     * @return
     */
    @ApiOperation("获取灯管品类列表")
    @PostMapping("/lightTubeList")
    public Map<Integer, T5LightTube> lightTubeList(@RequestBody T5TypeRequest param) {
        if (param == null) {
            param = new T5TypeRequest();
        }
        List<T5LightTube> data = service.lightTubeList(param);

        return data.stream().collect(Collectors.toMap(T5LightTube::getId, o -> o));
    }

    /**
     * 获取顶管连接器信息
     *
     * @param param
     * @return
     */
    @ApiOperation("获取顶管连接器信息")
    @PostMapping("/t5Adapter")
    public T5Adapter t5Adapter(@RequestBody T5TypeRequest param) {
        if (param == null) {
            param = new T5TypeRequest();
        }
        return service.t5Adapter(param);
    }
}
