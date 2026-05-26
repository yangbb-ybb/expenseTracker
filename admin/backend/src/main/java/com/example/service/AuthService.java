package com.example.service;

import com.example.entity.dto.*;

/**
 * 认证服务接口
 *
 * 作用：处理登录相关的业务逻辑（短信登录、第三方登录等）
 */
public interface AuthService {

    /**
     * 发送短信验证码
     * @param dto 手机号
     */
    void sendSmsCode(SmsSendDTO dto);

    /**
     * 短信验证码登录
     * @param dto 手机号 + 验证码
     * @return JWT Token
     */
    String smsLogin(SmsLoginDTO dto);

    /**
     * 微信登录
     * @param dto 微信 code
     * @return JWT Token
     */
    String wxLogin(WxLoginDTO dto);

    /**
     * 支付宝登录
     * @param dto 支付宝 authCode
     * @return JWT Token
     */
    String aliLogin(AliLoginDTO dto);

    /**
     * 抖音登录
     * @param dto 抖音 code
     * @return JWT Token
     */
    String ttLogin(TtLoginDTO dto);
}
