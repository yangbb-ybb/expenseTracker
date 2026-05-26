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
        System.out.println("===========================================");
    }
}
