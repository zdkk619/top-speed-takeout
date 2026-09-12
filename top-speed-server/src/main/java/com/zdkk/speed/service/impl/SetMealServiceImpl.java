package com.zdkk.speed.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zdkk.speed.constant.MessageConstant;
import com.zdkk.speed.constant.StatusConstant;
import com.zdkk.speed.dto.SetMealDTO;
import com.zdkk.speed.dto.SetMealPageQueryDTO;
import com.zdkk.speed.entity.Category;
import com.zdkk.speed.entity.Dish;
import com.zdkk.speed.entity.SetMeal;
import com.zdkk.speed.entity.SetmealDish;
import com.zdkk.speed.exception.CategoryTypeNotFoundException;
import com.zdkk.speed.exception.DeleteNotAllowedException;
import com.zdkk.speed.exception.SetMealAlreadyExistsException;
import com.zdkk.speed.exception.SetMealEnableFailedException;
import com.zdkk.speed.mapper.CategoryMapper;
import com.zdkk.speed.mapper.DishMapper;
import com.zdkk.speed.mapper.SetMealDishMapper;
import com.zdkk.speed.mapper.SetMealMapper;
import com.zdkk.speed.result.PageResult;
import com.zdkk.speed.service.AutoFillService;
import com.zdkk.speed.service.SetMealService;
import com.zdkk.speed.vo.SetMealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class SetMealServiceImpl implements SetMealService {
    @Autowired
    private SetMealMapper setMealMapper;

    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private SetMealDishMapper setmealDishMapper;

    @Autowired
    private AutoFillService autoFillService;
    @Autowired
    private DishMapper dishMapper;

    @Override
    public PageResult pageQuery(SetMealPageQueryDTO setMealPageQueryDTO) {
        PageHelper.startPage(setMealPageQueryDTO.getPage(), setMealPageQueryDTO.getPageSize());
        Page<SetMealVO> page = setMealMapper.pageQuery(setMealPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Transactional
    @Override
    public void save(SetMealDTO setMealDTO) {
        if (setMealMapper.getByName(setMealDTO.getName()) != null) {
            throw new SetMealAlreadyExistsException(MessageConstant.DATA_ALREADY_EXISTS);
        }

        if (categoryMapper.getByIdAndType(setMealDTO.getCategoryId(), SetMealDTO.TYPE) == null) {
            throw new CategoryTypeNotFoundException(MessageConstant.CATEGORY_TYPE_NOT_FOUND);
        }

        SetMeal setMeal = new SetMeal();
        BeanUtils.copyProperties(setMealDTO, setMeal);
        autoFillService.insert(setMeal, setMealMapper::insert);

        List<SetmealDish> setmealDishes = setMealDTO.getSetmealDishes();
        if (setmealDishes != null && !setmealDishes.isEmpty()) {
            setmealDishes.forEach(setmealDish -> {
                setmealDish.setSetmealId(setMeal.getId());
            });
            setmealDishMapper.insertBatch(setmealDishes);
        }
    }

    @Override
    public SetMealVO getById(Long id) {
        SetMeal setMeal = setMealMapper.getById(id);
        SetMealVO setMealVO = new SetMealVO();
        BeanUtils.copyProperties(setMeal, setMealVO);
        List<SetmealDish> setmealDishes = setmealDishMapper.getBySetMealId(id);
        setMealVO.setSetmealDishes(setmealDishes);
        return setMealVO;
    }

    @Transactional
    @Override
    public void update(SetMealDTO setMealDTO) {
        SetMeal setMeal = new SetMeal();
        BeanUtils.copyProperties(setMealDTO, setMeal);
        autoFillService.update(setMeal, setMealMapper::update);

        setmealDishMapper.deleteBySetMealId(setMeal.getId());

        List<SetmealDish> setmealDishes = setMealDTO.getSetmealDishes();
        if (setmealDishes != null && !setmealDishes.isEmpty()) {
            setmealDishes.forEach(setmealDish -> {
                setmealDish.setSetmealId(setMeal.getId());
            });
            setmealDishMapper.insertBatch(setmealDishes);
        }
    }

    @Override
    public void enableOrDisable(Integer status, Long id) {
        if (Objects.equals(status, StatusConstant.ENABLE)) {
            List<Dish> dishes = dishMapper.getBySetMealId(id);
            if (dishes != null && !dishes.isEmpty()) {
                long count = dishes.stream().filter(dish -> dish.getStatus().equals(StatusConstant.DISABLE)).count();
                if (count > 0) {
                    throw new SetMealEnableFailedException(MessageConstant.SETMEAL_ENABLE_FAILED);
                }
            }
        }

        SetMeal setMeal = SetMeal.builder()
                .id(id)
                .status(status)
                .build();
        autoFillService.update(setMeal, setMealMapper::update);
    }

    @Transactional
    @Override
    public void delete(List<Long> ids) {
        // 判断套餐状态，启用状态不能删
        ids.forEach(id -> {
            SetMeal setMeal = setMealMapper.getById(id);
            if (Objects.equals(setMeal.getStatus(), StatusConstant.ENABLE)) {
                throw new DeleteNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            }
        });
        // 删除套餐包含的菜品
        // 删除套餐
        ids.forEach(id -> {
            setmealDishMapper.deleteBySetMealId(id);
            setMealMapper.delete(id);
        });
    }
}
