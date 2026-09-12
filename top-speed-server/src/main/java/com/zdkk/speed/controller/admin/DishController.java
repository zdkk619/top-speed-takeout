package com.zdkk.speed.controller.admin;

import com.zdkk.speed.dto.DishDTO;
import com.zdkk.speed.dto.DishPageQueryDTO;
import com.zdkk.speed.entity.Dish;
import com.zdkk.speed.result.PageResult;
import com.zdkk.speed.result.Result;
import com.zdkk.speed.service.DishService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/dish")
@Tag(name = "菜品管理", description = "菜品管理相关接口")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;
    @PostMapping
    @Operation(summary = "新增菜品", description = "新增菜品")
    public Result<String> save(@RequestBody DishDTO dishDTO) {
        log.info("【新增菜品】 dishDTO:{}", dishDTO);
        dishService.save(dishDTO);
        return Result.success();
    }


    @GetMapping("/page")
    @Operation(summary = "菜品分页查询", description = "菜品分页查询")
    public Result<PageResult> pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        // TODO 前端这里传的页码和页大小需要调整，如果带有额外查询条件，需将页码重置为1
        log.info("【菜品分页查询】 dishPageQueryDTO:{}", dishPageQueryDTO);
        PageResult pageResult = dishService.pageQuery(dishPageQueryDTO);
        return Result.success(pageResult);
    }

    @DeleteMapping
    @Operation(summary = "删除菜品", description = "删除菜品，可以删除一个或多个，用逗号分隔")
    public Result<String> delete(@RequestParam List<Long> ids) {
        log.info("【删除菜品】 ids:{}", ids);
        dishService.delete(ids);
        return Result.success();
    }
}
