package com.zdkk.speed.vo;

import com.zdkk.speed.entity.DishFlavor;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "菜品VO")
public class DishVO implements Serializable {

    @Schema(description = "菜品ID")
    private Long id;
    //菜品名称
    @Schema(description = "菜品名称")
    private String name;

    @Schema(description = "菜品分类ID")
    //菜品分类id
    private Long categoryId;

    @Schema(description = "菜品价格")
    //菜品价格
    private BigDecimal price;

    //图片
    @Schema(description = "图片")
    private String image;

    //描述信息
    @Schema(description = "描述信息")
    private String description;

    //0 停售 1 起售
    @Schema(description = "状态")
    private Integer status;

    //更新时间
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    //分类名称
    @Schema(description = "分类名称")
    private String categoryName;

    //菜品关联的口味
    @Schema(description = "菜品关联的口味")
    private List<DishFlavor> flavors = new ArrayList<>();

    //private Integer copies;
}
