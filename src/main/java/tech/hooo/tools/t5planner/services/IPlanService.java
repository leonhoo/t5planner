package tech.hooo.tools.t5planner.services;

import tech.hooo.tools.t5planner.models.BizException;
import tech.hooo.tools.t5planner.models.PlanModel;
import tech.hooo.tools.t5planner.models.PlanRequest;

import java.util.List;

public interface IPlanService {
    List<PlanModel> calculate(PlanRequest param) throws BizException;
}
