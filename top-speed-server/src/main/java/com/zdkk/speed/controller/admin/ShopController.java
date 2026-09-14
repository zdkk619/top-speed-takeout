package com.zdkk.speed.controller.admin;

import com.zdkk.speed.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

@RestController("adminShopController")
@RequestMapping("/admin/shop")
@Slf4j
@Tag(name = "店铺管理", description = "店铺相关接口")

public class ShopController {
    private static final String KEY = "SHOP_STATUS";

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @PutMapping("/{status}")
    @Operation(summary = "更新店铺状态")
    public Result<String> updateShopStatus(@PathVariable Integer status) {
        log.info("【更新店铺状态】status: {}", status == 1 ? "营业中" : "打烊了");
        redisTemplate.opsForValue().set(KEY, status);
        return Result.success();
    }

    @GetMapping("/status")
    @Operation(summary = "获取店铺状态")
    public Result<Integer> getShopStatus() {
        Integer status = (Integer) redisTemplate.opsForValue().get(KEY);
        log.info("【获取店铺状态】status: {}", status == 1 ? "营业中" : "打烊了");
        return Result.success(status);
    }
}
