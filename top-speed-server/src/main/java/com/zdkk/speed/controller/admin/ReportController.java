package com.zdkk.speed.controller.admin;

import com.zdkk.speed.result.Result;
import com.zdkk.speed.service.ReportService;
import com.zdkk.speed.vo.OrderReportVO;
import com.zdkk.speed.vo.SalesTop10ReportVO;
import com.zdkk.speed.vo.TurnoverReportVO;
import com.zdkk.speed.vo.UserReportVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/admin/report")
@Slf4j
@Tag(name = "报表管理", description = "报表管理相关接口")
public class ReportController {
    @Autowired
    private ReportService reportService;


    @GetMapping("/turnoverStatistics")
    @Operation(summary = "营业额统计", description = "查询指定日期范围内的营业额统计")
    public Result<TurnoverReportVO> turnoverStatistics(LocalDate begin, LocalDate end) {
        log.info("查询营业额 begin:{}, end:{}", begin, end);
        TurnoverReportVO turnover = reportService.getTurnover(begin, end);
        return Result.success(turnover);
    }

    @GetMapping("/userStatistics")
    @Operation(summary = "用户统计", description = "查询指定日期范围内的用户数量")
    public Result<UserReportVO> userStatistics(LocalDate begin, LocalDate end) {
        log.info("查询用户统计 begin:{}, end:{}", begin, end);
        UserReportVO userReport = reportService.getUserReport(begin, end);
        return Result.success(userReport);
    }

    @GetMapping("/ordersStatistics")
    @Operation(summary = "订单统计", description = "查询指定日期范围内的订单统计")
    public Result<OrderReportVO> ordersStatistics(LocalDate begin, LocalDate end) {
        log.info("查询订单统计 begin:{}, end:{}", begin, end);
        OrderReportVO orderReport = reportService.getOrderReport(begin, end);
        return Result.success(orderReport);
    }

    @GetMapping("/top10")
    @Operation(summary = "销售TOP10", description = "查询指定日期范围内的销售TOP10")
    public Result<SalesTop10ReportVO> top10(LocalDate begin, LocalDate end) {
        log.info("查询销售TOP10 begin:{}, end:{}", begin, end);
        SalesTop10ReportVO salesTop10 = reportService.getSalesTop10(begin, end);
        return Result.success(salesTop10);
    }
}
