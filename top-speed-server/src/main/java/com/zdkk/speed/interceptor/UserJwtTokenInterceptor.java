package com.zdkk.speed.interceptor;

import com.zdkk.speed.constant.JwtClaimsConstant;
import com.zdkk.speed.context.BaseContext;
import com.zdkk.speed.properties.JwtProperties;
import com.zdkk.speed.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@Slf4j
public class UserJwtTokenInterceptor implements HandlerInterceptor {
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 校验jwt
     * @param request current HTTP request
     * @param response current HTTP response
     * @param handler chosen handler to execute, for type and/or instance evaluation
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        String token = request.getHeader(jwtProperties.getUserTokenName());

        try {
            log.info("【jwt校验】用户：{}", token);
            Claims claims = JwtUtil.parseToken(jwtProperties.getUserSecretKey(), token);
            if (JwtUtil.isExpired(jwtProperties.getUserSecretKey(), token)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return false;
            }
            long id = Long.parseLong(claims.get(JwtClaimsConstant.USER_ID).toString());
            log.info("【jwt校验】用户id: {}", id);
            BaseContext.setCurrentId(id);
            return true;
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
    }
}
