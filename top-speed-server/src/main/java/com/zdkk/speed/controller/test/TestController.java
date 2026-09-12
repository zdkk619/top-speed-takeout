package com.zdkk.speed.controller.test;

import com.zdkk.speed.result.Result;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@Hidden
public class TestController {
    @GetMapping("/test/time")
    public Result<Map<String, Object>> testTime() {
        Map<String, Object> map = new HashMap<>();
        map.put("localDateTime", LocalDateTime.now());
        map.put("localDate", LocalDate.now());
        map.put("localTime", LocalTime.now());
        return Result.success(map);
    }
}
