package com.example.entity.vo;

import java.time.LocalDateTime;

/**
 * 用户信息 VO（View Object，视图对象）
 *
 * 作用：返回给前端的用户信息
 *
 * VO vs DTO 的区别：
 *   DTO = 前端传给后端的数据
 *   VO = 后端返回给前端的数据
 *
 * 为什么需要 VO？
 *   密码不能返回给前端！
 *   数据库里的 deleted 字段也不需要返回
 *   VO 可以选择只返回需要的字段
 */
public class UserInfoVO {

    /** 用户ID */
    private Long id;

    /** 用户名 */
    private String username;

    /** 昵称 */
    private String nickname;

    /** 头像URL */
    private String avatar;

    /** 邮箱 */
    private String email;

    /** 手机号 */
    private String mobile;

    /** 账号状态：0=禁用，1=正常 */
    private Integer status;

    /** 注册时间 */
    private LocalDateTime createTime;

    // ========== Getter 和 Setter ==========

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }

    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getMobile() { return mobile; }
    public void setMobile(String mobile) { this.mobile = mobile; }

    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }

    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
}
