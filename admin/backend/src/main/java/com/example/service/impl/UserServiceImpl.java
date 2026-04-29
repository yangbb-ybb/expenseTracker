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

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public String login(UserLoginDTO dto) {
        User user = userRepository.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, dto.getUsername()));
        if (user == null) {
            throw new BusinessException(401, "用户不存在");
        }
        String password = DigestUtils.md5DigestAsHex((dto.getPassword()).getBytes());
        if (!password.equals(user.getPassword())) {
            throw new BusinessException(401, "密码错误");
        }
        if (user.getStatus() == 0) {
            throw new BusinessException(401, "账号已被禁用");
        }
        return JwtUtil.generateToken(user.getId());
    }

    @Override
    public void register(UserRegisterDTO dto) {
        User existUser = userRepository.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, dto.getUsername()));
        if (existUser != null) {
            throw new BusinessException("用户名已存在");
        }
        User user = new User();
        BeanUtils.copyProperties(dto, user);
        user.setPassword(DigestUtils.md5DigestAsHex(dto.getPassword().getBytes()));
        userRepository.insert(user);
    }

    @Override
    public UserInfoVO getUserInfo(Long userId) {
        User user = userRepository.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        UserInfoVO vo = new UserInfoVO();
        BeanUtils.copyProperties(user, vo);
        return vo;
    }
}