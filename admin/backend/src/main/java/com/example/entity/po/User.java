package com.example.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;

/**
 * 用户实体类（PO：Persistent Object，持久化对象）
 *
 * 作用：对应数据库中的 sys_user 表
 *
 * @TableName("sys_user") 告诉 MyBatis Plus 这个类对应哪张表
 */
@TableName("sys_user")
public class User {

    /** 用户ID，主键，自增 */
    private Long id;

    /** 用户名，用于登录，唯一 */
    private String username;

    /** 密码，MD5 加密存储 */
    private String password;

    /** 用户昵称，展示用 */
    private String nickname;

    /** 头像 URL 地址 */
    private String avatar;

    /** 邮箱 */
    private String email;

    /** 手机号 */
    private String mobile;

    /** 账号状态：0=禁用，1=正常 */
    private Integer status;

    /** 创建时间，自动填充 */
    private LocalDateTime createTime;

    /** 更新时间，自动填充 */
    private LocalDateTime updateTime;

    /** 逻辑删除标记：0=未删除，1=已删除（MyBatis Plus 自动处理） */
    private Integer deleted;

    // ========== Getter 和 Setter ==========

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

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

    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }

    public Integer getDeleted() { return deleted; }
    public void setDeleted(Integer deleted) { this.deleted = deleted; }
}
