package com.zdkk.speed.service;

import com.zdkk.speed.dto.EmployeeLoginDTO;
import com.zdkk.speed.entity.Employee;

public interface EmployeeService {
    /**
     * 员工登录
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);
}
