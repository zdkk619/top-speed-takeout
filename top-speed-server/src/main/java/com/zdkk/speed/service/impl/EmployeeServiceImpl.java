package com.zdkk.speed.service.impl;

import com.zdkk.speed.constant.MessageConstant;
import com.zdkk.speed.constant.StatusConstant;
import com.zdkk.speed.dto.EmployeeLoginDTO;
import com.zdkk.speed.entity.Employee;
import com.zdkk.speed.exception.AccountLockedException;
import com.zdkk.speed.exception.AccountNotFoundException;
import com.zdkk.speed.exception.PasswordErrorException;
import com.zdkk.speed.mapper.EmployeeMapper;
import com.zdkk.speed.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    private EmployeeMapper employeeMapper;

    @Override
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();
        // 1. 根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);

        // 2. 处理各种异常（用户名不存在，密码错误，账号锁定）
        if (employee == null) {
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }
        password = DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));
        if (!password.equals(employee.getPassword())) {
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (StatusConstant.DISABLE.equals(employee.getStatus())) {
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        // 3. 返回实体对象
        return employee;
    }
}
