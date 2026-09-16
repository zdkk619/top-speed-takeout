package com.zdkk.speed.vo;

import com.zdkk.speed.entity.OrderDetail;
import com.zdkk.speed.entity.Orders;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderVO extends Orders implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    //订单菜品信息
    private String orderDishes;

    //订单详情
    private List<OrderDetail> orderDetailList;
}
