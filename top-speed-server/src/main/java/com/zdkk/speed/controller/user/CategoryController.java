package com.zdkk.speed.controller.user;

import com.zdkk.speed.entity.Category;
import com.zdkk.speed.result.Result;
import com.zdkk.speed.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("userCategoryController")
@Slf4j
@RequestMapping("/user/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @RequestMapping("/list")
    public Result<List<Category>> list(Integer type) {
        log.info("【分类列表查询】 type:{}", type);
        List<Category> list = categoryService.getByType(type);
        return Result.success(list);
    }
}
