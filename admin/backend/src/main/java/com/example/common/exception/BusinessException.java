package com.example.common.exception;

/**
 * 业务异常类
 *
 * 作用：当业务逻辑出错时抛出此异常
 * 例如：用户不存在、密码错误、余额不足等
 *
 * 使用场景：
 *   throw new BusinessException("用户名已存在");
 *   throw new BusinessException(401, "未授权访问");
 */
public class BusinessException extends RuntimeException {

    /** 异常状态码，默认 500（服务器内部错误） */
    private int code = 500;

    /**
     * 构造方法，只传入错误信息
     * @param message 错误信息
     */
    public BusinessException(String message) {
        super(message);  // 调用父类 RuntimeException 的构造方法
    }

    /**
     * 构造方法，传入状态码和错误信息
     * @param code 状态码，如 401=未授权，400=参数错误
     * @param message 错误信息
     */
    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    /**
     * 获取状态码
     * @return 状态码
     */
    public int getCode() {
        return code;
    }
}
