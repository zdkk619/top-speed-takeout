package com.zdkk.speed.config;

import com.zdkk.speed.interceptor.AdminJwtTokenInterceptor;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@Slf4j
public class WebMvcConfiguration implements WebMvcConfigurer {

    @Autowired
    private AdminJwtTokenInterceptor adminJwtTokenInterceptor;
    /**
     * 添加拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        log.info("开始注册自定义拦截器...");
        registry.addInterceptor(adminJwtTokenInterceptor)
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/admin/employee/login");
    }

    private Info createInfo(String title) {
        return new Info()
                .title("Top-Speed-Takeout " + title + " API 文档")
                .version("1.0")
                .description("基于 SpringDoc + Knife4j-next 的 极速外卖 " + title + " 接口文档")
                .contact(new Contact()
                        .name("zdkk")
                        .email("1040893382@qq.com")
                        .url("https://github.com/zdkk619/top-speed-takeout"))
                .termsOfService("http://example.com/terms")
                .license(new License()
                        .name("Apache 2.0")
                        .url("http://springdoc.org"));
    }

    // 配置后 Knife4j 左上角下拉框会出现“管理模块”“用户模块”两个分组。
    @Bean
    public GroupedOpenApi userGroup() {
        return GroupedOpenApi.builder()
                .group("管理模块")
                .pathsToMatch("/admin/**")
                .packagesToScan("com.zdkk.speed.controller.admin")
                // 在这里单独设置文档信息
                .addOpenApiCustomizer(openApi -> openApi.info(createInfo("管理模块")))
                .build();
    }

    @Bean
    public GroupedOpenApi orderGroup() {
        return GroupedOpenApi.builder()
                .group("用户模块")
                .pathsToMatch("/user/**")
                .packagesToScan("com.zdkk.speed.controller.user")
                .addOpenApiCustomizer(openApi -> openApi.info(createInfo("用户模块")))
                .build();
    }
}
