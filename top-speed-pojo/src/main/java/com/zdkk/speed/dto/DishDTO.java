package com.zdkk.speed.dto;

import com.zdkk.speed.constant.CategoryTypeConstant;
import com.zdkk.speed.entity.DishFlavor;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Data
@Schema(description = "菜品DTO")
public class DishDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public static final int TYPE = CategoryTypeConstant.DISH_TYPE;

    @Schema(description = "菜品id")
    private Long id;

    @NotBlank
    @Schema(description = "菜品名称")
    private String name;

    @NotNull
    @Schema(description = "菜品分类id")
    private Long categoryId;

    @Schema(description = "菜品价格")
    private BigDecimal price;
    @Schema(description = "图片")
    private String image;
    @Schema(description = "描述信息")
    private String description;
    @Schema(description = "0 停售 1 起售")
    private Integer status;
    @Schema(description = "口味")
    private List<DishFlavor> flavors = new ArrayList<>();
}
