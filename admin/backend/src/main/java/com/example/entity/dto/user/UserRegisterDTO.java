package com.example.entity.dto;

/**
 * 用户注册 DTO
 *
 * 作用：接收前端注册请求的参数
 */
public class UserRegisterDTO {

    /** 用户名（必填） */
    private String username;

    /** 密码（必填） */
    private String password;

    /** 昵称（选填） */
    private String nickname;

    /** 邮箱（选填） */
    private String email;

    /** 手机号（选填） */
    private String mobile;

    // ========== Getter 和 Setter ==========

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getMobile() { return mobile; }
    public void setMobile(String mobile) { this.mobile = mobile; }
}
