package com.zdkk.speed.config;

import com.zdkk.speed.interceptor.AdminJwtTokenInterceptor;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import lombok.extern.slf4j.Slf4j;
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

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        // 1. 设置标题
                        .title("Top-Speed-Takeout 项目 API 文档")
                        // 2. 设置版本
                        .version("1.0")
                        // 3. 设置描述/简介
                        .description("基于 SpringDoc + Knife4j 的 极速外卖 接口文档")
                        // 4. 设置作者信息 (对应截图中的 作者)
                        .contact(new Contact()
                                .name("zdkk")
                                .email("1040893382@qq.com")
                                .url("https://github.com/your-repo"))
                        // 5. 设置服务条款
                        .termsOfService("http://example.com/terms")
                        // 6. 设置许可证 (可选，通常也会显示在文档底部或侧边栏)
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://springdoc.org")));
    }
}
