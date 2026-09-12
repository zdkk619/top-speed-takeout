package com.zdkk.speed.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "菜品分页查询参数")
public class DishPageQueryDTO {
    @Schema(description = "当前页码")
    private int page;

    @Schema(description = "每页大小")
    private int pageSize;

    @Schema(description = "菜品名称")
    private String name;

    //分类id
    @Schema(description = "分类id")
    private Integer categoryId;

    //状态 0表示禁用 1表示启用
    @Schema(description = "状态 0表示禁用 1表示启用")
    private Integer status;
}
