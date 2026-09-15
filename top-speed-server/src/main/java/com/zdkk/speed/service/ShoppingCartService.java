package com.zdkk.speed.service;

import com.zdkk.speed.dto.ShoppingCartDTO;
import com.zdkk.speed.entity.ShoppingCart;
import com.zdkk.speed.result.Result;

import java.util.List;

public interface ShoppingCartService {
    /**
     * 查询购物车商品列表
     * @return
     */
    List<ShoppingCart> list();

    /**
     * 添加购物车
     * @param shoppingCartDTO
     */
    void add(ShoppingCartDTO shoppingCartDTO);


    /**
     * 减少购物车商品
     * @param shoppingCartDTO
     */
    void sub(ShoppingCartDTO shoppingCartDTO);

    /**
     * 清空购物车
     */
    void clean();
}
