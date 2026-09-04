package com.example.service;

import com.example.entity.dto.auth.*;
import com.example.entity.dto.user.UserLoginDTO;
import com.example.entity.vo.LoginResultVO;

/**
 * 认证服务接口
 *
 * 作用：处理登录相关的业务逻辑（短信登录、第三方登录等）
 */
public interface AuthService {

    /**
     * 发送短信验证码
     * @param dto 手机号
     * @param clientIp 客户端 IP
     */
    void sendSmsCode(SmsSendDTO dto, String clientIp);

    /**
     * 短信验证码登录
     * @param dto 手机号 + 验证码
     * @return 登录结果（token + 用户信息）
     */
    LoginResultVO smsLogin(SmsLoginDTO dto);

    /**
     * 微信登录
     * @param dto 微信 code
     * @return 登录结果（token + 用户信息）
     */
    LoginResultVO wxLogin(WxLoginDTO dto);

    /**
     * 支付宝登录
     * @param dto 支付宝 authCode
     * @return 登录结果（token + 用户信息）
     */
    LoginResultVO aliLogin(AliLoginDTO dto);

    /**
     * 抖音登录
     * @param dto 抖音 code
     * @return 登录结果（token + 用户信息）
     */
    LoginResultVO ttLogin(TtLoginDTO dto);
}
