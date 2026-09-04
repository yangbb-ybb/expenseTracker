package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * CORS 跨域配置
 *
 * 作用：允许前端页面 ajax 访问后端接口
 *
 * 什么是跨域？
 *   浏览器的安全策略，同源策略
 *   如果前端页面是 http://localhost:8080，后端接口是 http://localhost:8081
 *   端口不同，浏览器会阻止前端请求后端（除非后端允许跨域）
 *
 * 配置说明：
 *   - setAllowCredentials(true)：允许携带 cookie
 *   - addAllowedOriginPattern("*")：允许所有来源
 *   - addAllowedHeader("*")：允许所有请求头
 *   - addAllowedMethod("*")：允许所有请求方法（GET、POST、PUT、DELETE 等）
 */
@Configuration  // 标记为配置类，等价于 XML 配置文件
public class CorsConfig {

    /**
     * 创建 CORS 过滤器 Bean
     * @return CorsFilter 过滤器
     */
    @Bean
    public CorsFilter corsFilter() {
        // 创建 CORS 配置对象
        CorsConfiguration config = new CorsConfiguration();

        // 允许携带认证信息（cookie、authorization header）
        config.setAllowCredentials(true);

        // 允许所有来源（生产环境建议指定具体域名）
        config.addAllowedOriginPattern("*");

        // 允许所有请求头
        config.addAllowedHeader("*");

        // 允许所有请求方法
        config.addAllowedMethod("*");

        // 预检请求（OPTIONS）的缓存时间（秒），减少预检请求
        config.setMaxAge(3600L);

        // 创建 URL 路径配置源，配置哪些路径需要跨域支持
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        // 所有路径都支持跨域
        source.registerCorsConfiguration("/**", config);

        // 返回 CORS 过滤器
        return new CorsFilter(source);
    }
}
