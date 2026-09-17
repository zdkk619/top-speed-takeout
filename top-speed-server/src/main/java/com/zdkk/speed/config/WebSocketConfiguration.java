package com.zdkk.speed.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * WebSocket 配置类
 */
@Configuration
public class WebSocketConfiguration {


    /**
     * 如果是 Spring Boot + 内嵌容器（默认方式），这个配置类必须保留。如果哪天改成打 war 包部署到外部 Tomcat，记得把它删掉。
     * @return
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}
