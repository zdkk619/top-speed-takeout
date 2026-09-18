package com.zdkk.speed.service.impl;

import com.zdkk.speed.dto.GoodsSalesDTO;
import com.zdkk.speed.entity.Orders;
import com.zdkk.speed.mapper.OrderMapper;
import com.zdkk.speed.mapper.UserMapper;
import com.zdkk.speed.service.ReportService;
import com.zdkk.speed.service.WorkSpaceService;
import com.zdkk.speed.vo.*;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements ReportService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private WorkSpaceService workSpaceService;
    @Override
    public TurnoverReportVO getTurnover(LocalDate startDate, LocalDate endDate) {
        List<LocalDate> dateArrayList = getDateList(startDate, endDate);

        ArrayList<Double> turnoverList = new ArrayList<>();
        for (LocalDate date : dateArrayList) {
            LocalDateTime start = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime end = LocalDateTime.of(date, LocalTime.MAX);
            Map<String, Object> map = new HashMap<>();
            map.put("status", Orders.COMPLETED);
            map.put("begin", start);
            map.put("end", end);
            Double turnover = orderMapper.sumByMap(map);
            turnoverList.add(turnover == null ? 0 : turnover);
        }
        return TurnoverReportVO.builder()
                .dateList(dateArrayList.stream().map(Object::toString).collect(Collectors.joining(",")))
                .turnoverList(turnoverList.stream().map(String::valueOf).collect(Collectors.joining(",")))
                .build();
    }

    @Override
    public UserReportVO getUserReport(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList = getDateList(begin, end);

        List<Integer> newUserList = new ArrayList<>();
        List<Integer> totalUserList = new ArrayList<>();
        for (LocalDate date : dateList) {
            LocalDateTime startTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);

            Map<String, Object> map = new HashMap<>();
            map.put("end", endTime);
            Integer totalUser = userMapper.countByMap(map);
            map.put("begin", startTime);
            Integer newUser = userMapper.countByMap(map);

            totalUserList.add(totalUser);
            newUserList.add(newUser);
        }
        return UserReportVO.builder()
                .dateList(dateList.stream().map(Object::toString).collect(Collectors.joining(",")))
                .totalUserList(totalUserList.stream().map(String::valueOf).collect(Collectors.joining(",")))
                .newUserList(newUserList.stream().map(String::valueOf).collect(Collectors.joining(",")))
                .build();
    }

    @Override
    public OrderReportVO getOrderReport(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList = getDateList(begin, end);
        List<Integer> orderCountList = new ArrayList<>();
        List<Integer> completedOrderCountList = new ArrayList<>();
        Integer allOrderCount = 0;
        Integer allCompletedOrderCount = 0;
        for (LocalDate localDate : dateList) {
            LocalDateTime startTime = LocalDateTime.of(localDate, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(localDate, LocalTime.MAX);
            Map<String, Object> map = new HashMap<>();
            map.put("begin", startTime);
            map.put("end", endTime);
            Integer orderCount = orderMapper.countByMap(map);
            orderCountList.add(orderCount);
            allOrderCount += orderCount;

            map.put("status", Orders.COMPLETED);
            Integer completedOrderCount = orderMapper.countByMap(map);
            completedOrderCountList.add(completedOrderCount);
            allCompletedOrderCount += completedOrderCount;
        }
        double orderCompletionRate = allOrderCount != 0 ? allCompletedOrderCount.doubleValue() / allOrderCount : 0;
        return OrderReportVO.builder()
                .dateList(dateList.stream().map(Object::toString).collect(Collectors.joining(",")))
                .orderCountList(orderCountList.stream().map(String::valueOf).collect(Collectors.joining(",")))
                .validOrderCountList(completedOrderCountList.stream().map(String::valueOf).collect(Collectors.joining(",")))
                .totalOrderCount(allOrderCount)
                .validOrderCount(allCompletedOrderCount)
                .orderCompletionRate(orderCompletionRate)
                .build();
    }

    @Override
    public SalesTop10ReportVO getSalesTop10(LocalDate begin, LocalDate end) {
        LocalDateTime startTime = LocalDateTime.of(begin, LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(end, LocalTime.MAX);
        List<GoodsSalesDTO> goodsSalesDTOList = orderMapper.getSalesTop10(startTime, endTime);
        return SalesTop10ReportVO.builder()
                .nameList(goodsSalesDTOList.stream().map(GoodsSalesDTO::getName).collect(Collectors.joining(",")))
                .numberList(goodsSalesDTOList.stream().map(GoodsSalesDTO::getNumber).map(String::valueOf).collect(Collectors.joining(",")))
                .build();
    }

    @Override
    public void exportBusinessData(HttpServletResponse response) {
        LocalDate begin = LocalDate.now().minusDays(30);
        LocalDate end = LocalDate.now().minusDays(1);

        // 查询概览数据
        BusinessDataVO businessData = workSpaceService.getBusinessData(LocalDateTime.of(begin, LocalTime.MIN), LocalDateTime.of(end, LocalTime.MAX));
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("template/business_data_template.xlsx");

        ServletOutputStream outputStream = null;
        XSSFWorkbook excel = null;
        try {
            excel = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = excel.getSheetAt(0);
            sheet.getRow(1).getCell(1).setCellValue(begin + "-" + end);
            //获得第4行
            XSSFRow row = sheet.getRow(3);
            //获取单元格
            row.getCell(2).setCellValue(businessData.getTurnover());
            row.getCell(4).setCellValue(businessData.getOrderCompletionRate());
            row.getCell(6).setCellValue(businessData.getNewUsers());
            row = sheet.getRow(4);
            row.getCell(2).setCellValue(businessData.getValidOrderCount());
            row.getCell(4).setCellValue(businessData.getUnitPrice());

            for (int i = 0; i < 30; i++) {
                LocalDate date = begin.plusDays(i);
                businessData = workSpaceService.getBusinessData(LocalDateTime.of(date, LocalTime.MIN), LocalDateTime.of(date, LocalTime.MAX));
                row = sheet.getRow(7 + i);
                row.getCell(1).setCellValue(date.toString());
                row.getCell(2).setCellValue(businessData.getTurnover());
                row.getCell(3).setCellValue(businessData.getValidOrderCount());
                row.getCell(4).setCellValue(businessData.getOrderCompletionRate());
                row.getCell(5).setCellValue(businessData.getUnitPrice());
                row.getCell(6).setCellValue(businessData.getNewUsers());
            }

            // 输出文件到客户端浏览器
            outputStream = response.getOutputStream();
            excel.write(outputStream);
            outputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if (excel != null) {
                try {
                    excel.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private List<LocalDate> getDateList(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList = new ArrayList<>();
        while (begin.isBefore(end)) {
            dateList.add(begin);
            begin = begin.plusDays(1);
        }
        dateList.add(end);
        return dateList;
    }
}
