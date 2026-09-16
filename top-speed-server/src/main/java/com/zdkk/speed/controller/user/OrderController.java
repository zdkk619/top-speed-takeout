package com.zdkk.speed.controller.user;

import com.zdkk.speed.dto.OrdersPaymentDTO;
import com.zdkk.speed.dto.OrdersSubmitDTO;
import com.zdkk.speed.result.PageResult;
import com.zdkk.speed.result.Result;
import com.zdkk.speed.service.OrderService;
import com.zdkk.speed.vo.OrderPaymentVO;
import com.zdkk.speed.vo.OrderSubmitVO;
import com.zdkk.speed.vo.OrderVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController("userOrderController")
@RequestMapping("/user/order")
@Slf4j
@Tag(name = "用户订单管理")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/submit")
    @Operation(summary = "提交订单", description = "提交订单")
    public Result<OrderSubmitVO> submit(@RequestBody OrdersSubmitDTO orderSubmitDTO) {
        log.info("提交订单：{}", orderSubmitDTO);
        OrderSubmitVO orderSubmitVO = orderService.submitOrder(orderSubmitDTO);
        return Result.success(orderSubmitVO);
    }

    @PutMapping("/payment")
    @Operation(summary = "订单支付", description = "订单支付")
    public Result<OrderPaymentVO> payment(@RequestBody OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        log.info("订单支付：{}", ordersPaymentDTO);

        // 调用支付接口，生成预支付订单
        // 这里由于不是个体商户，所以先不走，先占位
//        OrderPaymentVO orderPaymentVO = orderService.payment(ordersPaymentDTO);
//        log.info("生成预支付订单：{}", orderPaymentVO);
        OrderPaymentVO orderPaymentVO = new OrderPaymentVO();

        // 模拟支付成功，修改订单状态、来单提醒
        orderService.paySuccess(ordersPaymentDTO.getOrderNumber());
        return Result.success(orderPaymentVO);
    }

    @GetMapping("/historyOrders")
    @Operation(summary = "历史订单列表", description = "用户历史订单列表")
    public Result<PageResult> historyOrders(Integer page, Integer pageSize, Integer status) {
        log.info("查询用户订单列表：page = {}, pageSize = {}, status = {}", page, pageSize, status);
        PageResult pageResult = orderService.historyOrders(page, pageSize, status);
        return Result.success(pageResult);
    }

    @GetMapping("/orderDetail/{id}")
    @Operation(summary = "订单详情", description = "订单详情")
    public Result<OrderVO> orderDetail(@PathVariable Long id) {
        log.info("查询订单详情：{}", id);
        OrderVO orderVO = orderService.orderDetail(id);
        return Result.success(orderVO);
    }

    @PutMapping("/cancel/{id}")
    @Operation(summary = "取消订单", description = "取消订单")
    public Result<String> cancel(@PathVariable Long id) throws Exception {
        log.info("取消订单：{}", id);
        orderService.userCancel(id);
        return Result.success();
    }

    @PostMapping("/repetition/{id}")
    @Operation(summary = "再来一单", description = "再来一单")
    public Result<String> repetition(@PathVariable Long id) {
        log.info("再来一单：{}", id);
        orderService.repetition(id);
        return Result.success();
    }
}
