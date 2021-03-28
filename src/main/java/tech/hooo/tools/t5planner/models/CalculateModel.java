package tech.hooo.tools.t5planner.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
public class CalculateModel {
    /**
     * ID对应的个数
     */
    private Map<Integer, Integer> map;
    /**
     * 剩余的数量, 单位毫米
     */
    private int remaining;

    public CalculateModel() {
        map = new HashMap<>();
    }
}
