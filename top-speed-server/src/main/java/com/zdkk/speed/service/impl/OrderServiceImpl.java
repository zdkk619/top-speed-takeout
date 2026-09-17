package com.zdkk.speed.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zdkk.speed.constant.MessageConstant;
import com.zdkk.speed.context.BaseContext;
import com.zdkk.speed.dto.*;
import com.zdkk.speed.entity.*;
import com.zdkk.speed.exception.AddressBookBusinessException;
import com.zdkk.speed.exception.OrderBusinessException;
import com.zdkk.speed.exception.ShoppingCartBusinessException;
import com.zdkk.speed.mapper.*;
import com.zdkk.speed.result.PageResult;
import com.zdkk.speed.service.OrderService;
import com.zdkk.speed.utils.HttpClientUtil;
import com.zdkk.speed.utils.WeChatPayUtil;
import com.zdkk.speed.vo.OrderPaymentVO;
import com.zdkk.speed.vo.OrderStatisticsVO;
import com.zdkk.speed.vo.OrderSubmitVO;
import com.zdkk.speed.vo.OrderVO;
import com.zdkk.speed.websocket.WebSocketServer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {
    @Value("${top-speed.shop.address}")
    private String shopAddress;
    @Value("${top-speed.baidu.ak}")
    private String baiduAk;

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

    @Autowired
    private WebSocketServer webSocketServer;

    @Transactional
    @Override
    public OrderSubmitVO submitOrder(OrdersSubmitDTO orderSubmitDTO) {
        // 异常处理
        // 收获地址为空、超出配送范围、购物车为空
        AddressBook addressBook = addressBookMapper.getById(orderSubmitDTO.getAddressBookId());
        if (addressBook == null) {
            throw new AddressBookBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        }

        // 检查配送范围
        checkOutOfRange(addressBook.getCityName() + addressBook.getDistrictName() + addressBook.getDetail());

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
        if (!Objects.equals(order.getStatus(), Orders.PENDING_PAYMENT)) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
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
        if (order == null) {
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }
        if (!Objects.equals(order.getStatus(), Orders.PENDING_PAYMENT)) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }
        log.info("订单状态：{}", order.getStatus());
        // 订单状态为待接单
        order.setStatus(Orders.TO_BE_CONFIRMED);
        // 支付状态为已支付
        order.setPayStatus(Orders.PAID);
        // 设置支付完成时间
        order.setCheckoutTime(LocalDateTime.now());
        orderMapper.update(order);

        // 通过WebSocket发送订单状态给商家
        HashMap<String, Object> map = new HashMap<>();
        map.put("type", 1);
        map.put("orderId", order.getId());
        map.put("content", "订单号: " + outTradeNo);
        webSocketServer.sendToAllClient(JSON.toJSONString(map));
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
    public void userCancel(Long id) throws Exception {
        Orders orders = orderMapper.getById(id);
        if (orders == null) {
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }
        if (orders.getStatus() > Orders.TO_BE_CONFIRMED) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }

        // 用户已付款但未接单，需退钱再取消
        if (orders.getPayStatus().equals(Orders.PAID)) {
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

    @Override
    public PageResult conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO) {
        PageHelper.startPage(ordersPageQueryDTO.getPage(), ordersPageQueryDTO.getPageSize());
        Page<Orders> page = orderMapper.pageQuery(ordersPageQueryDTO);
        // 部分订单状态，需要额外返回订单菜品信息，将Orders转化为OrderVO
        List<OrderVO> orderVOList = getOrderVOList(page);

        return new PageResult(page.getTotal(), orderVOList);
    }

    private List<OrderVO> getOrderVOList(Page<Orders> page) {
        // 需要返回订单菜品信息，自定义OrderVO响应结果
        List<OrderVO> orderVOList = new ArrayList<>();

        List<Orders> ordersList = page.getResult();
        if (!CollectionUtils.isEmpty(ordersList)) {
            for (Orders orders : ordersList) {
                // 将共同字段复制到OrderVO
                OrderVO orderVO = new OrderVO();

                BeanUtils.copyProperties(orders, orderVO);
                // 将订单菜品信息封装到orderVO中，并添加到orderVOList
                orderVO.setOrderDishes(getOrderDishesStr(orders));

                orderVOList.add(orderVO);
            }
        }
        return orderVOList;
    }

    /**
     * 根据订单id获取菜品信息字符串
     *
     * @param orders
     * @return
     */
    private String getOrderDishesStr(Orders orders) {
        // 查询订单菜品详情信息（订单中的菜品和数量）
        List<OrderDetail> orderDetailList = orderDetailMapper.getByOrderId(orders.getId());

        // 将所有订单菜品信息拼接为字符串（格式：宫保鸡丁*3；）
        List<String> orderDishList = orderDetailList.stream().map(x -> x.getName() + "*" + x.getNumber() + ";").collect(Collectors.toList());

        // 将该订单对应的所有菜品信息拼接在一起
        return String.join("", orderDishList);
    }

    @Override
    public OrderStatisticsVO getOrderStatistics() {
        OrderStatisticsVO orderStatisticsVO = new OrderStatisticsVO();
        orderStatisticsVO.setToBeConfirmed(orderMapper.countByStatus(Orders.TO_BE_CONFIRMED));
        orderStatisticsVO.setConfirmed(orderMapper.countByStatus(Orders.CONFIRMED));
        orderStatisticsVO.setDeliveryInProgress(orderMapper.countByStatus(Orders.DELIVERY_IN_PROGRESS));
        return orderStatisticsVO;
    }

    @Override
    public void confirm(OrdersConfirmDTO ordersConfirmDTO) {
        Orders orders = orderMapper.getById(ordersConfirmDTO.getId());
        if (orders == null || !orders.getStatus().equals(Orders.TO_BE_CONFIRMED)) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }
        orders.setStatus(Orders.CONFIRMED);
        orderMapper.update(orders);
    }

    @Override
    public void rejection(OrdersRejectionDTO ordersRejectionDTO) throws Exception {
        Orders orders = orderMapper.getById(ordersRejectionDTO.getId());
        if (orders == null || !Objects.equals(orders.getStatus(), Orders.TO_BE_CONFIRMED)) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }

        if (orders.getPayStatus().equals(Orders.PAID)) {
//            weChatPayUtil.refund(orders.getNumber(), //商户订单号
//                    orders.getNumber(), //商户退款单号
//                    orders.getAmount(), //退款金额，单位 元
//                    orders.getAmount()); //原订单金额
            orders.setPayStatus(Orders.REFUND);
            log.info("订单 {} 退款成功", orders.getId());
        }

        Orders build = Orders.builder()
                .id(ordersRejectionDTO.getId())
                .status(Orders.CANCELLED)
                .cancelTime(LocalDateTime.now())
                .rejectionReason(ordersRejectionDTO.getRejectionReason())
                .build();
        orderMapper.update(build);
    }

    @Override
    public void cancel(OrdersCancelDTO ordersCancelDTO) {
        Orders orders = orderMapper.getById(ordersCancelDTO.getId());
        if (orders == null) {
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }

        // 用户已付款，需退钱再取消
        if (orders.getPayStatus().equals(Orders.PAID)) {
//            weChatPayUtil.refund(orders.getNumber(), //商户订单号
//                    orders.getNumber(), //商户退款单号
//                    orders.getAmount(), //退款金额，单位 元
//                    orders.getAmount()); //原订单金额
            orders.setPayStatus(Orders.REFUND);
        }

        // 更新订单状态、取消原因、取消时间
        orders.setStatus(Orders.CANCELLED);
        orders.setCancelReason(ordersCancelDTO.getCancelReason());
        orders.setCancelTime(LocalDateTime.now());
        orderMapper.update(orders);
    }

    @Override
    public void delivery(Long id) {
        Orders orders = orderMapper.getById(id);
        if (orders == null || !orders.getStatus().equals(Orders.CONFIRMED))
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);

        orders.setStatus(Orders.DELIVERY_IN_PROGRESS);
        orderMapper.update(orders);
    }

    @Override
    public void complete(Long id) {
        Orders orders = orderMapper.getById(id);
        if (orders == null || !orders.getStatus().equals(Orders.DELIVERY_IN_PROGRESS)) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }
        orders.setStatus(Orders.COMPLETED);
        orders.setDeliveryTime(LocalDateTime.now());
        orderMapper.update(orders);
    }

    @Override
    public void reminder(Long id) {
        Orders orders = orderMapper.getById(id);
        if (orders == null || !orders.getStatus().equals(Orders.TO_BE_CONFIRMED)) {
            throw new OrderBusinessException(MessageConstant.ORDER_STATUS_ERROR);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("type", 2);
        map.put("id", orders.getId());
        map.put("content", "订单号: " + orders.getNumber());
        webSocketServer.sendToAllClient(JSON.toJSONString(map));
    }

    /**
     * 检查客户的收货地址是否超出配送范围
     * @param address
     */
    private void checkOutOfRange(String address) {
        Map<String, String> map = new HashMap();
        map.put("address",shopAddress);
        map.put("output","json");
        map.put("ak",baiduAk);

        //获取店铺的经纬度坐标
        String shopCoordinate = HttpClientUtil.doGet("https://api.map.baidu.com/geocoding/v3", map);

        JSONObject jsonObject = JSON.parseObject(shopCoordinate);
        if(!jsonObject.getString("status").equals("0")){
            log.error("百度返回：{}", shopCoordinate);
            throw new OrderBusinessException("店铺地址解析失败");
        }

        //数据解析
        JSONObject location = jsonObject.getJSONObject("result").getJSONObject("location");
        String lat = location.getString("lat");
        String lng = location.getString("lng");
        //店铺经纬度坐标
        String shopLngLat = lat + "," + lng;

        map.put("address",address);
        //获取用户收货地址的经纬度坐标
        String userCoordinate = HttpClientUtil.doGet("https://api.map.baidu.com/geocoding/v3", map);

        jsonObject = JSON.parseObject(userCoordinate);
        if(!jsonObject.getString("status").equals("0")){
            log.error("百度返回：{}", userCoordinate);
            throw new OrderBusinessException("收货地址解析失败");
        }

        //数据解析
        location = jsonObject.getJSONObject("result").getJSONObject("location");
        lat = location.getString("lat");
        lng = location.getString("lng");
        //用户收货地址经纬度坐标
        String userLngLat = lat + "," + lng;

        map.put("origin",shopLngLat);
        map.put("destination",userLngLat);

        //路线规划
        String json = HttpClientUtil.doGet("https://api.map.baidu.com/direction/v2/riding", map);

        jsonObject = JSON.parseObject(json);
        if(!jsonObject.getString("status").equals("0")){
            log.error("百度返回：{}", json);
            throw new OrderBusinessException("配送路线规划失败");
        }

        //数据解析
        JSONObject result = jsonObject.getJSONObject("result");
        JSONArray jsonArray = (JSONArray) result.get("routes");
        Integer distance = (Integer) ((JSONObject) jsonArray.getFirst()).get("distance");

        if(distance > 5000){
            //配送距离超过5000米
            throw new OrderBusinessException("超出配送范围");
        }
    }
}
