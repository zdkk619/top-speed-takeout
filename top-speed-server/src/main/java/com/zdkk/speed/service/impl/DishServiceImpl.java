package com.zdkk.speed.service.impl;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zdkk.speed.constant.MessageConstant;
import com.zdkk.speed.constant.StatusConstant;
import com.zdkk.speed.dto.DishDTO;
import com.zdkk.speed.dto.DishPageQueryDTO;
import com.zdkk.speed.entity.Dish;
import com.zdkk.speed.entity.DishFlavor;
import com.zdkk.speed.entity.SetMeal;
import com.zdkk.speed.exception.CategoryTypeNotFoundException;
import com.zdkk.speed.exception.DeleteNotAllowedException;
import com.zdkk.speed.exception.DishAlreadyExistsException;
import com.zdkk.speed.mapper.*;
import com.zdkk.speed.result.PageResult;
import com.zdkk.speed.service.AutoFillService;
import com.zdkk.speed.service.DishService;
import com.zdkk.speed.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class DishServiceImpl implements DishService {
    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    @Autowired
    private AutoFillService autoFillService;

    @Autowired
    private SetMealDishMapper setMealDishMapper;

    @Transactional
    @Override
    public void save(DishDTO dishDTO) {
        if (dishMapper.getByName(dishDTO.getName()) != null) {
            throw new DishAlreadyExistsException(MessageConstant.DATA_ALREADY_EXISTS);
        }
        if (categoryMapper.getByIdAndType(dishDTO.getCategoryId(), DishDTO.TYPE) == null) {
            throw new CategoryTypeNotFoundException(MessageConstant.CATEGORY_TYPE_NOT_FOUND);
        }

        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        autoFillService.insert(dish, dishMapper::insert);

        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors != null && !flavors.isEmpty()) {
            flavors.forEach(flavor -> {
                flavor.setDishId(dish.getId());
            });
            dishFlavorMapper.insertBatch(flavors);
        }
    }

    @Override
    public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());
        Page<DishVO> page = dishMapper.pageQuery(dishPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Transactional
    @Override
    public void delete(List<Long> ids) {
        // 判断当前菜品能否删除
        // 1. 菜品是停用状态
        for (Long id : ids) {
            Dish dish = dishMapper.getById(id);
            if (Objects.equals(dish.getStatus(), StatusConstant.ENABLE)) {
                throw new DeleteNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }

        // 2. 菜品没有关联套餐
        List<Long> setMealIds =  setMealDishMapper.getSetMealsByDishIds(ids);
        if (setMealIds != null && !setMealIds.isEmpty()) {
            throw new DeleteNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }

        // 可以删除，需将口味一并删除
        for (Long id : ids) {
            dishMapper.deleteById(id);
            dishFlavorMapper.deleteByDishId(id);
        }
    }

    @Override
    public void enableOrDisable(Long id, Integer status) {
        Dish dish = Dish.builder()
                .id(id)
                .status(status)
                .build();
        autoFillService.update(dish, dishMapper::update);
    }

    @Override
    public DishVO getById(Long id) {
        Dish dish = dishMapper.getById(id);
        List<DishFlavor> flavors = dishFlavorMapper.getByDishId(id);
        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish, dishVO);
        dishVO.setFlavors(flavors);
        return dishVO;
    }

    @Transactional
    @Override
    public void update(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        // 更新菜品
        autoFillService.update(dish, dishMapper::update);

        // 更新菜品口味，先删除旧口味
        dishFlavorMapper.deleteByDishId(dishDTO.getId());

        // 插入新口味
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors != null && !flavors.isEmpty()) {
            flavors.forEach(flavor -> {
                flavor.setDishId(dishDTO.getId());
            });
            dishFlavorMapper.insertBatch(flavors);
        }
    }

    @Override
    public List<Dish> list(Long categoryId) {
        Dish dish = Dish.builder()
                .categoryId(categoryId)
                .status(StatusConstant.ENABLE)
                .build();
        return dishMapper.list(dish);
    }

    @Override
    public List<DishVO> listWithFlavor(Dish dish) {
        List<Dish> dishList = dishMapper.list(dish);
        ArrayList<DishVO> dishVOList = new ArrayList<>();
        for (Dish dishItem : dishList) {
            DishVO dishVO = new DishVO();
            BeanUtils.copyProperties(dishItem, dishVO);
            List<DishFlavor> flavors = dishFlavorMapper.getByDishId(dishItem.getId());
            dishVO.setFlavors(flavors);
            dishVOList.add(dishVO);
        }
        return dishVOList;
    }
}
