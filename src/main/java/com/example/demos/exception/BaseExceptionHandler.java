package com.example.demos.exception;

import com.example.demos.Error.ErrorCode;

/**
 * 自定义异常类
 */
public class BaseExceptionHandler extends RuntimeException {

    private int code;
    private String description;

    public BaseExceptionHandler(String message, int code, String description) {
        super(message);
        this.code = code;
        this.description = description;
    }
    public BaseExceptionHandler(ErrorCode errorCode){
        super(errorCode.getMessage());
        this.code= errorCode.getCode();
        this.description= errorCode.getDescription();
    }
    public BaseExceptionHandler(ErrorCode errorCode,String description){
        super(errorCode.getMessage());
        this.code=errorCode.getCode();
        this.description= description;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
