package com.zdkk.speed.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

public class JwtUtil {

    /**
     * 生成 JWT Token
     *
     * @param claims 自定义载荷（比如 userId、username 等）
     * @return 签名后的 JWT 字符串
     */
    public static String createJwt(String secretKey, long ttlMillis, Map<String, Object> claims) {
        Date now = new Date();
        // jwt的超时时间
        long expMillis = System.currentTimeMillis() + ttlMillis;
        Date expireDate = new Date(expMillis);

        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        return Jwts.builder()
                .claims(claims)                              // 自定义载荷
                .issuedAt(now)                               // 签发时间
                .expiration(expireDate)                      // 过期时间
                .signWith(key, Jwts.SIG.HS512)              // 使用 HS512 签名
                .compact();
    }

    /**
     * 解析 JWT Token
     *
     * @param token JWT 字符串
     * @return 载荷 Claims，解析失败返回 null
     */
    public static Claims parseToken(String secretKey, String token) {
        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        return Jwts.parser()
                .verifyWith(key)                         // 使用同一密钥验签
                .build()
                .parseSignedClaims(token)                // 解析并校验签名+过期
                .getPayload();
    }

    /**
     * 判断 JWT 是否已过期
     *
     * @param secretKey 签名密钥
     * @param token     JWT 字符串
     * @return true=已过期，false=未过期
     */
    public static boolean isExpired(String secretKey, String token) {
        try {
            Claims claims = parseToken(secretKey, token);
            Date expiration = claims.getExpiration();
            return expiration == null || expiration.before(new Date());
        } catch (Exception e) {
            // 解析失败（签名错误、格式错误、已过期等）统一视为不可用
            return true;
        }
    }
}
