package com.zdkk.speed.service;

import com.zdkk.speed.dto.OrdersPaymentDTO;
import com.zdkk.speed.dto.OrdersSubmitDTO;
import com.zdkk.speed.vo.OrderPaymentVO;
import com.zdkk.speed.vo.OrderSubmitVO;

import java.io.IOException;

public interface OrderService {
    /**
     * 提交订单
     * @param orderSubmitDTO
     * @return
     */
    OrderSubmitVO submitOrder(OrdersSubmitDTO orderSubmitDTO);

    /**
     * 订单支付
     * @param ordersPaymentDTO
     * @return
     */
    OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception;
}
