package com.zdkk.speed.mapper;

import com.github.pagehelper.Page;
import com.zdkk.speed.dto.GoodsSalesDTO;
import com.zdkk.speed.dto.OrdersPageQueryDTO;
import com.zdkk.speed.entity.Orders;
import com.zdkk.speed.vo.OrderOverViewVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface OrderMapper {
    /**
     * 插入订单数据
     * @param order
     */
    void insert(Orders order);

    /**
     * 根据订单号查询订单
     * @param orderNumber
     * @return
     */
    @Select("SELECT * FROM orders WHERE number = #{orderNumber}")
    Orders getByNumber(String orderNumber);

    /**
     * 更新订单数据
     * @param order
     */
    void update(Orders order);

    /**
     * 分页查询订单
     * @param ordersPageQueryDTO
     * @return
     */
    Page<Orders> pageQuery(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 根据id查询订单
     * @param id
     * @return
     */
    @Select("SELECT * FROM orders WHERE id = #{id}")
    Orders getById(Long id);

    /**
     * 根据状态查询订单数量
     * @param status
     * @return
     */
    @Select("SELECT COUNT(*) FROM orders WHERE status = #{status}")
    Integer countByStatus(Integer status);

    /**
     * 根据订单状态和下单时间查询订单
     * @param status
     * @param orderTime
     */
    @Select("SELECT * FROM orders WHERE status = #{status} AND order_time < #{orderTime}")
    List<Orders> getByStatusAndOrderTimeLT(Integer status, LocalDateTime orderTime);

    /**
     * 根据订单状态和时间范围查询订单金额总和
     * @param map
     * @return
     */
    Double sumByMap(Map<String, Object> map);

    /**
     * 根据条件查询订单数量
     * @param map
     * @return
     */
    Integer countByMap(Map<String, Object> map);

    /**
     * 根据时间范围查询销售前十的商品
     * @param startTime
     * @param endTime
     * @return
     */
    List<GoodsSalesDTO> getSalesTop10(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 根据时间范围查询订单概览
     * @param begin
     * @param end
     * @return
     */
    OrderOverViewVO getOverviewOrders(LocalDateTime begin, LocalDateTime end);
}
