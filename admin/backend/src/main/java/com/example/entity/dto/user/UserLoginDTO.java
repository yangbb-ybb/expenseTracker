package com.example.entity.dto.user;

/**
 * 用户登录 DTO（Data Transfer Object，数据传输对象）
 *
 * 作用：接收前端登录请求的参数
 *
 * DTO vs PO 的区别：
 *   PO = 和数据库表一一对应（User 实体类）
 *   DTO = 用于接口数据传输，不一定和数据库表对应
 *
 * 为什么需要 DTO？
 *   前端只需要传 username 和 password，不需要传其他字段
 *   用 DTO 接收，清晰明了
 */
public class UserLoginDTO {

    /** 用户名 */
    private String username;

    /** 密码 */
    private String password;

    // ========== Getter 和 Setter ==========

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
