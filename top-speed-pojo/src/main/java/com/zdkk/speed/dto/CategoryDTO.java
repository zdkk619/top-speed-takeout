package com.zdkk.speed.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(description = "添加菜品/套餐分类时传输的数据模型")
public class CategoryDTO implements Serializable {

    @Schema(description = "id")
    private Long id;

    @Schema(description = "类型 1 菜品分类 2 套餐分类")
    @NotNull
    private Integer type;

    @Schema(description = "分类名称")
    @NotNull
    private String name;

    @Schema(description = "排序")
    @NotNull
    private Integer sort;

}
