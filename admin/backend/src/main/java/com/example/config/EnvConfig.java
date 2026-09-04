package com.example.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * 环境配置类
 *
 * 作用：明确指定当前运行的环境
 *
 * ⚠️ 接手项目的人必看！
 * ⚠️ 修改此文件的 @Profile 注解值来切换环境！
 *
 * 可选值：
 *   - "dev"   开发环境（本地 MySQL）
 *   - "test"  测试环境（测试服务器 MySQL）
 *   - "prod"  生产环境（真实生产数据库）
 *
 * 使用方式：
 *   1. 修改本文件的 @Profile("dev") 为目标环境
 *   2. 重启项目即可
 *
 * 注意：每个环境的详细配置在 application-{环境名}.yml 文件中
 */
@Configuration
@Profile("dev")  // ← ⚠️ 修改这里切换环境：dev | test | prod
public class EnvConfig {

    /**
     * 当前环境标识
     * 用于日志或监控中标识环境
     */
    public static final String ENVIRONMENT = "dev";

    /**
     * 初始化提示
     * 项目启动时会打印当前环境信息
     */
    public void init() {
        System.out.println("===========================================");
        System.out.println("当前环境: " + ENVIRONMENT);
        System.out.println("数据库配置: application-" + ENVIRONMENT + ".yml");
        System.out.println("API Base URL: http://localhost:8080/api");
        System.out.println("用户登录:    POST /api/user/login");
        System.out.println("用户注册:    POST /api/user/register");
        System.out.println("用户信息:    GET  /api/user/info");
        System.out.println("余额充值:    POST /api/userBalance/recharge");
        System.out.println("余额消费:    POST /api/userBalance/consume");
        System.out.println("余额冻结:    POST /api/userBalance/freeze");
        System.out.println("余额解冻:    POST /api/userBalance/unfreeze");
        System.out.println("余额转账:    POST /api/userBalance/transfer");
        System.out.println("余额退款:    POST /api/userBalance/refund");
        System.out.println("发送验证码:  POST /api/authSms/send");
        System.out.println("短信登录:    POST /api/authLogin/sms");
        System.out.println("消费明细:    POST /api/userDailyConsumeDetail");
        System.out.println("接口文档:    浏览器访问 /api/doc.html");
        System.out.println("===========================================");
    }
}
