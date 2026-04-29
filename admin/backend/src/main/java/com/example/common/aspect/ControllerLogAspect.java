package com.example.common.aspect;

import com.alibaba.fastjson2.JSON;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 控制器日志切面
 *
 * 作用：记录每个接口的调用日志，包括：
 *   - 请求参数
 *   - 返回结果
 *   - 执行时间
 *   - 请求 IP
 *
 * 什么是 AOP？
 *   AOP = Aspect Oriented Programming（面向切面编程）
 *   可以在不修改原有代码的情况下，在方法执行前后添加额外逻辑
 *
 * 使用场景：
 *   - 日志记录（当前使用）
 *   - 权限校验
 *   - 事务管理
 *   - 性能监控
 */
@Aspect  // 标记为切面类
@Component  // 注册为 Spring Bean
public class ControllerLogAspect {

    /**
     * 日志对象
     */
    private static final Logger log = LoggerFactory.getLogger(ControllerLogAspect.class);

    /**
     * 时间格式化
     */
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 定义切点（Pointcut）
     * 解释：controller 包下所有类的所有方法都会被这个切面拦截
     */
    @Pointcut("execution(* com.example.controller..*.*(..))")
    public void controllerPointcut() {
        // 这是一个空方法，只是用来定义切点
        // 下面的 @Around 注解引用了这个切点
    }

    /**
     * 环绕通知
     *
     * 解释：在目标方法执行前后都会执行
     *
     * @param joinPoint 连接点，即被拦截的方法
     * @return 方法执行结果
     * @throws Throwable 方法执行抛出的异常
     */
    @Around("controllerPointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        // ========== 请求处理前 ==========
        long startTime = System.currentTimeMillis();  // 记录开始时间

        // 获取请求信息
        HttpServletRequest request = getRequest();

        // 获取方法信息
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        // 获取类名和方法名
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = method.getName();

        // 获取请求参数
        String params = getParams(joinPoint, method);

        // 获取请求 IP
        String ip = getIpAddress(request);

        // 获取请求 URI
        String uri = request != null ? request.getRequestURI() : "";

        // 获取请求方式
        String httpMethod = request != null ? request.getMethod() : "";

        // 打印请求开始日志
        log.info("========== 接口请求开始 ==========");
        log.info("请求时间: {}", LocalDateTime.now().format(FORMATTER));
        log.info("请求 IP: {}", ip);
        log.info("请求方法: {} {}", httpMethod, uri);
        log.info("类名方法: {}.{}", className, methodName);
        log.info("请求参数: {}", params);

        // 执行目标方法（调用 Controller 的方法）
        Object result = null;
        Throwable error = null;

        try {
            // 调用目标方法，获取返回值
            result = joinPoint.proceed();
            return result;  // 返回结果
        } catch (Throwable e) {
            // 捕获异常
            error = e;
            throw e;  // 重新抛出，让 Spring 处理
        } finally {
            // ========== 请求处理后 ==========
            long endTime = System.currentTimeMillis();
            long costTime = endTime - startTime;  // 执行耗时

            // 打印请求结束日志
            if (error != null) {
                // 有异常的情况
                log.error("请求耗时: {} ms", costTime);
                log.error("接口异常: {}", error.getMessage());
                log.error("========== 接口请求结束 ==========");
            } else {
                // 正常情况
                log.info("请求耗时: {} ms", costTime);
                log.info("返回结果: {}", JSON.toJSONString(result));
                log.info("========== 接口请求结束 ==========");
            }
        }
    }

    /**
     * 获取请求对象
     */
    private HttpServletRequest getRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attributes != null ? attributes.getRequest() : null;
    }

    /**
     * 获取请求参数
     *
     * @param joinPoint 连接点
     * @param method 方法
     * @return 格式化后的参数字符串
     */
    private String getParams(ProceedingJoinPoint joinPoint, Method method) {
        // 获取方法的所有参数
        Parameter[] parameters = method.getParameters();
        // 获取参数值
        Object[] args = joinPoint.getArgs();

        // 如果参数为空，返回空字符串
        if (args == null || args.length == 0) {
            return "{}";
        }

        // 构建参数字符串
        StringBuilder sb = new StringBuilder("{");
        for (int i = 0; i < parameters.length; i++) {
            // 跳过 HttpServletRequest 和 HttpServletResponse
            if (args[i] instanceof jakarta.servlet.http.HttpServletRequest ||
                args[i] instanceof jakarta.servlet.http.HttpServletResponse) {
                continue;
            }

            // 获取参数名
            String paramName = parameters[i].getName();
            // 获取参数值
            Object paramValue = args[i];

            // 如果有 @RequestBody 注解，参数值是对象
            if (parameters[i].isAnnotationPresent(RequestBody.class) && paramValue != null) {
                // 把对象转成 JSON 字符串
                paramValue = JSON.toJSONString(paramValue);
            }

            if (i > 0) {
                sb.append(", ");
            }
            sb.append("\"").append(paramName).append("\": ").append(paramValue);
        }
        sb.append("}");

        return sb.toString();
    }

    /**
     * 获取请求 IP 地址
     */
    private String getIpAddress(HttpServletRequest request) {
        if (request == null) {
            return "unknown";
        }

        // 优先从代理头获取
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        // 多个代理的情况，取第一个 IP
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }

        return ip;
    }
}
