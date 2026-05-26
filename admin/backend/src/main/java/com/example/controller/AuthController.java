package com.example.controller;

import com.example.common.result.Result;
import com.example.entity.dto.*;
import com.example.service.AuthService;
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
     * @return 无数据
     */
    @PostMapping("/sms/send")
    public Result<Void> sendSms(@RequestBody SmsSendDTO dto) {
        authService.sendSmsCode(dto);
        return Result.success();
    }

    /**
     * 短信验证码登录
     * @param dto 手机号 + 验证码
     * @return JWT Token
     */
    @PostMapping("/login/sms")
    public Result<String> smsLogin(@RequestBody SmsLoginDTO dto) {
        String token = authService.smsLogin(dto);
        return Result.success(token);
    }

    /**
     * 微信登录
     * @param dto 微信 code
     * @return JWT Token
     */
    @PostMapping("/login/wx")
    public Result<String> wxLogin(@RequestBody WxLoginDTO dto) {
        String token = authService.wxLogin(dto);
        return Result.success(token);
    }

    /**
     * 支付宝登录
     * @param dto 支付宝 authCode
     * @return JWT Token
     */
    @PostMapping("/login/ali")
    public Result<String> aliLogin(@RequestBody AliLoginDTO dto) {
        String token = authService.aliLogin(dto);
        return Result.success(token);
    }

    /**
     * 抖音登录
     * @param dto 抖音 code
     * @return JWT Token
     */
    @PostMapping("/login/tt")
    public Result<String> ttLogin(@RequestBody TtLoginDTO dto) {
        String token = authService.ttLogin(dto);
        return Result.success(token);
    }
}
