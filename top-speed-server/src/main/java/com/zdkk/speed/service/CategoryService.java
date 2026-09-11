package com.zdkk.speed.service;

import com.zdkk.speed.dto.CategoryDTO;
import com.zdkk.speed.dto.CategoryPageQueryDTO;
import com.zdkk.speed.entity.Category;
import com.zdkk.speed.result.PageResult;

import java.util.List;

public interface CategoryService {
    /**
     * 分页查询分类
     * @param categoryPageQueryDTO
     * @return
     */
    PageResult pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);

    /**
     * 保存分类
     * @param categoryDTO
     */
    void save(CategoryDTO categoryDTO);

    /**
     * 启用禁用分类
     * @param status
     * @param id
     */
    void enableOrDisable(Integer status, Long id);

    /**
     * 更新分类
     * @param categoryDTO
     */
    void update(CategoryDTO categoryDTO);

    /**
     * 根据类型查询分类
     * @param type
     * @return
     */
    List<Category> getByType(Integer type);

    /**
     * 根据id删除分类
     * @param id
     */
    void deleteById(Long id);
}
