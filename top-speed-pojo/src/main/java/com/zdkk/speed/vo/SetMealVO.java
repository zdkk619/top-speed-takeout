package com.zdkk.speed.vo;

import com.zdkk.speed.entity.SetmealDish;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "套餐VO")
public class SetMealVO {
    @Schema(description = "套餐ID")
    private Long id;

    @Schema(description = "套餐名称")
    private String name;

    @Schema(description = "套餐分类ID")
    private Long categoryId;

    @Schema(description = "套餐价格")
    private BigDecimal price;

    @Schema(description = "套餐图片")
    private String image;

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

    //套餐和菜品的关联关系
    private List<SetmealDish> setmealDishes = new ArrayList<>();
}
