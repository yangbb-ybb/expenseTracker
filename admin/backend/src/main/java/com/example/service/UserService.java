package com.example.service;

import com.example.entity.dto.user.UserLoginDTO;
import com.example.entity.dto.user.UserRegisterDTO;
import com.example.entity.dto.user.UserUpdateDTO;
import com.example.entity.vo.UserInfoVO;

/**
 * 用户服务接口
 *
 * 作用：定义用户相关的业务方法
 *
 * 为什么需要接口？
 *   接口定义了"做什么"，实现类决定"怎么做"
 *   方便以后替换实现（比如从 MySQL 换成 MongoDB）
 *
 * 设计原则：面向接口编程
 */
public interface UserService {

    /**
     * 用户登录
     * @param dto 登录参数（用户名+密码）
     * @return JWT Token 字符串
     */
    String login(UserLoginDTO dto);

    /**
     * 用户注册
     * @param dto 注册参数
     */
    void register(UserRegisterDTO dto);

    /**
     * 获取用户信息
     * @param userId 用户ID
     * @return 用户信息
     */
    UserInfoVO getUserInfo(Long userId);

    /**
     * 修改用户信息
     * @param userId 用户ID
     * @param dto 用户更新信息
     * @return 更新后的用户信息
     */
    UserInfoVO updateUserInfo(Long userId, UserUpdateDTO dto);
}
