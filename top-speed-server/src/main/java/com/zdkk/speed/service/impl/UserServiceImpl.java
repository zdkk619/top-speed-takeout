package com.zdkk.speed.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.zdkk.speed.constant.MessageConstant;
import com.zdkk.speed.constant.WeChatConstant;
import com.zdkk.speed.dto.UserLoginDTO;
import com.zdkk.speed.entity.User;
import com.zdkk.speed.exception.LoginFailedException;
import com.zdkk.speed.mapper.UserMapper;
import com.zdkk.speed.properties.WeChatProperties;
import com.zdkk.speed.service.UserService;
import com.zdkk.speed.utils.HttpClientUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private WeChatProperties weChatProperties;

    @Override
    public User loginWX(UserLoginDTO userLoginDTO) {
        String openId = getOpenId(userLoginDTO.getCode());
        if (openId == null) {
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }

        User user = userMapper.getByOpenId(openId);

        // 新用户自动注册
        if (user == null) {
            user = User.builder()
                    .openid(openId)
                    .createTime(LocalDateTime.now())
                    .build();
            userMapper.insert(user);
        }
        return user;
    }


    /**
     * 调用微信接口服务，获取微信用户的openid
     * @param code
     * @return
     */
    private String getOpenId(String code) {
        Map<String, String> params = new HashMap<>(4);
        params.put(WeChatConstant.APPID, weChatProperties.getAppid());
        params.put(WeChatConstant.SECRET, weChatProperties.getSecret());
        params.put(WeChatConstant.JS_CODE, code);
        params.put(WeChatConstant.GRANT_TYPE, weChatProperties.getGrantType());
        String json = HttpClientUtil.doGet(WeChatConstant.WX_LOGIN, params);
        JSONObject jsonObject = JSON.parseObject(json);
        return jsonObject.getString(WeChatConstant.OPENID);
    }
}
