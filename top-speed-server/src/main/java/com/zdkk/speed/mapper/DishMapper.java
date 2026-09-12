package com.zdkk.speed.mapper;

import com.github.pagehelper.Page;
import com.zdkk.speed.dto.DishPageQueryDTO;
import com.zdkk.speed.entity.Dish;
import com.zdkk.speed.vo.DishVO;
import jakarta.validation.constraints.NotBlank;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

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
}
