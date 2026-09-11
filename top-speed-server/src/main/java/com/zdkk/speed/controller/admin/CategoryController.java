package com.zdkk.speed.controller.admin;

import com.zdkk.speed.dto.CategoryDTO;
import com.zdkk.speed.dto.CategoryPageQueryDTO;
import com.zdkk.speed.result.PageResult;
import com.zdkk.speed.result.Result;
import com.zdkk.speed.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/admin/category")
@RestController
@Slf4j
@Tag(name = "CategoryController", description = "分类管理")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @Operation(summary = "分类分页查询", description = "分页查询分类")
    @GetMapping("/page")
    public Result<PageResult> page(CategoryPageQueryDTO categoryPageQueryDTO) {
        log.info("【分类分页查询】 page:{}, pageSize:{}", categoryPageQueryDTO.getPage(), categoryPageQueryDTO.getPageSize());
        PageResult pageResult = categoryService.pageQuery(categoryPageQueryDTO);
        return Result.success(pageResult);
    }

    @PostMapping
    @Operation(summary = "保存分类", description = "保存分类")
    public Result<String> save(@RequestBody CategoryDTO categoryDTO) {
        log.info("【保存分类】 categoryDTO:{}", categoryDTO);
        categoryService.save(categoryDTO);
        return Result.success();
    }
}
