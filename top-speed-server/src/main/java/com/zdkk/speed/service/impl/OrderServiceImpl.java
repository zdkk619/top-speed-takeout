package com.zdkk.speed.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zdkk.speed.constant.MessageConstant;
import com.zdkk.speed.context.BaseContext;
import com.zdkk.speed.dto.OrdersPageQueryDTO;
import com.zdkk.speed.dto.OrdersPaymentDTO;
import com.zdkk.speed.dto.OrdersSubmitDTO;
import com.zdkk.speed.entity.*;
import com.zdkk.speed.exception.AddressBookBusinessException;
import com.zdkk.speed.exception.OrderBusinessException;
import com.zdkk.speed.exception.ShoppingCartBusinessException;
import com.zdkk.speed.mapper.*;
import com.zdkk.speed.result.PageResult;
import com.zdkk.speed.service.OrderService;
import com.zdkk.speed.utils.WeChatPayUtil;
import com.zdkk.speed.vo.OrderPaymentVO;
import com.zdkk.speed.vo.OrderSubmitVO;
import com.zdkk.speed.vo.OrderVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private WeChatPayUtil weChatPayUtil;

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

    @Override
    public OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        Long userId = BaseContext.getCurrentId();
        User user = userMapper.getById(userId);

        Orders order = orderMapper.getByNumber(ordersPaymentDTO.getOrderNumber());
        if (order == null) {
            throw new OrderBusinessException(MessageConstant.ORDER_NUMBER_IS_INVALID);
        }

        // 调用微信支付接口，生成预支付交易订单
        JSONObject jsonObject = weChatPayUtil.pay(ordersPaymentDTO.getOrderNumber(),
                order.getAmount(),
                "极速外卖订单",
                user.getOpenid());

        return jsonObject.toJavaObject(OrderPaymentVO.class);
    }

    @Override
    public void paySuccess(String outTradeNo) {
        Orders order = orderMapper.getByNumber(outTradeNo);
        // 订单状态为待接单
        order.setStatus(Orders.TO_BE_CONFIRMED);
        // 支付状态为已支付
        order.setPayStatus(Orders.PAID);
        // 设置支付完成时间
        order.setCheckoutTime(LocalDateTime.now());
        orderMapper.update(order);
    }

    @Override
    public PageResult historyOrders(Integer pageNum, Integer pageSize, Integer status) {
        Long userId = BaseContext.getCurrentId();
        OrdersPageQueryDTO ordersPageQueryDTO = new OrdersPageQueryDTO();
        ordersPageQueryDTO.setUserId(userId);
        ordersPageQueryDTO.setStatus(status);

        PageHelper.startPage(pageNum, pageSize);
        Page<Orders> page = orderMapper.pageQuery(ordersPageQueryDTO);

        List<OrderVO> orderVOList = new ArrayList<>();
        if (page != null && page.getTotal() > 0) {
            page.forEach(order -> {
                List<OrderDetail> orderDetails = orderDetailMapper.getByOrderId(order.getId());
                OrderVO orderVO = new OrderVO();
                BeanUtils.copyProperties(order, orderVO);
                orderVO.setOrderDetailList(orderDetails);
                orderVOList.add(orderVO);
            });
            return new PageResult(page.getTotal(), orderVOList);
        }
        return new PageResult(0, new ArrayList<>());
    }

    @Override
    public OrderVO orderDetail(Long id) {
        Orders orders = orderMapper.getById(id);
        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(orders, orderVO);
        List<OrderDetail> orderDetails = orderDetailMapper.getByOrderId(orders.getId());
        orderVO.setOrderDetailList(orderDetails);
        return orderVO;
    }

    @Override
    public void cancel(Long id) throws Exception {
        Orders orders = orderMapper.getById(id);
        if (orders == null) {
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }
        if (orders.getStatus() > Orders.TO_BE_CONFIRMED) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }

        // 用户已付款但未接单，需退钱再取消
        if (orders.getStatus().equals(Orders.TO_BE_CONFIRMED)) {
//            weChatPayUtil.refund(orders.getNumber(), //商户订单号
//                    orders.getNumber(), //商户退款单号
//                    orders.getAmount(), //退款金额，单位 元
//                    orders.getAmount()); //原订单金额
            orders.setPayStatus(Orders.REFUND);
        }

        // 更新订单状态、取消原因、取消时间
        orders.setStatus(Orders.CANCELLED);
        orders.setCancelReason("用户取消");
        orders.setCancelTime(LocalDateTime.now());
        orderMapper.update(orders);
    }

    @Override
    public void repetition(Long id) {
        Long userId = BaseContext.getCurrentId();

        // 查询订单详情
        List<OrderDetail> byOrderId = orderDetailMapper.getByOrderId(id);

        // 构造购物车数据
        List<ShoppingCart> shoppingCartList = new ArrayList<>();
        for (OrderDetail orderDetail : byOrderId) {
            ShoppingCart shoppingCart = new ShoppingCart();
            BeanUtils.copyProperties(orderDetail, shoppingCart, "id");
            shoppingCart.setUserId(userId);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartList.add(shoppingCart);
        }
        shoppingCartMapper.insertBatch(shoppingCartList);
    }
}
