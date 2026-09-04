package com.example.entity.dto.auth;

/**
 * 发送短信验证码 DTO
 */
public class SmsSendDTO {

    /** 手机号 */
    private String phone;

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
}
