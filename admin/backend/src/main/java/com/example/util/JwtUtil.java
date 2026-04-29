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
 *
 * 为什么用 JWT？
 *   - 无状态：服务器不需要存储 session
 *   - 可验证：签名保证了内容不被篡改
 *   - 跨域：可以在不同域之间传递
 */
public class JwtUtil {

    /**
     * 加密密钥
     * 生产环境要改成复杂的随机字符串，且长度至少 32 字符
     * 用于签名和验证 JWT
     */
    private static final String SECRET = "your-secret-key-must-be-at-least-32-characters-long";

    /**
     * Token 有效期：24小时（单位：毫秒）
     * 24 * 60 * 60 * 1000 = 86400000
     */
    private static final long EXPIRATION = 86400000L;

    /**
     * 生成 Token
     *
     * @param userId 用户ID
     * @return JWT Token 字符串
     *
     * JWT 生成过程：
     *   1. 创建 Builder
     *   2. 设置主题（subject）= 用户ID
     *   3. 设置签发时间
     *   4. 设置过期时间
     *   5. 用密钥签名
     *   6. 生成 token 字符串
     */
    public static String generateToken(Long userId) {
        return Jwts.builder()
                // 设置 token 主题，存储用户ID
                .subject(String.valueOf(userId))
                // 设置签发时间（当前时间）
                .issuedAt(new Date())
                // 设置过期时间（当前时间 + 24小时）
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION))
                // 用密钥签名（HMAC SHA 算法）
                .signWith(Keys.hmacShaKeyFor(SECRET.getBytes()))
                // 生成 token 字符串
                .compact();
    }

    /**
     * 解析 Token
     *
     * @param token JWT Token 字符串
     * @return 用户ID
     *
     * 解析过程：
     *   1. 用密钥验证签名（防止篡改）
     *   2. 解析 token，获取 Claims（载荷）
     *   3. 从 Claims 中取出 subject（用户ID）
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
}
