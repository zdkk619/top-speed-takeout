package com.zdkk.speed.config;

import com.zdkk.speed.properties.AliOssProperties;
import com.zdkk.speed.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class OssConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public AliOssUtil aliOssUtil(AliOssProperties aliOssProperties){
        AliOssUtil aliOssUtil = AliOssUtil.builder()
                .accessKeyId(aliOssProperties.getAccessKeyId())
                .accessKeySecret(aliOssProperties.getAccessKeySecret())
                .bucketName(aliOssProperties.getBucketName())
                .region(aliOssProperties.getRegion())
                .endpoint(aliOssProperties.getEndpoint())
                .build();
        log.info("创建阿里云文件上传工具类对象成功：{}",aliOssUtil);
        return aliOssUtil;
    }
}
