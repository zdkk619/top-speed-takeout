package com.zdkk.speed.service;

import com.zdkk.speed.dto.UserLoginDTO;
import com.zdkk.speed.entity.User;

public interface UserService {
    /**
     * 微信登录
     * @param userLoginDTO
     * @return
     */
    User loginWX(UserLoginDTO userLoginDTO);
}
