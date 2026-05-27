package com.example.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.Date;

/**
 * JWT Token 工具类
 *
 * 什么是 JWT？
 *   JWT = JSON Web Token，一种开放标准（RFC 7519）
 *   用于在各方之间安全地传输信息
 *
 * JWT 的用途？
 *   1. 用户登录成功后，服务器生成一个 JWT 返回给前端
 *   2. 前端后续请求带着这个 JWT
 *   3. 服务器验证 JWT，识别用户身份
 *
 * JWT 结构（三部分，用 . 分隔）：
 *   Header.Payload.Signature
 *   例如：xxx.yyy.zzz
 */
public class JwtUtil {

    /**
     * 加密密钥
     * 生产环境要改成复杂的随机字符串，且长度至少 32 字符
     * 用于签名和验证 JWT
     */
    private static final String SECRET = "your-secret-key-must-be-at-least-32-characters-long";

    /**
     * 默认 Token 有效期：7天（毫秒）
     * 可以通过 generateToken(userId, validity) 自定义有效期
     */
    private static final long DEFAULT_VALIDITY = 7 * 24 * 60 * 60 * 1000L;

    /**
     * 生成 Token（使用默认有效期 7 天）
     *
     * @param userId 用户ID
     * @return JWT Token 字符串
     */
    public static String generateToken(Long userId) {
        return generateToken(userId, null, DEFAULT_VALIDITY);
    }

    /**
     * 生成 Token（携带登录方式）
     *
     * @param userId    用户ID
     * @param loginType 登录方式：sms、wx、ali、tt
     * @return JWT Token 字符串
     */
    public static String generateToken(Long userId, String loginType) {
        return generateToken(userId, loginType, DEFAULT_VALIDITY);
    }

    /**
     * 生成 Token（自定义有效期，不带登录方式）
     *
     * @param userId   用户ID
     * @param validity 有效期（毫秒）
     * @return JWT Token 字符串
     */
    public static String generateToken(Long userId, long validity) {
        return generateToken(userId, null, validity);
    }

    /**
     * 生成 Token（自定义有效期 + 登录方式）
     *
     * @param userId    用户ID
     * @param loginType 登录方式
     * @param validity  有效期（毫秒）
     * @return JWT Token 字符串
     */
    public static String generateToken(Long userId, String loginType, long validity) {
        var builder = Jwts.builder()
                // 设置 token 主题，存储用户ID
                .subject(String.valueOf(userId))
                // 设置签发时间（当前时间）
                .issuedAt(new Date())
                // 设置过期时间（当前时间 + 有效期）
                .expiration(new Date(System.currentTimeMillis() + validity));

        // 如果有登录方式，存入自定义 claim
        if (loginType != null && !loginType.isEmpty()) {
            builder.claim("loginType", loginType);
        }

        return builder
                // 用密钥签名（HMAC SHA 算法）
                .signWith(Keys.hmacShaKeyFor(SECRET.getBytes()))
                // 生成 token 字符串
                .compact();
    }

    /**
     * 解析 Token（验证签名，获取用户ID）
     *
     * @param token JWT Token 字符串
     * @return 用户ID
     *
     * 注意：如果 token 过期或被篡改，会抛出异常
     */
    public static Long parseToken(String token) {
        // 解析 token 的各个部分
        Claims claims = Jwts.parser()
                // 用密钥验证签名
                .verifyWith(Keys.hmacShaKeyFor(SECRET.getBytes()))
                // 构建解析器
                .build()
                // 解析 token（如果签名不匹配会抛异常）
                .parseSignedClaims(token)
                // 获取载荷（Payload）
                .getPayload();

        // 返回存储的用户ID
        return Long.parseLong(claims.getSubject());
    }

    /**
     * 获取 Token 过期时间
     *
     * @param token JWT Token 字符串
     * @return 过期时间戳（毫秒）
     */
    public static long getExpiration(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(SECRET.getBytes()))
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.getExpiration().getTime();
    }

    /**
     * 解析登录方式
     *
     * @param token JWT Token 字符串
     * @return 登录方式（sms、wx、ali、tt），没有则返回 null
     */
    public static String parseLoginType(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(SECRET.getBytes()))
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.get("loginType", String.class);
    }

    /**
     * 计算 Token 剩余有效期
     *
     * @param token JWT Token 字符串
     * @return 剩余时间（毫秒），<=0 表示已过期
     */
    public static long getRemainingTime(String token) {
        long expiration = getExpiration(token);
        return expiration - System.currentTimeMillis();
    }
}
