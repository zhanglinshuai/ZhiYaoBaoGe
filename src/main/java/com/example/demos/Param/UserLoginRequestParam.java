package com.example.demos.Param;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户登录请求参数封装体
 */
@Data
public class UserLoginRequestParam implements Serializable {

    private static final long serialVersionUID = 6146386496704961214L;

    private String userAccount;
    private String userPassword;
}
