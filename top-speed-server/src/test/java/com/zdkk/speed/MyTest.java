package com.zdkk.speed;

import com.zdkk.speed.aspect.AutoFillAspect;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

@SpringBootTest
public class MyTest {

    @Test
    public void test01() {
        String password = "123456";
        password = DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));
        System.out.println(password);
    }

    @Autowired
    private AutoFillAspect autoFillAspect;

    @Test
    public void test02() {
        System.out.println(autoFillAspect);
    }
}
