package com.zdkk.speed.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SetMealMapper {
    /**
     * 根据分类id统计套餐数量
     * @param id
     * @return
     */
    @Select("SELECT COUNT(*) FROM setmeal WHERE category_id = #{id}")
    int countByCategoryId(Long id);
}
