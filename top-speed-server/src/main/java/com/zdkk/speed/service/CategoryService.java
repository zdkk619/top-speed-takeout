package com.zdkk.speed.service;

import com.zdkk.speed.dto.CategoryPageQueryDTO;
import com.zdkk.speed.result.PageResult;

public interface CategoryService {
    /**
     * 分页查询分类
     * @param categoryPageQueryDTO
     * @return
     */
    PageResult pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);
}
