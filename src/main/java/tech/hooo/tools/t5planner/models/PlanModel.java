package tech.hooo.tools.t5planner.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanModel {
    /**
     * 总长(与既定目标±10cm)
     */
    private double totalLength;
    /**
     * 组合(id,数量)
     */
    private Map<Integer, Integer> combination;
    /**
     * 用到的连接器的数量
     */
    private int adapterCount;

}
