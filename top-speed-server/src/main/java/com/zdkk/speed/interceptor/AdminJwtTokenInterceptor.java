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
public class AdminJwtTokenInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtProperties jwtProperties;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        String token = request.getHeader(jwtProperties.getAdminTokenName());

        try {
            log.info("【jwt校验】{}", token);
            Claims claims = JwtUtil.parseToken(jwtProperties.getAdminSecretKey(), token);
            if (JwtUtil.isExpired(jwtProperties.getAdminSecretKey(), token)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return false;
            }
            long id = Long.parseLong(claims.get(JwtClaimsConstant.EMP_ID).toString());
            log.info("【jwt校验】id: {}", id);
            BaseContext.setCurrentId(id);
            return true;
        } catch (Exception ex) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
    }
}
