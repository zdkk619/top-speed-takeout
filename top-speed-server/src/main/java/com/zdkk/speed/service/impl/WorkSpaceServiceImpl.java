package com.zdkk.speed.service.impl;

import com.zdkk.speed.entity.Orders;
import com.zdkk.speed.mapper.DishMapper;
import com.zdkk.speed.mapper.OrderMapper;
import com.zdkk.speed.mapper.SetMealMapper;
import com.zdkk.speed.mapper.UserMapper;
import com.zdkk.speed.service.WorkSpaceService;
import com.zdkk.speed.vo.BusinessDataVO;
import com.zdkk.speed.vo.DishOverViewVO;
import com.zdkk.speed.vo.OrderOverViewVO;
import com.zdkk.speed.vo.SetmealOverViewVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class WorkSpaceServiceImpl implements WorkSpaceService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetMealMapper setMealMapper;
    @Override
    public BusinessDataVO getBusinessData() {
        LocalDateTime begin = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime end = LocalDateTime.now();
        BusinessDataVO businessDataVO = new BusinessDataVO();

        // 查询营业额
        Map<String, Object> map = new HashMap<>();
        map.put("begin", begin);
        map.put("end", end);
        map.put("status", Orders.COMPLETED);
        Double turnover = orderMapper.sumByMap(map);

        // 查询有效订单数
        Integer validOrderCount = orderMapper.countByMap(map);

        map.remove("status");
        // 查询新增用户数
        Integer newUsers = userMapper.countByMap(map);

        // 查询总订单数
        Integer totalOrders = orderMapper.countByMap(map);

        businessDataVO.setTurnover(turnover);
        businessDataVO.setValidOrderCount(validOrderCount);
        businessDataVO.setNewUsers(newUsers);
        businessDataVO.setOrderCompletionRate((double) validOrderCount / totalOrders);
        businessDataVO.setUnitPrice(turnover / validOrderCount);
        return businessDataVO;
    }

    @Override
    public OrderOverViewVO getOverviewOrders() {
        LocalDateTime begin = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime end = LocalDateTime.now();
        return orderMapper.getOverviewOrders(begin, end);
    }

    @Override
    public SetmealOverViewVO getOverviewSetMeals() {
        return setMealMapper.getOverviewSetMeals();
    }

    @Override
    public DishOverViewVO getOverviewDishes() {
        return dishMapper.getOverviewDishes();
    }
}
