package com.zdkk.speed.controller.admin;

import com.zdkk.speed.dto.SetMealDTO;
import com.zdkk.speed.dto.SetMealPageQueryDTO;
import com.zdkk.speed.entity.SetMeal;
import com.zdkk.speed.entity.SetmealDish;
import com.zdkk.speed.mapper.SetMealDishMapper;
import com.zdkk.speed.result.PageResult;
import com.zdkk.speed.result.Result;
import com.zdkk.speed.service.SetMealService;
import com.zdkk.speed.vo.SetMealVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/setmeal")
@Slf4j
@Tag(name = "套餐管理", description = "套餐管理")
public class SetMealController {

    @Autowired
    private SetMealService setMealService;

    @GetMapping("/page")
    @Operation(summary = "套餐分页查询", description = "套餐分页查询")
    public Result<PageResult> pageQuery(SetMealPageQueryDTO setMealPageQueryDTO) {
        log.info("【套餐分页查询】page:{}, pageSize:{}", setMealPageQueryDTO.getPage(), setMealPageQueryDTO.getPageSize());
        PageResult pageResult = setMealService.pageQuery(setMealPageQueryDTO);
        return Result.success(pageResult);
    }

    @PostMapping
    @Operation(summary = "新增套餐", description = "新增套餐")
    public Result save(@RequestBody SetMealDTO setMealDTO) {
        log.info("【新增套餐】setMealDTO:{}", setMealDTO);
        setMealService.save(setMealDTO);
        return Result.success();
    }

    @GetMapping("/{id}")
    @Operation(summary = "根据id查询套餐", description = "根据id查询套餐")
    public Result<SetMealVO> getById(@PathVariable Long id) {
        log.info("【根据id查询套餐】id:{}", id);
        SetMealVO setMealVO = setMealService.getById(id);
        return Result.success(setMealVO);
    }

    @PutMapping
    @Operation(summary = "修改套餐", description = "修改套餐")
    public Result<String> update(@RequestBody SetMealDTO setMealDTO) {
        log.info("【修改套餐】setMealDTO:{}", setMealDTO);
        setMealService.update(setMealDTO);
        return Result.success();
    }

    @PostMapping("/status/{status}")
    @Operation(summary = "修改套餐状态", description = "修改套餐状态")
    public Result<String> enableOrDisable(@PathVariable Integer status, Long id) {
        log.info("【修改套餐状态】status:{}, id:{}", status, id);
        setMealService.enableOrDisable(status, id);
        return Result.success();
    }

    @DeleteMapping
    @Operation(summary = "删除套餐", description = "删除套餐，删除套餐同时删除套餐中的菜品，可以删除一个或多个，用逗号分隔")
    public Result<String> delete(@RequestParam List<Long> ids) {
        log.info("【删除套餐】ids:{}", ids);
        setMealService.delete(ids);
        return Result.success();
    }
}
