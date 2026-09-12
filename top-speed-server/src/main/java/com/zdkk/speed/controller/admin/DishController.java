package com.zdkk.speed.controller.admin;

import com.zdkk.speed.dto.DishDTO;
import com.zdkk.speed.result.Result;
import com.zdkk.speed.service.DishService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
