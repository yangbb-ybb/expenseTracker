package com.example.common.exception;

import com.example.common.result.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 *
 * 作用：捕获项目中所有未处理的异常，转换为统一的 JSON 格式返回给前端
 *
 * @RestControllerAdvice = @ControllerAdvice + @ResponseBody
 *                        表示这是一个全局异常处理类
 *
 * 异常处理顺序：
 *   1. BusinessException - 业务异常（我们自己定义的）
 *   2. MethodArgumentNotValidException - 参数校验失败（@Valid 注解生效）
 *   3. BindException - 参数绑定失败
 *   4. Exception - 其他所有未处理的异常
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 处理业务异常
     * @param e 业务异常对象
     * @return 统一格式的响应，code 为异常的 code 字段
     */
    @ExceptionHandler(BusinessException.class)
    public Result<?> handleBusinessException(BusinessException e) {
        return Result.error(e.getCode(), e.getMessage());
    }

    /**
     * 处理参数校验失败异常（@Valid 校验不通过时触发）
     * @param e 参数校验异常
     * @return 统一格式的响应，code=400
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<?> handleValidException(MethodArgumentNotValidException e) {
        // 获取第一个校验失败的错误信息
        String message = e.getBindingResult().getFieldError().getDefaultMessage();
        return Result.error(400, message);
    }

    /**
     * 处理参数绑定失败异常
     * @param e 绑定异常
     * @return 统一格式的响应，code=400
     */
    @ExceptionHandler(BindException.class)
    public Result<?> handleBindException(BindException e) {
        String message = e.getBindingResult().getFieldError().getDefaultMessage();
        return Result.error(400, message);
    }

    /**
     * 处理所有未捕获的异常（兜底处理）
     * @param e 异常对象
     * @return 统一格式的响应，code=500
     */
    @ExceptionHandler(Exception.class)
    public Result<?> handleException(Exception e) {
        // 记录异常堆栈到日志文件，方便排查问题
        log.error("系统异常: {}", e.getMessage(), e);
        return Result.error("系统异常，请稍后重试");
    }
}
