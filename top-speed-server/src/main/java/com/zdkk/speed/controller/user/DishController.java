package com.zdkk.speed.controller.user;


import com.zdkk.speed.constant.StatusConstant;
import com.zdkk.speed.entity.Dish;
import com.zdkk.speed.result.Result;
import com.zdkk.speed.service.DishService;
import com.zdkk.speed.vo.DishVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("userDishController")
@RequestMapping("/user/dish")
@Slf4j
@Tag(name = "C端-菜品浏览接口")
public class DishController {
    @Autowired
    private DishService dishService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 根据分类id查询菜品
     *
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    @Operation(summary = "根据分类id查询菜品", description = "根据分类id查询菜品")
    public Result<List<DishVO>> list(Long categoryId) {
        // 先查缓存
        String key = "dish_" + categoryId;
        List<DishVO> voList = (List<DishVO>) redisTemplate.opsForValue().get(key);
        if (voList != null && voList.size() > 0) {
            return Result.success(voList);
        }

        Dish dish = new Dish();
        dish.setCategoryId(categoryId);
        dish.setStatus(StatusConstant.ENABLE);//查询起售中的菜品

        List<DishVO> list = dishService.listWithFlavor(dish);
        redisTemplate.opsForValue().set(key, list);

        return Result.success(list);
    }

}
