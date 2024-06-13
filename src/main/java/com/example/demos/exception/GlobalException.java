package com.example.demos.exception;


import com.example.demos.error.BaseResponse;
import com.example.demos.error.ErrorCode;
import com.example.demos.error.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 */
@RestControllerAdvice
@Slf4j
public class GlobalException {
    @ExceptionHandler(BaseExceptionHandler.class)
    public <T> BaseResponse<T> baseExceptionHandler(BaseExceptionHandler e) {
        return ResultUtils.fail(e.getCode(), e.getMessage(), e.getDescription());
    }
    @ExceptionHandler(RuntimeException.class)
    public <T> BaseResponse<T> RuntimeExceptionHandler(BaseExceptionHandler e) {
        return ResultUtils.fail(ErrorCode.SYSTEM_ERROR,"", e.getDescription());
    }
}
