package com.example.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC 配置类
 *
 * 作用：配置 Spring MVC 的一些选项
 *   - 添加拦截器（Interceptor）
 *   - 配置静态资源处理
 *
 * @WebMvcConfigurer Spring MVC 配置接口
 *                   实现此接口可以自定义 Spring MVC 配置
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    /**
     * 注入 JWT 拦截器
     */
    @Autowired
    private JwtInterceptor jwtInterceptor;

    /**
     * 添加拦截器
     *
     * @param registry 拦截器注册表
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 创建拦截器注册构建器
        // addPathPatterns("/**") 表示拦截所有请求
        // excludePathPatterns(...) 表示排除哪些路径不拦截
        registry.addInterceptor(jwtInterceptor)
                // 拦截所有请求（基于 context-path: /api 之后的路径）
                .addPathPatterns("/user/**")
                .addPathPatterns("/userDailyConsumeDetail", "/userDailyConsumeDetail/**")
                // 排除不需要 Token 的路径
                .excludePathPatterns("/user/login", "/user/register")
                .excludePathPatterns("/authSms/send", "/authLogin/sms", "/authLogin/wx", "/authLogin/ali", "/authLogin/tt")
                .excludePathPatterns("/userBalance/**")
                // 排除接口文档路径
                .excludePathPatterns("/doc.html", "/swagger-ui/**", "/v3/api-docs/**");
    }
}
