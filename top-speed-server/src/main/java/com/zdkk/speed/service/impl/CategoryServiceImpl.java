package com.zdkk.speed.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zdkk.speed.annotation.AutoFill;
import com.zdkk.speed.constant.MessageConstant;
import com.zdkk.speed.constant.StatusConstant;
import com.zdkk.speed.context.BaseContext;
import com.zdkk.speed.dto.CategoryDTO;
import com.zdkk.speed.dto.CategoryPageQueryDTO;
import com.zdkk.speed.entity.Category;
import com.zdkk.speed.enumeration.OperationType;
import com.zdkk.speed.exception.AccountAlreadyExistException;
import com.zdkk.speed.exception.CategoryAlreadyExistsException;
import com.zdkk.speed.exception.DeletionNotAllowedException;
import com.zdkk.speed.mapper.CategoryMapper;
import com.zdkk.speed.mapper.DishMapper;
import com.zdkk.speed.mapper.SetMealMapper;
import com.zdkk.speed.result.PageResult;
import com.zdkk.speed.service.AutoFillService;
import com.zdkk.speed.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.zdkk.speed.constant.MessageConstant.CATEGORY_ALREADY_EXISTS;

@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private SetMealMapper setMealMapper;

    @Autowired
    private AutoFillService autoFillService;

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

        autoFillService.insert(category, categoryMapper::insert);
    }

    @Override
    public void enableOrDisable(Integer status, Long id) {
        Category category = Category.builder()
                .id(id)
                .status(status)
                .build();
        autoFillService.update(category, categoryMapper::update);
    }

    @Override
    public void update(CategoryDTO categoryDTO) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO, category);
        autoFillService.update(category, categoryMapper::update);
    }

    @Override
    public List<Category> getByType(Integer type) {
        return categoryMapper.list(type);
    }

    @Override
    public void deleteById(Long id) {
        if (dishMapper.countByCategoryId(id) > 0) {
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_DISH);
        }
        if (setMealMapper.countByCategoryId(id) > 0) {
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_SETMEAL);
        }
        categoryMapper.deleteById(id);
    }
}
