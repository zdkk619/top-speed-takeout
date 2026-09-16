package com.zdkk.speed.service;

import com.zdkk.speed.dto.OrdersPaymentDTO;
import com.zdkk.speed.dto.OrdersSubmitDTO;
import com.zdkk.speed.result.PageResult;
import com.zdkk.speed.vo.OrderPaymentVO;
import com.zdkk.speed.vo.OrderSubmitVO;
import com.zdkk.speed.vo.OrderVO;

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

    /**
     * 支付成功
     * @param outTradeNo
     */
    void paySuccess(String outTradeNo);

    /**
     * 历史订单列表
     * @param page
     * @param pageSize
     * @param status
     * @return
     */
    PageResult historyOrders(Integer page, Integer pageSize, Integer status);

    /**
     * 查询订单详情
     * @param id
     * @return
     */
    OrderVO orderDetail(Long id);

    /**
     * 取消订单
     * @param id
     */
    void cancel(Long id) throws Exception;

    /**
     * 再来一单
     * @param id
     */
    void repetition(Long id);
}
