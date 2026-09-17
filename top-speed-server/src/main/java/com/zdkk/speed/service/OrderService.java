package com.zdkk.speed.service;

import com.zdkk.speed.dto.*;
import com.zdkk.speed.result.PageResult;
import com.zdkk.speed.vo.OrderPaymentVO;
import com.zdkk.speed.vo.OrderStatisticsVO;
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
     * 用户取消订单
     * @param id
     */
    void userCancel(Long id) throws Exception;

    /**
     * 再来一单
     * @param id
     */
    void repetition(Long id);

    /**
     * 根据条件查询订单列表
     * @param ordersPageQueryDTO
     * @return
     */
    PageResult conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 统计不同状态的订单数量
     * @return
     */
    OrderStatisticsVO getOrderStatistics();

    /**
     * 接受订单
     * @param ordersConfirmDTO
     */
    void confirm(OrdersConfirmDTO ordersConfirmDTO);

    /**
     * 拒绝订单
     * @param ordersRejectionDTO
     */
    void rejection(OrdersRejectionDTO ordersRejectionDTO) throws Exception;

    /**
     * 商家取消订单
     * @param ordersCancelDTO
     */
    void cancel(OrdersCancelDTO ordersCancelDTO);

    /**
     * 订单派送
     * @param id
     */
    void delivery(Long id);

    /**
     * 订单完成
     * @param id
     */
    void complete(Long id);

    /**
     * 催单
     * @param id
     */
    void reminder(Long id);
}
