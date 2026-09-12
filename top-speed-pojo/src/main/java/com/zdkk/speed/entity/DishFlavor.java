package com.zdkk.speed.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "菜品口味")
public class DishFlavor implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "口味id")
    private Long id;

    @Schema(description = "菜品id")
    private Long dishId;

    @Schema(description = "口味名称")
    private String name;

    @Schema(description = "口味数据 数组字符串，按逗号分割")
    private String value;
}
