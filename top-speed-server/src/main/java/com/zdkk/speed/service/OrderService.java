package com.zdkk.speed.service;

import com.zdkk.speed.dto.OrdersSubmitDTO;
import com.zdkk.speed.vo.OrderSubmitVO;

public interface OrderService {
    /**
     * 提交订单
     * @param orderSubmitDTO
     * @return
     */
    OrderSubmitVO submitOrder(OrdersSubmitDTO orderSubmitDTO);
}
