package tech.hooo.tools.t5planner.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class T5LightTube implements IT5Module {
    /**
     * 灯管ID
     */
    private Integer id;
    /**
     * 灯管名称
     */
    private String name;
    /**
     * 灯管长度,单位米
     */
    private Double length;
    /**
     * 灯管价格
     */
    private Double price;
    /**
     * 品牌
     */
    private Integer brandId;
}
