package com.zdkk.speed.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "分类分页查询参数")
public class CategoryPageQueryDTO {
    @Schema(description = "分类名称")
    private String name;
    @Schema(description = "当前页码")
    @NotNull
    private Integer page;
    @Schema(description = "每页大小")
    @NotNull
    private Integer pageSize;
    @Schema(description = "分类类型")
    private Integer type;
}
