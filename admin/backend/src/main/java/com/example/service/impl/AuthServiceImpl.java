package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.common.exception.BusinessException;
import com.example.entity.dto.auth.*;
import com.example.entity.dto.user.UserLoginDTO;
import com.example.entity.po.User;
import com.example.entity.vo.LoginResultVO;
import com.example.repository.UserRepository;
import com.example.service.AuthService;
import com.example.util.JwtUtil;
import com.example.util.PasswordUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * 认证服务实现类
 */
@Service
public class AuthServiceImpl implements AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);

    /** 手机号正则 */
    private static final Pattern PHONE_PATTERN = Pattern.compile("^1[3-9]\\d{9}$");

    /** Redis Key 前缀 */
    private static final String SMS_CODE_KEY = "sms:code:";
    private static final String SMS_FREQ_KEY = "sms:freq:";
    private static final String SMS_IP_KEY = "sms:ip:";

    /** 验证码有效期：5分钟 */
    private static final long SMS_CODE_EXPIRE = 5;

    /** 发送冷却时间：60秒 */
    private static final long SMS_COOLDOWN_SECONDS = 60;

    /** 同一 IP 60 秒内最多发送次数 */
    private static final int SMS_IP_MAX_COUNT = 5;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private UserRepository userRepository;

    /**
     * 发送短信验证码
     * 1. 校验手机号格式
     * 2. 检查发送频率（同一手机号 60 秒内不能重复）
     * 3. 检查 IP 防刷（同一 IP 60 秒内最多 5 次）
     * 4. 生成验证码并存入 Redis
     */
    @Override
    public void sendSmsCode(SmsSendDTO dto, String clientIp) {
        String phone = dto.getPhone();

        // 1. 校验手机号格式
        if (phone == null || !PHONE_PATTERN.matcher(phone).matches()) {
            throw new BusinessException(400, "手机号格式不正确");
        }

        // 2. 检查手机号发送频率（60 秒内是否已发送）
        String freqKey = SMS_FREQ_KEY + phone;
        if (Boolean.TRUE.equals(redisTemplate.hasKey(freqKey))) {
            Long ttl = redisTemplate.getExpire(freqKey, TimeUnit.SECONDS);
            long remain = ttl != null ? ttl : SMS_COOLDOWN_SECONDS;
            throw new BusinessException(400, "验证码已发送，请" + remain + "秒后再试");
        }

        // 3. 检查 IP 防刷（同一 IP 60 秒内最多 5 次）
        if (clientIp != null && !clientIp.isEmpty()) {
            String ipKey = SMS_IP_KEY + clientIp;
            String ipCount = redisTemplate.opsForValue().get(ipKey);
            if (ipCount != null) {
                int count = Integer.parseInt(ipCount);
                if (count >= SMS_IP_MAX_COUNT) {
                    throw new BusinessException(400, "发送过于频繁，请稍后再试");
                }
                redisTemplate.opsForValue().increment(ipKey);
            } else {
                redisTemplate.opsForValue().set(ipKey, "1", SMS_COOLDOWN_SECONDS, TimeUnit.SECONDS);
            }
        }

        // 4. 生成6位数字验证码
        String code = String.format("%06d", new Random().nextInt(1000000));

        // 5. 存入 Redis，5分钟过期
        String key = SMS_CODE_KEY + phone;
        redisTemplate.opsForValue().set(key, code, SMS_CODE_EXPIRE, TimeUnit.MINUTES);

        // 6. 记录手机号发送频率（60 秒内禁止重复发送）
        redisTemplate.opsForValue().set(freqKey, "1", SMS_COOLDOWN_SECONDS, TimeUnit.SECONDS);

        // TODO: 接入真实短信服务商（阿里云、腾讯云等）
        log.info("【短信验证码】手机号：{}，验证码：{}，IP：{}，有效期5分钟", phone, code, clientIp);
    }

    /**
     * 短信验证码登录
     * 1. 从 Redis 取出验证码校验
     * 2. 校验通过后，根据手机号查询用户（不存在则自动注册）
     * 3. 生成 JWT Token 并返回用户信息
     */
    @Override
    public LoginResultVO smsLogin(SmsLoginDTO dto) {
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
            user.setPassword(DigestUtils.md5DigestAsHex(PasswordUtil.generateRandomPassword().getBytes()));
            userRepository.insert(user);

            // 重新查询获取ID
            user = userRepository.selectOne(wrapper);
        }

        // 检查账号状态
        if (user.getStatus() == 0) {
            throw new BusinessException(401, "账号已被禁用");
        }

        // 组装登录结果
        LoginResultVO result = new LoginResultVO();
        result.setToken(JwtUtil.generateToken(user.getId(), "sms"));
        result.setUserId(user.getId());
        result.setPhone(user.getMobile());
        result.setNickname(user.getNickname());
        return result;
    }

    @Override
    public LoginResultVO wxLogin(WxLoginDTO dto) {
        // TODO: 调用微信接口换取 openid，查询/创建用户，生成 Token（loginType = "wx"）
        throw new BusinessException(501, "微信登录功能暂未实现");
    }

    @Override
    public LoginResultVO aliLogin(AliLoginDTO dto) {
        // TODO: 调用支付宝接口换取 userId，查询/创建用户，生成 Token（loginType = "ali"）
        throw new BusinessException(501, "支付宝登录功能暂未实现");
    }

    @Override
    public LoginResultVO ttLogin(TtLoginDTO dto) {
        // TODO: 调用抖音接口换取 openid，查询/创建用户，生成 Token（loginType = "tt"）
        throw new BusinessException(501, "抖音登录功能暂未实现");
    }
}
