package tech.hooo.tools.t5planner.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 品牌
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BrandModel {
    /**
     * ID
     */
    @JsonProperty("id")
    @ApiModelProperty("品牌ID")
    private Integer id;
    /**
     * 品牌名称
     */
    @JsonProperty("name")
    @ApiModelProperty("品牌名称")
    private String name;
}
