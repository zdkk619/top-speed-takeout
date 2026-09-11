package com.zdkk.speed.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zdkk.speed.constant.MessageConstant;
import com.zdkk.speed.constant.PasswordConstant;
import com.zdkk.speed.constant.StatusConstant;
import com.zdkk.speed.context.BaseContext;
import com.zdkk.speed.dto.EmployeeDTO;
import com.zdkk.speed.dto.EmployeeLoginDTO;
import com.zdkk.speed.dto.EmployeePageQueryDTO;
import com.zdkk.speed.entity.Employee;
import com.zdkk.speed.exception.AccountAlreadyExistException;
import com.zdkk.speed.exception.AccountLockedException;
import com.zdkk.speed.exception.AccountNotFoundException;
import com.zdkk.speed.exception.PasswordErrorException;
import com.zdkk.speed.mapper.EmployeeMapper;
import com.zdkk.speed.result.PageResult;
import com.zdkk.speed.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Service
@Slf4j
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

    @Override
    public void save(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        // 提前检查账号是否已存在
        if (employeeMapper.getByUsername(employeeDTO.getUsername()) != null) {
            throw new AccountAlreadyExistException(MessageConstant.ACCOUNT_ALREADY_EXISTS);
        }

        // 属性拷贝
        BeanUtils.copyProperties(employeeDTO, employee);

        // 设置账号状态为启用
        employee.setStatus(StatusConstant.ENABLE);
        // 设置密码为默认密码
        String password = DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes(StandardCharsets.UTF_8));
        employee.setPassword(password);

        // 设置创建时间和最后修改时间
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());

        // 设置创建人和最后修改人
        Long currentId = BaseContext.getCurrentId();
        employee.setCreateUser(currentId);
        employee.setUpdateUser(currentId);
        employeeMapper.insert(employee);
    }

    @Override
    public PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO) {
        // 开始分页查询
        PageHelper.startPage(employeePageQueryDTO.getPage(), employeePageQueryDTO.getPageSize());
        Page<Employee> page = employeeMapper.pageQuery(employeePageQueryDTO);
        log.info("page: {}", page);
        long total = page.getTotal();
        page.getResult().forEach(e -> e.setPassword(null)); // Set password to null for security
        return new PageResult(total, page.getResult());
    }

    @Override
    public void enableOrDisable(Integer status, Long id) {
        Employee employee = Employee.builder()
                .status(status)
                .id(id)
                .updateTime(LocalDateTime.now())
                .updateUser(BaseContext.getCurrentId())
                .build();
        employeeMapper.update(employee);
    }
}
