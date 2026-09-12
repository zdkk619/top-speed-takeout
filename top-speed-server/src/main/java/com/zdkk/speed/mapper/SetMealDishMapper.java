package com.zdkk.speed.mapper;

import com.zdkk.speed.entity.SetmealDish;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

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

    /**
     * 批量插入
     * @param setmealDishes
     */
    void insertBatch(List<SetmealDish> setmealDishes);

    /**
     * 根据套餐id查询套餐中的菜品
     * @param id
     * @return
     */
    @Select("select * from setmeal_dish where setmeal_id = #{id}")
    List<SetmealDish> getBySetMealId(Long id);

    /**
     * 根据套餐id删除套餐中的菜品
     * @param id
     */
    @Delete("delete from setmeal_dish where setmeal_id = #{id}")
    void deleteBySetMealId(Long id);
}
