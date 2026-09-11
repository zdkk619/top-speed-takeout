package com.zdkk.speed.mapper;

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
}
