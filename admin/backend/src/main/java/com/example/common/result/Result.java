package com.example.common.result;

/**
 * 统一响应结果类
 *
 * 作用：所有接口返回统一格式，方便前端处理
 *
 * @param <T> 返回数据的类型
 *
 * 统一格式：
 * {
 *   "code": 200,        // 状态码，200=成功，其他=失败
 *   "message": "操作成功", // 提示信息
 *   "data": {...}       // 返回的数据
 * }
 */
public class Result<T> {

    /** 状态码：200=成功，500=系统错误，401=未授权，400=参数错误 */
    private int code;

    /** 提示信息：成功时返回"操作成功"，失败时返回具体错误信息 */
    private String message;

    /** 返回的数据，可以是任意类型（用户信息、列表、分页等） */
    private T data;

    // ========== Getter 和 Setter 方法 ==========

    public int getCode() { return code; }
    public void setCode(int code) { this.code = code; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public T getData() { return data; }
    public void setData(T data) { this.data = data; }

    // ========== 静态方法：快速构建响应结果 ==========

    /**
     * 成功响应，无返回数据
     * @param <T> 类型
     * @return Result 对象，code=200
     */
    public static <T> Result<T> success() {
        return success(null);
    }

    /**
     * 成功响应，有返回数据
     * @param data 返回的数据
     * @param <T> 类型
     * @return Result 对象，code=200，message="操作成功"
     */
    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMessage("操作成功");
        result.setData(data);
        return result;
    }

    /**
     * 错误响应，默认状态码 500
     * @param message 错误信息
     * @param <T> 类型
     * @return Result 对象，code=500
     */
    public static <T> Result<T> error(String message) {
        return error(500, message);
    }

    /**
     * 错误响应，自定义状态码
     * @param code 状态码
     * @param message 错误信息
     * @param <T> 类型
     * @return Result 对象
     */
    public static <T> Result<T> error(int code, String message) {
        Result<T> result = new Result<>();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }
}
