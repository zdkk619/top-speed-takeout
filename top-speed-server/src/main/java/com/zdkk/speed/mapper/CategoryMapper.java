package com.zdkk.speed.mapper;

import com.github.pagehelper.Page;
import com.zdkk.speed.dto.CategoryPageQueryDTO;
import com.zdkk.speed.entity.Category;
import jakarta.validation.constraints.NotNull;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CategoryMapper {
    /**
     * 分页查询分类
     * @param categoryPageQueryDTO
     * @return
     */
    Page<Category> pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);

    /**
     * 新增分类
     * @param category
     */
    @Insert("INSERT INTO category (type, name, sort, status, create_time, update_time, create_user, update_user) " +
            "VALUES (#{type}, #{name}, #{sort}, #{status}, #{createTime}, #{updateTime}, #{createUser}, #{updateUser})")
    void insert(Category category);

    /**
     * 根据名称和类型查询分类
     * @param name
     * @param type
     * @return
     */
    @Select("SELECT * FROM category WHERE name = #{name} AND type = #{type}")
    Category getByNameAndType(String name, Integer type);

    /**
     * 更新分类
     * @param category
     */
    void update(Category category);

    /**
     * 根据类型查询分类
     * @param type
     * @return
     */
    @Select("SELECT * FROM category WHERE type = #{type} ORDER BY sort ASC, update_time DESC")
    List<Category> list(Integer type);

    /**
     * 根据id删除分类
     * @param id
     */
    @Delete("DELETE FROM category WHERE id = #{id}")
    void deleteById(Long id);

    /**
     * 根据分类id和类型查询分类
     * @param categoryId
     * @param type
     * @return
     */
    @Select("SELECT * FROM category WHERE id = #{categoryId} AND type = #{type}")
    Category getByIdAndType(@NotNull Long categoryId, int type);
}
