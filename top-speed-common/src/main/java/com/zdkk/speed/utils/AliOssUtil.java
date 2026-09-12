package com.zdkk.speed.utils;

import com.aliyun.sdk.service.oss2.OSSClient;
import com.aliyun.sdk.service.oss2.OSSClientBuilder;
import com.aliyun.sdk.service.oss2.credentials.StaticCredentialsProvider;
import com.aliyun.sdk.service.oss2.models.PutObjectRequest;
import com.aliyun.sdk.service.oss2.transport.BinaryData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Slf4j
public class AliOssUtil {
    private String endpoint;
    private String region;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;

    public String upload(byte[] bytes, String objectName) {
        OSSClientBuilder builder = OSSClient.newBuilder().
                endpoint(endpoint).
                region(region).
                credentialsProvider(new StaticCredentialsProvider(accessKeyId, accessKeySecret));
        try (OSSClient ossClient = builder.build()) {
            // 创建PutObject请求。
            ossClient.putObject(PutObjectRequest.newBuilder()
                    .bucket(bucketName)
                    .key(objectName)
                    .body(BinaryData.fromBytes(bytes))
                    .build());
        } catch (Exception e) {
            log.error("【文件上传】:{}", e.getMessage());
        }

        //文件访问路径规则 https://BucketName.Endpoint/ObjectName
        StringBuilder stringBuilder = new StringBuilder("https://");
        stringBuilder
                .append(bucketName)
                .append(".")
                .append(endpoint)
                .append("/")
                .append(objectName);

        log.info("【文件上传】:{}", stringBuilder.toString());

        return stringBuilder.toString();
    }
}
