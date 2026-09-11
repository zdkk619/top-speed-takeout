package com.zdkk.speed.controller.admin;

import com.zdkk.speed.constant.JwtClaimsConstant;
import com.zdkk.speed.dto.EmployeeDTO;
import com.zdkk.speed.dto.EmployeeLoginDTO;
import com.zdkk.speed.dto.EmployeePageQueryDTO;
import com.zdkk.speed.entity.Employee;
import com.zdkk.speed.properties.JwtProperties;
import com.zdkk.speed.result.PageResult;
import com.zdkk.speed.result.Result;
import com.zdkk.speed.service.EmployeeService;
import com.zdkk.speed.utils.JwtUtil;
import com.zdkk.speed.vo.EmployeeLoginVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 员工controller
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
@Tag(name = "员工接口", description = "员工相关接口")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    @Operation(summary = "员工登录", description = "员工登录")
    @PostMapping("/login")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("【员工登录】 {}", employeeLoginDTO.getUsername());
        Employee employee = employeeService.login(employeeLoginDTO);

        // 登录成功，生成jwt令牌
        String token = JwtUtil.createJwt(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                Map.of(JwtClaimsConstant.EMP_ID, employee.getId()));

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .name(employee.getName())
                .userName(employee.getUsername())
                .token(token)
                .build();
        return Result.success(employeeLoginVO);
    }

    /**
     * 新增员工
     * @param employeeDTO
     * @return
     */
    @PostMapping
    @Operation(summary = "新增员工", description = "新增员工")
    public Result<String> save(@RequestBody EmployeeDTO employeeDTO) {
        log.info("【新增员工】{}", employeeDTO);
        employeeService.save(employeeDTO);
        return Result.success();
    }

    @Operation(summary = "员工分页查询", description = "分页查询员工")
    @GetMapping("/page")
    public Result<PageResult> page(EmployeePageQueryDTO employeePageQueryDTO) {
        log.info("【分页查询员工】 page:{}, pageSize:{}", employeePageQueryDTO.getPage(), employeePageQueryDTO.getPageSize());
        PageResult pageResult = employeeService.pageQuery(employeePageQueryDTO);
        return Result.success(pageResult);
    }
}
