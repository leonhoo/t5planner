package tech.hooo.tools.t5planner.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 请求类
 */
@Data
public class PlanRequest {
    /**
     * 品牌
     */
    @ApiModelProperty("品牌")
    @JsonProperty("brandId")
    private Integer brandId;
    /**
     * 阈值(吊顶深度), 允许总长与所有管子长度总和的误差范围, 允许长一点
     * 单位 米
     */
    @ApiModelProperty("阈值(吊顶深度), 允许总长与所有管子长度总和的误差范围, 允许长一点或者短一点, 单位: 米")
    @JsonProperty("threshold")
    private double threshold;
    /**
     * 各个直线总长度
     */
    @ApiModelProperty("各个直线总长度")
    @JsonProperty("items")
    private List<Double> items;
}
