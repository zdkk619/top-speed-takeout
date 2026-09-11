package com.zdkk.speed.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zdkk.speed.constant.StatusConstant;
import com.zdkk.speed.context.BaseContext;
import com.zdkk.speed.dto.CategoryDTO;
import com.zdkk.speed.dto.CategoryPageQueryDTO;
import com.zdkk.speed.entity.Category;
import com.zdkk.speed.exception.AccountAlreadyExistException;
import com.zdkk.speed.exception.CategoryAlreadyExistsException;
import com.zdkk.speed.mapper.CategoryMapper;
import com.zdkk.speed.result.PageResult;
import com.zdkk.speed.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;

import static com.zdkk.speed.constant.MessageConstant.CATEGORY_ALREADY_EXISTS;

@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 分类分页查询
     * @param categoryPageQueryDTO
     * @return
     */
    @Override
    public PageResult pageQuery(CategoryPageQueryDTO categoryPageQueryDTO) {
        PageHelper.startPage(categoryPageQueryDTO.getPage(), categoryPageQueryDTO.getPageSize());
        Page<Category> page = categoryMapper.pageQuery(categoryPageQueryDTO);
        log.info("page: {}", page);
        long total = page.getTotal();
        return new PageResult(total, page.getResult());
    }

    /**
     * 新增分类
     * @param categoryDTO
     */
    @Override
    public void save(CategoryDTO categoryDTO) {
        String name = categoryDTO.getName();
        Integer type = categoryDTO.getType();
        if (categoryMapper.getByNameAndType(name, type) != null) {
            throw new CategoryAlreadyExistsException(CATEGORY_ALREADY_EXISTS);
        }
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO, category);
        category.setStatus(StatusConstant.DISABLE);

        category.setCreateTime(LocalDateTime.now());
        category.setUpdateTime(LocalDateTime.now());
        category.setCreateUser(BaseContext.getCurrentId());
        category.setUpdateUser(BaseContext.getCurrentId());
        categoryMapper.insert(category);
    }
}
