package com.zdkk.speed.controller.admin;

import com.zdkk.speed.dto.OrdersCancelDTO;
import com.zdkk.speed.dto.OrdersConfirmDTO;
import com.zdkk.speed.dto.OrdersPageQueryDTO;
import com.zdkk.speed.dto.OrdersRejectionDTO;
import com.zdkk.speed.result.PageResult;
import com.zdkk.speed.result.Result;
import com.zdkk.speed.service.OrderService;
import com.zdkk.speed.vo.OrderStatisticsVO;
import com.zdkk.speed.vo.OrderVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("adminOrderController")
@RequestMapping("/admin/order")
@Slf4j
@Tag(name = "订单管理", description = "订单管理")
public class OrderController {

    @Autowired
    private OrderService orderService;
    @GetMapping("/conditionSearch")
    @Operation(summary = "条件查询订单", description = "条件查询订单")
    public Result<PageResult> conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO) {
        log.info("条件查询订单：{}", ordersPageQueryDTO);
        PageResult pageResult = orderService.conditionSearch(ordersPageQueryDTO);
        return Result.success(pageResult);
    }

    @GetMapping("/statistics")
    @Operation(summary = "订单统计", description = "订单统计")
    public Result<OrderStatisticsVO> orderStatistics() {
        OrderStatisticsVO orderStatisticsVO = orderService.getOrderStatistics();
        log.info("订单统计 {}", orderStatisticsVO);
        return Result.success(orderStatisticsVO);
    }

    @GetMapping("/details/{id}")
    @Operation(summary = "订单详情", description = "订单详情")
    public Result<OrderVO> orderDetail(@PathVariable Long id) {
        log.info("订单详情：{}", id);
        return Result.success(orderService.orderDetail(id));
    }

    @PutMapping("/confirm")
    @Operation(summary = "接受订单", description = "接受订单")
    public Result<String> confirm(@RequestBody OrdersConfirmDTO ordersConfirmDTO) {
        log.info("接受订单：{}", ordersConfirmDTO);
        orderService.confirm(ordersConfirmDTO);
        return Result.success();
    }

    @PutMapping("/rejection")
    @Operation(summary = "拒绝订单", description = "拒绝订单")
    public Result<String> rejection(@RequestBody OrdersRejectionDTO ordersRejectionDTO) throws Exception {
        log.info("拒绝订单：{}", ordersRejectionDTO);
        orderService.rejection(ordersRejectionDTO);
        return Result.success();
    }

    @PutMapping("/cancel")
    @Operation(summary = "取消订单", description = "取消订单")
    public Result<String> cancel(@RequestBody OrdersCancelDTO ordersCancelDTO) throws Exception {
        log.info("取消订单：{}", ordersCancelDTO);
        orderService.cancel(ordersCancelDTO);
        return Result.success();
    }

    @PutMapping("/delivery/{id}")
    @Operation(summary = "订单派送", description = "订单派送")
    public Result<String> delivery(@PathVariable Long id) {
        log.info("订单开始派送：{}", id);
        orderService.delivery(id);
        return Result.success();
    }

    @PutMapping("/complete/{id}")
    @Operation(summary = "订单完成", description = "订单完成")
    public Result<String> complete(@PathVariable Long id) {
        log.info("订单完成：{}", id);
        orderService.complete(id);
        return Result.success();
    }
}
