package com.example.entity.vo;

/**
 * 登录结果 VO
 */
public class LoginResultVO {

    /** JWT Token */
    private String token;

    /** 用户ID */
    private Long userId;

    /** 手机号 */
    private String phone;

    /** 用户昵称 */
    private String nickname;

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }
}
