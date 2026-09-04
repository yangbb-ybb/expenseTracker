# 后端项目 (backend)

企业级 Spring Boot 3.2 + MyBatis Plus 后端服务

---

## 目录结构

```
backend/
├── sql/
│   └── init.sql                          # 数据库初始化脚本
├── src/main/java/com/example/
│   ├── BackendApplication.java           # 【启动类】Spring Boot 入口
│   ├── common/
│   │   ├── aspect/
│   │   │   └── ControllerLogAspect.java  # 【AOP】接口日志切面（自动记录每个接口）
│   │   ├── exception/
│   │   │   ├── BusinessException.java    # 【异常】自定义业务异常
│   │   │   └── GlobalExceptionHandler.java # 【异常】全局异常处理器
│   │   └── result/
│   │       └── Result.java               # 【响应】统一响应格式
│   ├── config/
│   │   ├── CorsConfig.java               # 【配置】跨域配置
│   │   ├── JwtInterceptor.java           # 【配置】JWT 认证拦截器（自动续期）
│   │   ├── MybatisPlusConfig.java        # 【配置】MyBatis Plus 自动填充
│   │   └── WebMvcConfig.java             # 【配置】Web MVC 配置（注册拦截器）
│   ├── controller/
│   │   └── UserController.java           # 【接口】用户相关接口
│   ├── entity/
│   │   ├── dto/                         # 【实体】数据传输对象（接收参数）
│   │   │   ├── UserLoginDTO.java
│   │   │   └── UserRegisterDTO.java
│   │   ├── po/                          # 【实体】持久化对象（对应数据库表）
│   │   │   └── User.java
│   │   └── vo/                          # 【实体】视图对象（返回数据）
│   │       └── UserInfoVO.java
│   ├── repository/
│   │   └── UserRepository.java          # 【数据层】数据库操作（不用写 SQL）
│   ├── service/
│   │   ├── UserService.java             # 【服务】服务接口
│   │   └── impl/
│   │       └── UserServiceImpl.java     # 【服务】服务实现
│   └── util/
│       └── JwtUtil.java                 # 【工具】JWT Token 生成和解析
├── src/main/resources/
│   ├── application.yml                   # 【配置】Spring Boot 配置文件
│   └── logback-spring.xml               # 【配置】日志配置文件
└── pom.xml                              # 【配置】Maven 项目配置
```

---

## 核心概念解释

### 1. 分层架构

```
浏览器请求
    ↓
┌─────────────────────────────────────────┐
│           Controller（控制器层）           │
│   接收请求、参数校验、调用 Service         │
└─────────────────────────────────────────┘
    ↓
┌─────────────────────────────────────────┐
│           Service（服务层）               │
│   业务逻辑处理                           │
└─────────────────────────────────────────┘
    ↓
┌─────────────────────────────────────────┐
│         Repository（数据层）               │
│   数据库操作（MyBatis Plus 提供）        │
└─────────────────────────────────────────┘
    ↓
        MySQL 数据库
```

### 2. DTO / PO / VO 的区别

| 概念 | 全称 | 作用 | 方向 |
|------|------|------|------|
| DTO | Data Transfer Object | 接收前端参数 | 前端 → 后端 |
| PO | Persistent Object | 对应数据库表 | 数据库 |
| VO | View Object | 返回给前端 | 后端 → 前端 |

```
前端 POST /user/login     ← UserLoginDTO（只有用户名密码）
         ↓
Service 处理
         ↓
数据库 sys_user 表        ← User PO（完整字段）
         ↓
前端 GET /user/info       ← UserInfoVO（没有密码字段）
```

### 3. AOP 切面编程

AOP = Aspect Oriented Programming（面向切面编程）

作用：在不修改原有代码的情况下，给方法添加额外功能

本项目使用场景：**接口日志记录**

```
┌────────────────────────────────────┐
│     ControllerLogAspect（切面）      │
│                                    │
│  @Around("controllerPointcut()")   │
│  自动拦截所有 Controller 接口         │
│                                    │
│  记录：IP、参数、耗时、返回          │
└────────────────────────────────────┘
         ↓ 拦截 ↓ 拦截
┌────────┐  ┌────────┐  ┌────────┐
│UserCtrl│  │DemoCtrl│  │OrderCtrl│
└────────┘  └────────┘  └────────┘
```

---

## 文件详解

---

### sql/init.sql

**作用**：初始化数据库和表结构

```sql
CREATE DATABASE backend;           -- 创建数据库
CREATE TABLE sys_user (...);       -- 创建用户表
INSERT INTO sys_user (...);        -- 插入测试数据
```

**执行方式**：
```bash
mysql -u root --socket=/tmp/my.sock < sql/init.sql
```

---

### BackendApplication.java

**作用**：Spring Boot 应用程序的启动入口

```java
@SpringBootApplication          // 标记为 Spring Boot 应用
@MapperScan("com.example.repository")  // 扫描 Mapper 接口
public class BackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }
}
```

**启动后**：
- 自动扫描并注册所有 Bean
- 启动内嵌 Tomcat 服务器
- 监听配置的端口（默认 8080）

---

### application.yml

**作用**：Spring Boot 全局配置文件

```yaml
server:
  port: 8080                    # 端口号
  servlet:
    context-path: /api          # 项目访问路径前缀

spring:
  datasource:                   # 数据库连接
    url: jdbc:mysql://...
    username: root
    password:
  data:
    redis:                      # Redis 配置（预留）
      host: localhost
      port: 6379

jwt:                            # JWT 配置
  validity: 604800000           # Token 有效期（7天）
  renewal-threshold: 3600000    # 自动续期阈值（1小时）

mybatis-plus:                   # MyBatis Plus 配置
  configuration:
    map-underscore-to-camel-case: true  # 下划线转驼峰
```

---

### logback-spring.xml

**作用**：日志配置文件

```xml
<!-- 日志格式：时间 [线程] 级别 类名 - 消息 -->
%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n

<!-- 输出到控制台 -->
<appender name="CONSOLE" ...>

<!-- 输出到文件（按天归档，保留30天） -->
<appender name="FILE" ...>

<!-- 错误日志单独输出 -->
<appender name="ERROR_FILE" ...>
```

**日志文件位置**：
```
logs/backend.log      # 所有日志
logs/error.log       # 只有 ERROR 级别
```

---

### Result.java

**作用**：统一接口响应格式

**为什么需要**：所有接口返回格式统一，前端好处理

```java
{
  "code": 200,              // 状态码：200=成功，其他=失败
  "message": "操作成功",    // 提示信息
  "data": {...}             // 返回的数据
}
```

**使用示例**：
```java
return Result.success();              // 成功，无数据
return Result.success(userInfo);        // 成功，有数据
return Result.error("用户名已存在");    // 失败
return Result.error(401, "未授权");    // 失败，自定义状态码
```

---

### BusinessException.java

**作用**：自定义业务异常

**为什么需要**：业务错误时抛出明确异常，而不是笼统的 Exception

```java
throw new BusinessException("用户不存在");           // 默认 500
throw new BusinessException(401, "密码错误");       // 自定义状态码
```

---

### GlobalExceptionHandler.java

**作用**：捕获所有未处理的异常，转换为统一格式返回

```java
@RestControllerAdvice  // 拦截所有 Controller 的异常
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public Result<?> handleBusinessException(BusinessException e) {
        return Result.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public Result<?> handleException(Exception e) {
        return Result.error("系统异常，请稍后重试");
    }
}
```

**处理流程**：
```
接口抛异常
    ↓
GlobalExceptionHandler 捕获
    ↓
返回统一格式 {code, message, data}
```

---

### CorsConfig.java

**作用**：解决跨域问题

**什么是跨域**：浏览器安全策略，禁止不同源之间的请求

```java
@Configuration
public class CorsConfig {
    @Bean
    public CorsFilter corsFilter() {
        // 允许所有来源、请求头、方法
        config.addAllowedOriginPattern("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
    }
}
```

---

### JwtInterceptor.java

**作用**：JWT Token 验证和自动续期

**滑动过期机制**：
- Token 有效期：7 天
- 续期阈值：1 小时
- 用户每次请求时，如果 Token 剩余时间 < 1 小时，自动续期 7 天

```java
@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, ...) {
        String token = request.getHeader("Authorization");

        // 解析 Token
        Long userId = JwtUtil.parseToken(token);

        // 检查是否快过期
        if (remainingTime < renewalThreshold) {
            // 生成新 Token 返回给前端
            String newToken = JwtUtil.generateToken(userId);
            response.setHeader("Authorization", "Bearer " + newToken);
        }

        return true;  // 放行
    }
}
```

---

### WebMvcConfig.java

**作用**：配置 Web MVC，包括注册拦截器

```java
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/**")  // 拦截所有请求
                .excludePathPatterns("/user/login", "/user/register");  // 排除这些
    }
}
```

---

### MybatisPlusConfig.java

**作用**：自动填充创建时间和更新时间

```java
@Component
public class MybatisPlusConfig implements MetaObjectHandler {

    // 新增时自动填充
    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
        this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
    }

    // 修改时自动填充
    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
    }
}
```

**使用效果**：
```java
userRepository.insert(user);  // 自动填充 createTime 和 updateTime
userRepository.updateById(user);  // 自动填充 updateTime
```

---

### ControllerLogAspect.java

**作用**：AOP 切面自动记录接口日志

```java
@Aspect
@Component
public class ControllerLogAspect {

    @Pointcut("execution(* com.example.controller..*.*(..))")
    public void controllerPointcut() {}

    @Around("controllerPointcut()")
    public Object around(ProceedingJoinPoint joinPoint) {
        // 请求前：记录 IP、参数、方法名
        log.info("请求 IP: {}", ip);
        log.info("请求参数: {}", params);

        // 执行方法
        Object result = joinPoint.proceed();

        // 请求后：记录耗时、返回
        log.info("请求耗时: {} ms", costTime);
        log.info("返回结果: {}", result);

        return result;
    }
}
```

**日志输出示例**：
```
========== 接口请求开始 ==========
请求时间: 2026-04-29 18:00:04
请求 IP: 127.0.0.1
请求方法: GET /api/user/info
类名方法: UserController.getUserInfo
请求参数: {"userId": 1}
请求耗时: 82 ms
返回结果: {"code":200,...}
========== 接口请求结束 ==========
```

---

### UserController.java

**作用**：用户接口（登录、注册、获取信息）

```java
@RestController
@RequestMapping("/user")
public class UserController {

    @PostMapping("/login")
    public Result<String> login(@RequestBody UserLoginDTO dto) {
        String token = userService.login(dto);
        return Result.success(token);
    }

    @GetMapping("/info")
    public Result<UserInfoVO> getUserInfo(@RequestParam Long userId) {
        return Result.success(userService.getUserInfo(userId));
    }
}
```

---

### UserService.java / UserServiceImpl.java

**作用**：用户业务逻辑处理

```java
public interface UserService {
    String login(UserLoginDTO dto);
    void register(UserRegisterDTO dto);
    UserInfoVO getUserInfo(Long userId);
}

@Service
public class UserServiceImpl implements UserService {

    @Override
    public String login(UserLoginDTO dto) {
        // 1. 查询用户
        User user = userRepository.selectOne(...);

        // 2. 校验密码
        if (!password.equals(user.getPassword())) {
            throw new BusinessException(401, "密码错误");
        }

        // 3. 生成 Token
        return JwtUtil.generateToken(user.getId());
    }
}
```

---

### UserRepository.java

**作用**：数据库操作层

**为什么不用写 SQL**：MyBatis Plus 提供了基础 CRUD 方法

```java
@Mapper
public interface UserRepository extends BaseMapper<User> {
    // 不用写！MyBatis Plus 自动提供：
    // selectById()、selectList()、insert()、updateById()、deleteById()
}

// 使用示例
userRepository.selectById(1);           // 根据 ID 查询
userRepository.selectOne(wrapper);      // 条件查询
userRepository.insert(user);            // 新增
userRepository.updateById(user);        // 修改
userRepository.deleteById(1);          // 删除
```

---

### User.java（PO）

**作用**：对应数据库 `sys_user` 表

```java
@TableName("sys_user")  // 告诉 MyBatis Plus 对应哪张表
public class User {
    private Long id;
    private String username;
    private String password;
    private String nickname;
    // ... 更多字段
}
```

---

### UserLoginDTO.java / UserRegisterDTO.java（DTO）

**作用**：接收前端登录/注册参数

```java
public class UserLoginDTO {
    private String username;  // 必填
    private String password;  // 必填
}
```

---

### UserInfoVO.java（VO）

**作用**：返回给前端的用户信息

```java
public class UserInfoVO {
    private Long id;
    private String username;
    private String nickname;
    // 注意：没有 password 字段！不会泄露密码
}
```

---

### JwtUtil.java

**作用**：生成和解析 JWT Token

```java
public class JwtUtil {

    // 生成 Token（默认 7 天有效期）
    public static String generateToken(Long userId) {
        return Jwts.builder()
                .subject(String.valueOf(userId))  // 存储用户 ID
                .issuedAt(new Date())            // 签发时间
                .expiration(new Date(...))       // 过期时间
                .signWith(...)                   // 签名
                .compact();
    }

    // 解析 Token，获取用户 ID
    public static Long parseToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(...)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return Long.parseLong(claims.getSubject());
    }
}
```

---

## 快速开始

### 1. 启动 MySQL

```bash
mysql -u root --socket=/tmp/my.sock
```

### 2. 初始化数据库

```bash
mysql -u root --socket=/tmp/my.sock < sql/init.sql
```

### 3. 启动后端

```bash
cd admin/backend
mvn spring-boot:run
```

### 4. 访问接口文档

```
http://localhost:8080/api/doc.html
```

### 5. 测试接口

```bash
# 获取用户信息
curl http://localhost:8080/api/user/info?userId=1

# 登录
curl -X POST http://localhost:8080/api/user/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"123456"}'
```

---

## 多环境配置切换

### ⚠️ 接手项目必看：如何切换环境

本项目支持三套环境，**修改以下任意一处即可切换**：

#### 方式一：在 `application.yml` 中修改（推荐）

```yaml
# =====================================================
#  环境配置（修改这里切换环境）
# =====================================================
# 可选值：dev | test | prod
spring:
  profiles:
    active: dev  # ← 修改这里
# =====================================================
```

#### 方式二：在 `EnvConfig.java` 中修改

```java
@Configuration
@Profile("dev")  // ← 修改这里：dev | test | prod
public class EnvConfig {
```

### 三套环境说明

| 环境 | 配置文件 | 数据库 | 用途 |
|------|---------|--------|------|
| `dev` | `application-dev.yml` | 本地 `127.0.0.1:3307` | 本地开发 |
| `test` | `application-test.yml` | 测试服务器 | 测试验收 |
| `prod` | `application-prod.yml` | 真实生产数据库 | 正式上线 |

### 各环境配置位置

```
src/main/resources/
├── application.yml              # 主配置 + 环境指定
├── application-dev.yml        # 开发环境
├── application-test.yml         # 测试环境
└── application-prod.yml       # 生产环境
```

### 启动命令

```bash
# 默认使用 application.yml 中指定的环境
mvn spring-boot:run

# 也可以命令行指定环境（会覆盖配置文件）
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=prod"
```

### 生产环境密码

生产环境密码从**环境变量**读取，不写死在配置文件中：

```yaml
# application-prod.yml
password: ${MYSQL_PASSWORD}      # 运行时：export MYSQL_PASSWORD=xxx
password: ${REDIS_PASSWORD}      # 运行时：export REDIS_PASSWORD=xxx
```

---

## 项目配置文件说明

| 文件 | 作用 |
|------|------|
| `pom.xml` | Maven 依赖和项目信息 |
| `application.yml` | Spring Boot 运行配置 |
| `application-dev/test/prod.yml` | 各环境数据库配置 |
| `EnvConfig.java` | 环境配置类（明确指定当前环境） |
| `logback-spring.xml` | 日志配置 |
| `init.sql` | 数据库初始化 SQL |

---

## 常见问题

### Q: 为什么要有 DTO、PO、VO 这么多实体类？

**A**: 分离关注点，清晰分层
- DTO：接收参数（前端传什么我就收什么）
- PO：数据库映射（一对一对应表）
- VO：返回数据（只返回前端需要的）

### Q: 为什么 Controller 里不写业务逻辑？

**A**: 职责分离
- Controller：接收请求、参数校验、调用 Service
- Service：业务逻辑处理
- Repository：数据访问

这样易于测试和维护。

### Q: AOP 切面是什么？

**A**: 切面 = 拦截器 + 额外逻辑

比如 `ControllerLogAspect`：
- 自动拦截所有 Controller 方法
- 在方法前后添加日志记录
- 不需要修改任何 Controller 代码
