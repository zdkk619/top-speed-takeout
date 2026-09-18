package com.zdkk.speed.service;

import com.zdkk.speed.vo.BusinessDataVO;
import com.zdkk.speed.vo.DishOverViewVO;
import com.zdkk.speed.vo.OrderOverViewVO;
import com.zdkk.speed.vo.SetmealOverViewVO;

import java.time.LocalDateTime;

/**
 * 工作台服务类
 */
public interface WorkSpaceService {

    /**
     * 获取业务数据
     * @param begin 开始时间
     * @param end 结束时间
     * @return
     */
    BusinessDataVO getBusinessData(LocalDateTime begin, LocalDateTime end);

    /**
     * 获取概览订单数据
     * @return
     */
    OrderOverViewVO getOverviewOrders();

    /**
     * 获取套餐概览数据
     * @return
     */
    SetmealOverViewVO getOverviewSetMeals();

    /**
     * 获取菜品概览数据
     * @return
     */
    DishOverViewVO getOverviewDishes();
}
