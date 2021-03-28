package tech.hooo.tools.t5planner.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 请求类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class T5TypeRequest {
    /**
     * 品牌ID
     */
    @JsonProperty("brandId")
    @ApiModelProperty("品牌ID")
    private Integer brandId;
}
