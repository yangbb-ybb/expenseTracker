package com.example.service;

import com.example.entity.dto.UserLoginDTO;
import com.example.entity.dto.UserRegisterDTO;
import com.example.entity.vo.UserInfoVO;

public interface UserService {
    String login(UserLoginDTO dto);
    void register(UserRegisterDTO dto);
    UserInfoVO getUserInfo(Long userId);
}