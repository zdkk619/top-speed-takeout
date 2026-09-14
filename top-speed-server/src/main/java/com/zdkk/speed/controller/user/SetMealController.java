package com.zdkk.speed.controller.user;

import com.zdkk.speed.constant.StatusConstant;
import com.zdkk.speed.entity.SetMeal;
import com.zdkk.speed.result.Result;
import com.zdkk.speed.service.SetMealService;
import com.zdkk.speed.vo.DishItemVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController("userSetMealController")
@RequestMapping("/user/setmeal")
@Slf4j
@Tag(name = "用户套餐管理", description = "用户套餐管理")
public class SetMealController {
    @Autowired
    private SetMealService setMealService;

    @GetMapping("/list")
    @Operation(summary = "查询套餐列表", description = "查询套餐列表")
    public Result<List<SetMeal>> list(Long categoryId) {
        log.info("【用户套餐管理】查询套餐列表 categoryId:{}", categoryId);
        SetMeal setMeal = SetMeal.builder()
                .categoryId(categoryId)
                .status(StatusConstant.ENABLE)
                .build();
        return Result.success(setMealService.list(setMeal));
    }

    @GetMapping("/dish/{id}")
    @Operation(summary = "查询套餐菜品列表", description = "查询套餐菜品列表")
    public Result<List<DishItemVO>> dishList(@PathVariable("id") Long id) {
        log.info("【用户套餐管理】查询套餐菜品列表 id:{}", id);
        return Result.success(setMealService.getDishItemsBySetMealId(id));
    }
}
