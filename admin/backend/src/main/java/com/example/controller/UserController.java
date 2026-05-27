package com.example.controller;

import com.example.common.result.Result;
import com.example.entity.dto.user.UserLoginDTO;
import com.example.entity.dto.user.UserRegisterDTO;
import com.example.entity.vo.UserInfoVO;
import com.example.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户控制器
 *
 * 作用：接收 HTTP 请求，调用 Service 处理，返回响应
 *
 * @RestController = @Controller + @ResponseBody
 *                   所有方法返回 JSON 数据
 *
 * @RequestMapping("/user") 所有接口路径以 /user 开头
 */
@RestController
@RequestMapping("/user")
public class UserController {

    /**
     * 注入 UserService
     * 通过构造方法注入
     */
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 用户登录接口
     *
     * @PostMapping("/login")  处理 POST 请求，路径：/user/login
     * @RequestBody           将请求体 JSON 转换为 UserLoginDTO 对象
     * @Valid                 开启参数校验（@NotBlank 等注解生效）
     *
     * @param dto 登录参数（用户名+密码）
     * @return Token 字符串
     */
    @PostMapping("/login")
    public Result<String> login(@Valid @RequestBody UserLoginDTO dto) {
        // 调用 Service 处理登录逻辑
        String token = userService.login(dto);

        // 返回成功响应（Token 在 data 中）
        return Result.success(token);
    }

    /**
     * 用户注册接口
     *
     * @PostMapping("/register")  处理 POST 请求，路径：/user/register
     * @RequestBody              将请求体 JSON 转换为 UserRegisterDTO 对象
     *
     * @param dto 注册参数
     * @return 无返回数据
     */
    @PostMapping("/register")
    public Result<Void> register(@Valid @RequestBody UserRegisterDTO dto) {
        // 调用 Service 处理注册逻辑
        userService.register(dto);

        // 返回成功响应（无数据）
        return Result.success();
    }

    /**
     * 获取用户信息接口
     *
     * @GetMapping("/info")  处理 GET 请求，路径：/user/info
     * 从请求属性中获取 userId（JwtInterceptor 解析 token 后存入）
     *
     * @param request HTTP 请求
     * @return 用户信息
     */
    @GetMapping("/info")
    public Result<UserInfoVO> getUserInfo(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");

        // 调用 Service 获取用户信息
        UserInfoVO userInfo = userService.getUserInfo(userId);

        // 返回成功响应（用户信息在 data 中）
        return Result.success(userInfo);
    }
}
