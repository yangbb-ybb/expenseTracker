package com.example.entity.dto.auth;

/**
 * 短信验证码登录 DTO
 */
public class SmsLoginDTO {

    /** 手机号 */
    private String phone;

    /** 验证码 */
    private String code;

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
}
