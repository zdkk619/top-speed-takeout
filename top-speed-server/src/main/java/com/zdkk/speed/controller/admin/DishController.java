package com.zdkk.speed.controller.admin;

import com.zdkk.speed.dto.DishDTO;
import com.zdkk.speed.dto.DishPageQueryDTO;
import com.zdkk.speed.entity.Dish;
import com.zdkk.speed.result.PageResult;
import com.zdkk.speed.result.Result;
import com.zdkk.speed.service.DishService;
import com.zdkk.speed.vo.DishVO;
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
    @Operation(summary = "删除菜品", description = "删除菜品，同时删除菜品对应的口味数据，可以删除一个或多个，用逗号分隔")
    public Result<String> delete(@RequestParam List<Long> ids) {
        log.info("【删除菜品】 ids:{}", ids);
        dishService.delete(ids);
        return Result.success();
    }

    @PostMapping("/status/{status}")
    @Operation(summary = "修改菜品状态", description = "修改菜品状态")
    public Result<String> enableOrDisable(@RequestParam Long id, @PathVariable Integer status) {
        log.info("【修改菜品状态】 id:{}, status:{}", id, status);
        dishService.enableOrDisable(id, status);
        return Result.success();
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据id查询菜品", description = "根据id查询菜品")
    public Result<DishVO> getById(@PathVariable Long id) {
        log.info("【根据id查询菜品】 id:{}", id);
        DishVO dishVO = dishService.getById(id);
        return Result.success(dishVO);
    }

    @PutMapping
    @Operation(summary = "修改菜品", description = "修改菜品")
    public Result<String> update(@RequestBody DishDTO dishDTO) {
        log.info("【修改菜品】 dishDTO:{}", dishDTO);
        dishService.update(dishDTO);
        return Result.success();
    }

    @GetMapping("/list")
    @Operation(summary = "根据分类id查询菜品列表", description = "根据分类id查询菜品列表")
    public Result<List<Dish>> list(@RequestParam Long categoryId) {
        log.info("【根据分类id查询菜品列表】 categoryId:{}", categoryId);
        List<Dish> dishList = dishService.list(categoryId);
        return Result.success(dishList);
    }
}
