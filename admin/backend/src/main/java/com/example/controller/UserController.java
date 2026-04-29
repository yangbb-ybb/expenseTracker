package com.example.controller;

import com.example.common.result.Result;
import com.example.entity.dto.UserLoginDTO;
import com.example.entity.dto.UserRegisterDTO;
import com.example.entity.vo.UserInfoVO;
import com.example.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public Result<String> login(@Valid @RequestBody UserLoginDTO dto) {
        String token = userService.login(dto);
        return Result.success(token);
    }

    @PostMapping("/register")
    public Result<Void> register(@Valid @RequestBody UserRegisterDTO dto) {
        userService.register(dto);
        return Result.success();
    }

    @GetMapping("/info")
    public Result<UserInfoVO> getUserInfo(@RequestParam Long userId) {
        return Result.success(userService.getUserInfo(userId));
    }
}