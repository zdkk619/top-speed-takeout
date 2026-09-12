package com.zdkk.speed.service;

import com.zdkk.speed.dto.SetMealDTO;
import com.zdkk.speed.dto.SetMealPageQueryDTO;
import com.zdkk.speed.entity.SetMeal;
import com.zdkk.speed.result.PageResult;
import com.zdkk.speed.vo.SetMealVO;

import java.util.List;

public interface SetMealService {
    /**
     * 套餐分页查询
     * @param setMealPageQueryDTO
     * @return
     */
    PageResult pageQuery(SetMealPageQueryDTO setMealPageQueryDTO);

    /**
     * 新增套餐
     * @param setMealDTO
     */
    void save(SetMealDTO setMealDTO);

    /**
     * 根据id查询套餐
     * @param id
     * @return
     */
    SetMealVO getById(Long id);

    /**
     * 修改套餐
     * @param setMealDTO
     */
    void update(SetMealDTO setMealDTO);

    /**
     * 修改套餐状态
     * @param status
     * @param id
     */
    void enableOrDisable(Integer status, Long id);

    /**
     * 删除套餐
     * @param ids
     */
    void delete(List<Long> ids);
}
