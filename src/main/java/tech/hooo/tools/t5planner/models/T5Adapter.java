package tech.hooo.tools.t5planner.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * T5连接器
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class T5Adapter implements IT5Module {
    /**
     * ID
     */
    private Integer id;
    /**
     * 连接器名称
     */
    private String name;
    /**
     * 单价
     */
    private Double price;
    /**
     * 品牌
     */
    private Integer brandId;
}
