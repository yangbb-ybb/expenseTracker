package com.example.config;

import com.example.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * JWT 认证拦截器
 *
 * 作用：
 *   1. 验证请求中的 Token 是否有效
 *   2. 如果 Token 快过期了（剩余时间 < 阈值），自动续期
 *   3. 续期后的新 Token 通过响应头返回给前端
 *
 * 滑动过期逻辑：
 *   - 用户有操作（发请求）→ Token 自动续期
 *   - 用户长时间没操作 → Token 过期，需重新登录
 */
@Component  // 注册为 Spring Bean
public class JwtInterceptor implements HandlerInterceptor {

    /**
     * 日志对象
     * 使用 SLF4J 日志框架
     * log.info()、log.warn()、log.error() 是常用的日志级别
     */
    private static final Logger log = LoggerFactory.getLogger(JwtInterceptor.class);

    /**
     * Token 续期阈值（毫秒）
     * 可在 application.yml 中配置：jwt.renewal-threshold
     * 默认：1小时（60 * 60 * 1000）
     */
    @Value("${jwt.renewal-threshold:3600000}")
    private long renewalThreshold;

    /**
     * Token 有效期（毫秒）
     * 可在 application.yml 中配置：jwt.validity
     * 默认：7天（7 * 24 * 60 * 60 * 1000）
     */
    @Value("${jwt.validity:604800000}")
    private long tokenValidity;

    /**
     * 请求前处理
     *
     * @param request  HTTP 请求
     * @param response HTTP 响应
     * @param handler  处理器
     * @return true=放行，false=拦截
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 从请求头获取 Token（字段名：Authorization，格式：Bearer xxx）
        String authHeader = request.getHeader("Authorization");

        // 如果没有 Token，放行（有些接口可能不需要登录）
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return true;
        }

        // 提取 Token 字符串
        String token = authHeader.substring(7);

        try {
            // 解析 Token，获取用户ID
            Long userId = JwtUtil.parseToken(token);

            // ========== 滑动过期逻辑 ==========
            // 计算剩余有效期
            long remainingTime = JwtUtil.getRemainingTime(token);

            // 如果剩余时间 < 阈值，自动续期
            if (remainingTime < renewalThreshold) {
                // 生成新的 Token
                String newToken = JwtUtil.generateToken(userId, tokenValidity);

                // 把新 Token 放到响应头里，前端拿到新 Token 替换旧的
                response.setHeader("Authorization", "Bearer " + newToken);

                // 记录日志：级别从高到低 trace > debug > info > warn > error
                // 一般业务用 info，需要排查问题用 debug
                log.info("Token 自动续期，用户ID: {}，新有效期: {} 天", userId, tokenValidity / 1000 / 60 / 60 / 24);
            }

            // 把用户ID放到请求属性里，后面 Controller 可以直接用
            request.setAttribute("userId", userId);

            // 放行
            return true;

        } catch (Exception e) {
            // Token 无效或过期，记录警告日志
            log.warn("Token 无效或已过期: {}", e.getMessage());
            // 返回 true 让请求继续，后面由 Controller 判断处理
            return true;
        }
    }
}
