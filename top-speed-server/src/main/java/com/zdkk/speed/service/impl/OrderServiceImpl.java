package com.zdkk.speed.service.impl;

import com.zdkk.speed.constant.MessageConstant;
import com.zdkk.speed.context.BaseContext;
import com.zdkk.speed.dto.OrdersSubmitDTO;
import com.zdkk.speed.entity.AddressBook;
import com.zdkk.speed.entity.OrderDetail;
import com.zdkk.speed.entity.Orders;
import com.zdkk.speed.entity.ShoppingCart;
import com.zdkk.speed.exception.AddressBookBusinessException;
import com.zdkk.speed.exception.ShoppingCartBusinessException;
import com.zdkk.speed.mapper.AddressBookMapper;
import com.zdkk.speed.mapper.OrderDetailMapper;
import com.zdkk.speed.mapper.OrderMapper;
import com.zdkk.speed.mapper.ShoppingCartMapper;
import com.zdkk.speed.service.OrderService;
import com.zdkk.speed.vo.OrderSubmitVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private AddressBookMapper addressBookMapper;

    @Transactional
    @Override
    public OrderSubmitVO submitOrder(OrdersSubmitDTO orderSubmitDTO) {
        // 异常处理
        // 收获地址为空、超出配送范围、购物车为空
        AddressBook addressBook = addressBookMapper.getById(orderSubmitDTO.getAddressBookId());
        if (addressBook == null) {
            throw new AddressBookBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        }
        Long userId = BaseContext.getCurrentId();
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUserId(userId);

        List<ShoppingCart> shoppingCartList = shoppingCartMapper.list(shoppingCart);
        if (shoppingCartList == null || shoppingCartList.isEmpty()) {
            throw new ShoppingCartBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
        }

//        BigDecimal totalAmount = BigDecimal.ZERO;
//        for (ShoppingCart shoppingCartItem : shoppingCartList) {
//            totalAmount = totalAmount.add(new BigDecimal(shoppingCartItem.getNumber()).multiply(shoppingCartItem.getAmount()));
//        }
//        if (!totalAmount.equals(orderSubmitDTO.getAmount())) {
//            throw new ShoppingCartBusinessException(MessageConstant.AMOUNT_ERROR);
//        }

        // 构造订单数据
        Orders order = new Orders();
        BeanUtils.copyProperties(orderSubmitDTO, order);
        order.setPhone(addressBook.getPhone());
        order.setAddress(addressBook.getDetail());
        order.setConsignee(addressBook.getConsignee());
        order.setNumber(String.valueOf(System.currentTimeMillis()));
        order.setUserId(userId);
        order.setStatus(Orders.PENDING_PAYMENT);
        order.setPayStatus(Orders.UN_PAID);
        order.setOrderTime(LocalDateTime.now());

        // 插入订单数据
        orderMapper.insert(order);

        // 订单明细数据
        List<OrderDetail> orderDetails = new ArrayList<>();
        for (ShoppingCart shoppingCartItem : shoppingCartList) {
            OrderDetail orderDetail = new OrderDetail();
            BeanUtils.copyProperties(shoppingCartItem, orderDetail);
            orderDetail.setOrderId(order.getId());
            orderDetails.add(orderDetail);
        }

        // 插入订单明细数据
        orderDetailMapper.insertBatch(orderDetails);

        // 清空购物车
        shoppingCartMapper.deleteByUserId(userId);

        // 封装返回结果
        return OrderSubmitVO.builder()
                .id(order.getId())
                .orderNumber(order.getNumber())
                .orderAmount(order.getAmount())
                .orderTime(order.getOrderTime())
                .build();
    }
}
