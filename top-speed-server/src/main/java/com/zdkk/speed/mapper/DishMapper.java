package com.zdkk.speed.mapper;

import com.github.pagehelper.Page;
import com.zdkk.speed.dto.DishPageQueryDTO;
import com.zdkk.speed.entity.Dish;
import com.zdkk.speed.vo.DishVO;
import jakarta.validation.constraints.NotBlank;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishMapper {
    /**
     * 根据分类id统计菜品数量
     * @param id
     * @return
     */
    @Select("SELECT COUNT(*) FROM dish WHERE category_id = #{id}")
    int countByCategoryId(Long id);

    /**
     * 根据名称查询菜品
     * @param name
     * @return
     */
    @Select("SELECT * FROM dish WHERE name = #{name}")
    Dish getByName(String name);

    /**
     * 插入菜品
     * @param dish
     */
    void insert(Dish dish);

    /**
     * 分页查询菜品
     * @param dishPageQueryDTO
     * @return
     */
    Page<DishVO> pageQuery(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 根据id查询菜品
     * @param id
     * @return
     */
    @Select("SELECT * FROM dish WHERE id = #{id}")
    Dish getById(Long id);

    /**
     * 根据id删除菜品
     * @param id
     */
    @Select("delete from dish where id = #{id}")
    void deleteById(Long id);

    /**
     * 更新菜品
     * @param dish
     */
    void update(Dish dish);

    /**
     * 根据分类id查询菜品
     * @param dish
     * @return
     */
    List<Dish> list(Dish dish);

    /**
     * 根据套餐id查询菜品
     * @param setMealId
     * @return
     */
    @Select("select d.* from dish d join setmeal_dish s on d.id = s.dish_id where s.setmeal_id = #{setMealId}")
    List<Dish> getBySetMealId(Long setMealId);
}
