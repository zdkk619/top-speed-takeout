package com.zdkk.speed.service;

import com.zdkk.speed.dto.DishDTO;

public interface DishService {
    /**
     * 新增菜品
     * @param dishDTO
     */
    void save(DishDTO dishDTO);
}
