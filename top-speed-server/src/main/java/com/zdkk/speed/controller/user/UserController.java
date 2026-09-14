package com.zdkk.speed.controller.user;

import com.zdkk.speed.constant.JwtClaimsConstant;
import com.zdkk.speed.dto.UserLoginDTO;
import com.zdkk.speed.entity.User;
import com.zdkk.speed.properties.JwtProperties;
import com.zdkk.speed.result.Result;
import com.zdkk.speed.service.UserService;
import com.zdkk.speed.utils.JwtUtil;
import com.zdkk.speed.vo.UserLoginVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequestMapping("/user/user")
@RestController
@Slf4j
@Tag(name = "客户端用户管理")
public class UserController {
    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    @Operation(summary = "用户登录", description = "用户登录")
    public Result<UserLoginVO> login(@RequestBody UserLoginDTO userLoginDTO) {
        log.info("【用户登录】code: {}", userLoginDTO.getCode());

        // 微信登录
        User user = userService.loginWX(userLoginDTO);
        // 为用户生成JWT令牌
        String token = JwtUtil.createJwt(jwtProperties.getUserSecretKey(),
                jwtProperties.getUserTtl(),
                Map.of(JwtClaimsConstant.USER_ID, user.getId()));
        return Result.success(UserLoginVO.builder()
                .id(user.getId())
                .openid(user.getOpenid())
                .token(token)
                .build());
    }

    @PostMapping("/logout")
    @Operation(summary = "用户退出登录", description = "用户退出登录")
    public Result<String> logout() {
        log.info("【用户退出登录】");
        return Result.success();
    }
}
