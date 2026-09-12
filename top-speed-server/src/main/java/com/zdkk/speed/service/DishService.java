package com.zdkk.speed.service;

import com.zdkk.speed.dto.DishDTO;
import com.zdkk.speed.dto.DishPageQueryDTO;
import com.zdkk.speed.result.PageResult;

import java.util.List;

public interface DishService {
    /**
     * 新增菜品
     * @param dishDTO
     */
    void save(DishDTO dishDTO);

    /**
     * 菜品分页查询
     * @param dishPageQueryDTO
     * @return
     */
    PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 删除菜品
     * @param ids
     */
    void delete(List<Long> ids);
}
