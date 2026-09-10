package com.zdkk.speed;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@Slf4j
public class TopSpeedApplication {
    public static void main(String[] args) {
        SpringApplication.run(TopSpeedApplication.class, args);
        log.info("Server Started");
    }
}
