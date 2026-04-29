package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.common.exception.BusinessException;
import com.example.entity.dto.UserLoginDTO;
import com.example.entity.dto.UserRegisterDTO;
import com.example.entity.po.User;
import com.example.entity.vo.UserInfoVO;
import com.example.repository.UserRepository;
import com.example.service.UserService;
import com.example.util.JwtUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

/**
 * 用户服务实现类
 *
 * @Service 标记为 Spring 管理的服务 Bean
 * implements UserService 实现 UserService 接口
 */
@Service  // 注册为 Spring Bean
public class UserServiceImpl implements UserService {

    /**
     * 注入 UserRepository（数据访问层）
     * 通过构造方法注入
     */
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * 用户登录
     * 1. 根据用户名查询用户
     * 2. 校验密码
     * 3. 生成 JWT Token 返回
     */
    @Override
    public String login(UserLoginDTO dto) {
        // 构建查询条件：根据用户名查询
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, dto.getUsername());

        // 查询用户
        User user = userRepository.selectOne(wrapper);

        // 用户不存在，抛出业务异常
        if (user == null) {
            throw new BusinessException(401, "用户不存在");
        }

        // 密码校验：将前端传来的密码 MD5 加密后和数据库对比
        String password = DigestUtils.md5DigestAsHex(dto.getPassword().getBytes());
        if (!password.equals(user.getPassword())) {
            throw new BusinessException(401, "密码错误");
        }

        // 检查账号状态
        if (user.getStatus() == 0) {
            throw new BusinessException(401, "账号已被禁用");
        }

        // 密码正确，生成 JWT Token 返回
        return JwtUtil.generateToken(user.getId());
    }

    /**
     * 用户注册
     * 1. 检查用户名是否已存在
     * 2. 密码 MD5 加密
     * 3. 保存到数据库
     */
    @Override
    public void register(UserRegisterDTO dto) {
        // 检查用户名是否已存在
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, dto.getUsername());
        User existUser = userRepository.selectOne(wrapper);

        if (existUser != null) {
            throw new BusinessException("用户名已存在");
        }

        // 创建用户对象
        User user = new User();

        // BeanUtils.copyProperties：复制 dto 的属性到 user 对象
        // 相当于：user.setUsername(dto.getUsername()); user.setNickname(dto.getNickname()); ...
        BeanUtils.copyProperties(dto, user);

        // 密码 MD5 加密存储
        user.setPassword(DigestUtils.md5DigestAsHex(dto.getPassword().getBytes()));

        // 保存到数据库（MyBatis Plus 会自动填充 createTime 和 updateTime）
        userRepository.insert(user);
    }

    /**
     * 获取用户信息
     * 根据用户ID查询并返回用户信息（不包含密码）
     */
    @Override
    public UserInfoVO getUserInfo(Long userId) {
        // 根据ID查询用户
        User user = userRepository.selectById(userId);

        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        // 创建 VO 对象
        UserInfoVO vo = new UserInfoVO();

        // 复制属性到 VO（BeanUtils 会跳过 null 值和 password）
        // 注意：UserInfoVO 没有 password 字段，所以不会泄露密码
        BeanUtils.copyProperties(user, vo);

        return vo;
    }
}
