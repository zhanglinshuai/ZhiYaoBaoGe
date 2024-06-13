package com.example.demos.Error;

/**
 * 封装通用返回对象工具类
 */
public class ResultUtils {
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(0,data,"返回成功");
    }
    public static <T> BaseResponse<T> fail(ErrorCode errorCode){
        return new BaseResponse<T>(errorCode);
    }
    public static <T> BaseResponse<T> fail(ErrorCode errorCode,String description){
        return new BaseResponse<T>(errorCode,description);
    }
    public static <T> BaseResponse<T> fail(ErrorCode errorCode,String message,String description){
        return new BaseResponse<T>(errorCode.getCode(),null,message,description);
    }
    public static <T> BaseResponse<T> fail(int code,String message,String description){
        return new BaseResponse<T>(code,null,message,description);
    }
}

