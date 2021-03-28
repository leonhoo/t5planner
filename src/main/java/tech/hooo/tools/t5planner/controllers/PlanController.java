package tech.hooo.tools.t5planner.controllers;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tech.hooo.tools.t5planner.models.BizException;
import tech.hooo.tools.t5planner.models.PlanModel;
import tech.hooo.tools.t5planner.models.PlanRequest;
import tech.hooo.tools.t5planner.services.IPlanService;

import java.util.List;

@RestController
public class PlanController {

    @Autowired
    private IPlanService service;

    /**
     * 开始计算
     *
     * @param param
     * @return
     */
    @ApiOperation("计算")
    @PostMapping("/calculate")
    public List<PlanModel> calculate(@RequestBody PlanRequest param) throws BizException {
        if (param.getBrandId() == null || param.getItems() == null || param.getItems().size() < 1) {
            throw new BizException("参数错误");
        }
        return service.calculate(param);
    }
}
