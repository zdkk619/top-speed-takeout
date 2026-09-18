package com.zdkk.speed.controller.admin;

import com.zdkk.speed.result.Result;
import com.zdkk.speed.service.WorkSpaceService;
import com.zdkk.speed.vo.BusinessDataVO;
import com.zdkk.speed.vo.DishOverViewVO;
import com.zdkk.speed.vo.OrderOverViewVO;
import com.zdkk.speed.vo.SetmealOverViewVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@RequestMapping("/admin/workspace")
@RestController
@Slf4j
@Tag(name = "工作台管理", description = "工作台管理相关接口")
public class WorkSpaceController {
    @Autowired
    private WorkSpaceService workSpaceService;
    @GetMapping("/businessData")
    @Operation(summary = "获取业务数据", description = "获取业务数据")
    public Result<BusinessDataVO> getBusinessData() {
        LocalDateTime begin = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime end = LocalDateTime.now();
        return Result.success(workSpaceService.getBusinessData(begin, end));
    }

    @GetMapping("/overviewOrders")
    @Operation(summary = "获取订单概览数据", description = "获取订单概览数据")
    public Result<OrderOverViewVO> getOverviewOrders() {
        return Result.success(workSpaceService.getOverviewOrders());
    }

    @GetMapping("/overviewSetmeals")
    @Operation(summary = "获取套餐概览数据", description = "获取套餐概览数据")
    public Result<SetmealOverViewVO> getOverviewSetMeals() {
        return Result.success(workSpaceService.getOverviewSetMeals());
    }

    @GetMapping("/overviewDishes")
    @Operation(summary = "获取菜品概览数据", description = "获取菜品概览数据")
    public Result<DishOverViewVO> getOverviewDishes() {
        return Result.success(workSpaceService.getOverviewDishes());
    }
}
