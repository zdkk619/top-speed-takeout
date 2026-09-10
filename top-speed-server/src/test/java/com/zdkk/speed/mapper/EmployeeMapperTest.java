package com.zdkk.speed.mapper;

import com.zdkk.speed.entity.Employee;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class EmployeeMapperTest {

    @Autowired
    private EmployeeMapper employeeMapper;

    @Test
    void getByUsername() {
        Employee admin = employeeMapper.getByUsername("admin");
        Assertions.assertNotNull(admin);
        Assertions.assertEquals("管理员", admin.getName());
    }
}