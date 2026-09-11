package com.zdkk.speed.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zdkk.speed.dto.CategoryPageQueryDTO;
import com.zdkk.speed.entity.Category;
import com.zdkk.speed.mapper.CategoryMapper;
import com.zdkk.speed.result.PageResult;
import com.zdkk.speed.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public PageResult pageQuery(CategoryPageQueryDTO categoryPageQueryDTO) {
        PageHelper.startPage(categoryPageQueryDTO.getPage(), categoryPageQueryDTO.getPageSize());
        Page<Category> page = categoryMapper.pageQuery(categoryPageQueryDTO);
        log.info("page: {}", page);
        long total = page.getTotal();
        return new PageResult(total, page.getResult());
    }
}
