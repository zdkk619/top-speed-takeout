package com.zdkk.speed.service.impl;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zdkk.speed.constant.MessageConstant;
import com.zdkk.speed.dto.DishDTO;
import com.zdkk.speed.dto.DishPageQueryDTO;
import com.zdkk.speed.entity.Dish;
import com.zdkk.speed.entity.DishFlavor;
import com.zdkk.speed.exception.CategoryTypeNotFoundException;
import com.zdkk.speed.exception.DishAlreadyExistsException;
import com.zdkk.speed.mapper.CategoryMapper;
import com.zdkk.speed.mapper.DishFlavorMapper;
import com.zdkk.speed.mapper.DishMapper;
import com.zdkk.speed.result.PageResult;
import com.zdkk.speed.service.AutoFillService;
import com.zdkk.speed.service.DishService;
import com.zdkk.speed.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
}
