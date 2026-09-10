package com.zdkk.speed.controller.admin;

import com.zdkk.speed.constant.JwtClaimsConstant;
import com.zdkk.speed.dto.EmployeeLoginDTO;
import com.zdkk.speed.entity.Employee;
import com.zdkk.speed.properties.JwtProperties;
import com.zdkk.speed.result.Result;
import com.zdkk.speed.service.EmployeeService;
import com.zdkk.speed.utils.JwtUtil;
import com.zdkk.speed.vo.EmployeeLoginVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
