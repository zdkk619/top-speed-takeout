package com.zdkk.speed.controller.admin;

import com.zdkk.speed.dto.CategoryDTO;
import com.zdkk.speed.dto.CategoryPageQueryDTO;
import com.zdkk.speed.entity.Category;
import com.zdkk.speed.result.PageResult;
import com.zdkk.speed.result.Result;
import com.zdkk.speed.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/admin/category")
@RestController
@Slf4j
@Tag(name = "分类管理", description = "分类管理相关接口")
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

    @PostMapping("/status/{status}")
    @Operation(summary = "启用/禁用分类", description = "启用/禁用分类")
    public Result<String> enableOrDisable(@PathVariable Integer status, Long id) {
        log.info("【分类启用禁用】id = {}, status = {}", id, status);
        categoryService.enableOrDisable(status, id);
        return Result.success();
    }

    @PutMapping
    @Operation(summary = "更新分类", description = "更新分类")
    public Result<String> update(@RequestBody CategoryDTO categoryDTO) {
        log.info("【更新分类】 categoryDTO:{}", categoryDTO);
        categoryService.update(categoryDTO);
        return Result.success();
    }

    @GetMapping("/list")
    @Operation(summary = "按分类类型查询", description = "分类列表查询")
    public Result<List<Category>> getByType(@RequestParam Integer type) {
        log.info("【分类列表查询】 type:{}", type);
        List<Category> categoryList = categoryService.getByType(type);
        return Result.success(categoryList);
    }

    @DeleteMapping
    @Operation(summary = "删除分类", description = "删除分类")
    public Result<String> delete(@RequestParam Long id) {
        log.info("【删除分类】 id:{}", id);
        categoryService.deleteById(id);
        return Result.success();
    }
}
