package com.zdkk.speed.mapper;

import com.github.pagehelper.Page;
import com.zdkk.speed.dto.CategoryPageQueryDTO;
import com.zdkk.speed.entity.Category;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CategoryMapper {
    /**
     * 分页查询分类
     * @param categoryPageQueryDTO
     * @return
     */
    Page<Category> pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);
}
