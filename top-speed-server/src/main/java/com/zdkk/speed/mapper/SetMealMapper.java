package com.zdkk.speed.mapper;

import com.github.pagehelper.Page;
import com.zdkk.speed.dto.SetMealDTO;
import com.zdkk.speed.dto.SetMealPageQueryDTO;
import com.zdkk.speed.entity.SetMeal;
import com.zdkk.speed.vo.DishItemVO;
import com.zdkk.speed.vo.SetMealVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetMealMapper {
    /**
     * 根据分类id统计套餐数量
     * @param id
     * @return
     */
    @Select("SELECT COUNT(*) FROM setmeal WHERE category_id = #{id}")
    int countByCategoryId(Long id);

    /**
     * 分页查询套餐列表
     * @param setMealPageQueryDTO
     * @return
     */
    Page<SetMealVO> pageQuery(SetMealPageQueryDTO setMealPageQueryDTO);

    /**
     * 根据名称查询套餐
     * @param name
     * @return
     */
    @Select("SELECT * FROM setmeal WHERE name = #{name}")
    SetMeal getByName(String name);

    /**
     * 新增套餐
     * @param setMeal
     */
    void insert(SetMeal setMeal);

    /**
     * 根据id查询套餐
     * @param id
     * @return
     */
    @Select("SELECT * FROM setmeal WHERE id = #{id}")
    SetMeal getById(Long id);

    /**
     * 更新套餐
     * @param setMeal
     */
    void update(SetMeal setMeal);

    /**
     * 根据id删除套餐
     * @param id
     */
    @Delete("DELETE FROM setmeal WHERE id = #{id}")
    void delete(Long id);

    /**
     * 根据条件查询套餐列表
     * @param setMeal
     * @return
     */
    List<SetMeal> list(SetMeal setMeal);

    /**
     * 根据套餐id查询菜品列表
     * @param id
     * @return
     */
    @Select("select sd.name, sd.copies, d.image, d.description from setmeal_dish sd join dish d on sd.dish_id = d.id where sd.setmeal_id = #{id}")
    List<DishItemVO> getDishItemsBySetMealId(Long id);
}
