package com.example.controller;

import com.example.common.result.Result;
import com.example.entity.dto.*;
import com.example.entity.vo.LoginResultVO;
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
