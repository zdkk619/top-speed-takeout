package com.zdkk.speed.service.impl;

import com.zdkk.speed.context.BaseContext;
import com.zdkk.speed.dto.ShoppingCartDTO;
import com.zdkk.speed.entity.Dish;
import com.zdkk.speed.entity.SetMeal;
import com.zdkk.speed.entity.ShoppingCart;
import com.zdkk.speed.mapper.DishMapper;
import com.zdkk.speed.mapper.SetMealMapper;
import com.zdkk.speed.mapper.ShoppingCartMapper;
import com.zdkk.speed.result.Result;
import com.zdkk.speed.service.ShoppingCartService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetMealMapper setMealMapper;

    @Override
    public List<ShoppingCart> list() {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUserId(BaseContext.getCurrentId());
        return shoppingCartMapper.list(shoppingCart);
    }

    @Override
    public void add(ShoppingCartDTO shoppingCartDTO) {
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        shoppingCart.setUserId(BaseContext.getCurrentId());
        List<ShoppingCart> shoppingCartList = shoppingCartMapper.list(shoppingCart);
        if (shoppingCartList != null && !shoppingCartList.isEmpty()) {
            // 已存在，数量+1
            ShoppingCart existShoppingCart = shoppingCartList.getFirst();
            existShoppingCart.setNumber(existShoppingCart.getNumber() + 1);
            shoppingCartMapper.updateNumberById(existShoppingCart);
        } else {
            if (shoppingCartDTO.getDishId() != null) {
                Dish dish = dishMapper.getById(shoppingCartDTO.getDishId());
                shoppingCart.setName(dish.getName());
                shoppingCart.setAmount(dish.getPrice());
                shoppingCart.setImage(dish.getImage());
            } else {
                SetMeal setMeal = setMealMapper.getById(shoppingCartDTO.getSetmealId());
                shoppingCart.setName(setMeal.getName());
                shoppingCart.setAmount(setMeal.getPrice());
                shoppingCart.setImage(setMeal.getImage());
            }
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartMapper.insert(shoppingCart);
        }
    }

    @Override
    public void sub(ShoppingCartDTO shoppingCartDTO) {
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        shoppingCart.setUserId(BaseContext.getCurrentId());
        List<ShoppingCart> shoppingCartList = shoppingCartMapper.list(shoppingCart);

        if (shoppingCartList != null && !shoppingCartList.isEmpty()) {
            ShoppingCart first = shoppingCartList.getFirst();
            if (first.getNumber() > 1) {
                first.setNumber(first.getNumber() - 1);
                shoppingCartMapper.updateNumberById(first);
            } else {
                shoppingCartMapper.deleteById(first.getId());
            }
        }
    }

    @Override
    public void clean() {
        shoppingCartMapper.deleteByUserId(BaseContext.getCurrentId());
    }
}
