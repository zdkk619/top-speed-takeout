package com.zdkk.speed;

import com.alibaba.fastjson.JSONObject;
import com.zdkk.speed.aspect.AutoFillAspect;
import com.zdkk.speed.utils.AliOssUtil;
import io.lettuce.core.json.JsonObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.DigestUtils;

import java.io.IOException;
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


    @Autowired
    private AliOssUtil aliOssUtil;

    @Test
    public void test03() {
        aliOssUtil.upload("hello，你好".getBytes(StandardCharsets.UTF_8), "hello.txt");
    }

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Test
    public void test04() {
        System.out.println(redisTemplate);
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        valueOperations.set("test", "hello");
        System.out.println(valueOperations.get("test"));
        redisTemplate.delete("test");
        System.out.println(valueOperations.get("test"));
    }

    @Test
    public void test05() {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet request = new HttpGet("http://localhost:8080/user/shop/status");
        CloseableHttpResponse response = null;
        try {
            response = client.execute(request);

            int statusCode = response.getStatusLine().getStatusCode();
            System.out.println("Status Code: " + statusCode);

            HttpEntity entity = response.getEntity();
            String string = EntityUtils.toString(entity);
            System.out.println(string);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            try {
                client.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Test
    public void test06() {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost request = new HttpPost("http://localhost:8080/admin/employee/login");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", "admin");
        jsonObject.put("password", "111111");

        request.setEntity(new StringEntity(jsonObject.toJSONString(), ContentType.APPLICATION_JSON));
        CloseableHttpResponse response = null;
        try {
            response = client.execute(request);
            int statusCode = response.getStatusLine().getStatusCode();
            System.out.println("Status Code: " + statusCode);

            HttpEntity entity = response.getEntity();
            String string = EntityUtils.toString(entity);
            System.out.println(string);

        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            try {
                client.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
