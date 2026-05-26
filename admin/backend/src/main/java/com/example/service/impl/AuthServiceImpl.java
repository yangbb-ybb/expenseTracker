package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.common.exception.BusinessException;
import com.example.entity.dto.*;
import com.example.entity.po.User;
import com.example.repository.UserRepository;
import com.example.service.AuthService;
import com.example.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * 认证服务实现类
 */
@Service
public class AuthServiceImpl implements AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);

    /** Redis Key 前缀 */
    private static final String SMS_CODE_KEY = "sms:code:";

    /** 验证码有效期：5分钟 */
    private static final long SMS_CODE_EXPIRE = 5;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private UserRepository userRepository;

    /**
     * 发送短信验证码
     * 模拟实现：生成6位随机码存入 Redis，同时打印到日志
     */
    @Override
    public void sendSmsCode(SmsSendDTO dto) {
        String phone = dto.getPhone();

        // 生成6位数字验证码
        String code = String.format("%06d", new Random().nextInt(1000000));

        // 存入 Redis，5分钟过期
        String key = SMS_CODE_KEY + phone;
        redisTemplate.opsForValue().set(key, code, SMS_CODE_EXPIRE, TimeUnit.MINUTES);

        // TODO: 接入真实短信服务商（阿里云、腾讯云等）
        log.info("【短信验证码】手机号：{}，验证码：{}，有效期5分钟", phone, code);
    }

    /**
     * 短信验证码登录
     * 1. 从 Redis 取出验证码校验
     * 2. 校验通过后，根据手机号查询用户（不存在则自动注册）
     * 3. 生成 JWT Token 返回
     */
    @Override
    public String smsLogin(SmsLoginDTO dto) {
        String phone = dto.getPhone();
        String code = dto.getCode();

        // 从 Redis 获取验证码
        String key = SMS_CODE_KEY + phone;
        String cachedCode = redisTemplate.opsForValue().get(key);

        // 验证码校验
        if (cachedCode == null) {
            throw new BusinessException(400, "验证码已过期，请重新获取");
        }
        if (!cachedCode.equals(code)) {
            throw new BusinessException(400, "验证码错误");
        }

        // 校验通过，删除验证码（一次性使用）
        redisTemplate.delete(key);

        // 根据手机号查询用户
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getMobile, phone);
        User user = userRepository.selectOne(wrapper);

        // 用户不存在，自动注册
        if (user == null) {
            user = new User();
            user.setUsername(phone);
            user.setNickname("用户" + phone.substring(phone.length() - 4));
            user.setMobile(phone);
            user.setStatus(1);
            userRepository.insert(user);

            // 重新查询获取ID
            user = userRepository.selectOne(wrapper);
        }

        // 检查账号状态
        if (user.getStatus() == 0) {
            throw new BusinessException(401, "账号已被禁用");
        }

        // 生成 Token
        return JwtUtil.generateToken(user.getId());
    }

    @Override
    public String wxLogin(WxLoginDTO dto) {
        // TODO: 调用微信接口换取 openid，查询/创建用户，生成 Token
        throw new BusinessException(501, "微信登录功能暂未实现");
    }

    @Override
    public String aliLogin(AliLoginDTO dto) {
        // TODO: 调用支付宝接口换取 userId，查询/创建用户，生成 Token
        throw new BusinessException(501, "支付宝登录功能暂未实现");
    }

    @Override
    public String ttLogin(TtLoginDTO dto) {
        // TODO: 调用抖音接口换取 openid，查询/创建用户，生成 Token
        throw new BusinessException(501, "抖音登录功能暂未实现");
    }
}
