package com.zdkk.speed.service;

import com.zdkk.speed.vo.OrderReportVO;
import com.zdkk.speed.vo.SalesTop10ReportVO;
import com.zdkk.speed.vo.TurnoverReportVO;
import com.zdkk.speed.vo.UserReportVO;
import jakarta.servlet.http.HttpServletResponse;

import java.time.LocalDate;

/**
 * 报表服务
 */
public interface ReportService {
    /**
     * 获取 指定时间段内的 营业额
     * @param startDate
     * @param endDate
     * @return
     */
    TurnoverReportVO getTurnover(LocalDate startDate, LocalDate endDate);

    /**
     * 获取 指定时间的用户数和新增用户数
     * @param begin
     * @param end
     * @return
     */
    UserReportVO getUserReport(LocalDate begin, LocalDate end);

    /**
     * 获取 指定时间的订单数统计
     * @param begin
     * @param end
     * @return
     */
    OrderReportVO getOrderReport(LocalDate begin, LocalDate end);

    /**
     * 获取 指定时间的菜品销量的TOP10
     * @param begin
     * @param end
     * @return
     */
    SalesTop10ReportVO getSalesTop10(LocalDate begin, LocalDate end);

    /**
     * 导出一个月内的业务数据
     * @param response
     */
    void exportBusinessData(HttpServletResponse response);
}
