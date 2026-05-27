package com.example.entity.dto;

/**
 * 支付宝登录 DTO
 */
public class AliLoginDTO {

    /** 支付宝 authCode */
    private String authCode;

    public String getAuthCode() { return authCode; }
    public void setAuthCode(String authCode) { this.authCode = authCode; }
}
