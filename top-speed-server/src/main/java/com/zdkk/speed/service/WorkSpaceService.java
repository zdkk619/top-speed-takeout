package com.zdkk.speed.service;

import com.zdkk.speed.vo.BusinessDataVO;
import com.zdkk.speed.vo.DishOverViewVO;
import com.zdkk.speed.vo.OrderOverViewVO;
import com.zdkk.speed.vo.SetmealOverViewVO;

/**
 * 工作台服务类
 */
public interface WorkSpaceService {
    /**
     * 获取业务数据
     * @return
     */
    BusinessDataVO getBusinessData();

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
