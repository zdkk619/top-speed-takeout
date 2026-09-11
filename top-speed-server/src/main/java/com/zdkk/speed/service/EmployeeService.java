package com.zdkk.speed.service;

import com.zdkk.speed.dto.EmployeeDTO;
import com.zdkk.speed.dto.EmployeeLoginDTO;
import com.zdkk.speed.entity.Employee;

public interface EmployeeService {
    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    /**
     * 新增员工
     * @param employeeDTO
     */
    void save(EmployeeDTO employeeDTO);
}
