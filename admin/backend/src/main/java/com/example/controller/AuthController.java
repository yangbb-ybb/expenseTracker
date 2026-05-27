package com.example.controller;

import com.example.common.result.Result;
import com.example.entity.dto.*;
import com.example.entity.vo.LoginResultVO;
import com.example.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 认证控制器
 *
 * 作用：处理登录相关的 HTTP 请求（短信登录、第三方登录等）
 */
@RestController
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * 发送短信验证码
     * @param dto 手机号
     * @param request HTTP 请求（获取客户端 IP）
     * @return 无数据
     */
    @PostMapping("/sms/send")
    public Result<Void> sendSms(@RequestBody SmsSendDTO dto, HttpServletRequest request) {
        String clientIp = getClientIp(request);
        authService.sendSmsCode(dto, clientIp);
        return Result.success();
    }

    /**
     * 获取客户端真实 IP
     * 优先读取 X-Forwarded-For、X-Real-IP 请求头（反向代理场景）
     */
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // X-Forwarded-For 可能包含多个 IP，取第一个
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }

    /**
     * 短信验证码登录
     * @param dto 手机号 + 验证码
     * @return 登录结果（token + 用户信息）
     */
    @PostMapping("/login/sms")
    public Result<LoginResultVO> smsLogin(@RequestBody SmsLoginDTO dto) {
        LoginResultVO result = authService.smsLogin(dto);
        return Result.success(result);
    }

    /**
     * 微信登录
     * @param dto 微信 code
     * @return 登录结果（token + 用户信息）
     */
    @PostMapping("/login/wx")
    public Result<LoginResultVO> wxLogin(@RequestBody WxLoginDTO dto) {
        LoginResultVO result = authService.wxLogin(dto);
        return Result.success(result);
    }

    /**
     * 支付宝登录
     * @param dto 支付宝 authCode
     * @return 登录结果（token + 用户信息）
     */
    @PostMapping("/login/ali")
    public Result<LoginResultVO> aliLogin(@RequestBody AliLoginDTO dto) {
        LoginResultVO result = authService.aliLogin(dto);
        return Result.success(result);
    }

    /**
     * 抖音登录
     * @param dto 抖音 code
     * @return 登录结果（token + 用户信息）
     */
    @PostMapping("/login/tt")
    public Result<LoginResultVO> ttLogin(@RequestBody TtLoginDTO dto) {
        LoginResultVO result = authService.ttLogin(dto);
        return Result.success(result);
    }
}
