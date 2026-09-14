package com.zdkk.speed.service;

import com.zdkk.speed.dto.DishDTO;
import com.zdkk.speed.dto.DishPageQueryDTO;
import com.zdkk.speed.entity.Dish;
import com.zdkk.speed.result.PageResult;
import com.zdkk.speed.vo.DishVO;

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

    /**
     * 修改菜品状态
     * @param id
     * @param status
     */
    void enableOrDisable(Long id, Integer status);

    /**
     * 根据id查询菜品
     * @param id
     * @return
     */
    DishVO getById(Long id);

    /**
     * 修改菜品(可能有口味配置)
     * @param dishDTO
     */
    void update(DishDTO dishDTO);

    /**
     * 根据分类id查询菜品列表
     * @param categoryId
     * @return
     */
    List<Dish> list(Long categoryId);

    /**
     * 根据条件查询菜品列表和口味
     * @param dish
     * @return
     */
    List<DishVO> listWithFlavor(Dish dish);
}
