package com.example;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Boot 启动类
 *
 * 作用：启动整个后端应用程序
 *
 * @SpringBootApplication = @Configuration + @EnableAutoConfiguration + @ComponentScan
 *                          表示这是一个 Spring Boot 应用
 *
 * @MapperScan 指定扫描哪个包下的 Mapper 接口
 *             扫描到后，MyBatis 会自动为这些接口创建实现类
 */
@SpringBootApplication
@MapperScan("com.example.repository")
public class BackendApplication {

    /**
     * 应用启动入口
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        // 启动 Spring Boot 应用
        SpringApplication.run(BackendApplication.class, args);
    }
}
