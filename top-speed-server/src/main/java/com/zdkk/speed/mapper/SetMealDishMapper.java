package com.zdkk.speed.mapper;

import com.zdkk.speed.entity.SetMeal;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SetMealDishMapper {

    /**
     * 根据一组菜品id查询一组套餐
     * @param ids
     * @return
     */
    // select distinct setmeal_id from setmeal_dish where dish_id in (ids)
    List<Long> getSetMealsByDishIds(List<Long> ids);
}
