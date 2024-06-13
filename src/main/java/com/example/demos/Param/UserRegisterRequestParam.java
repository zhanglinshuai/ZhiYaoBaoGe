package com.example.demos.Param;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户注册请求参数封装体
 */
@Data
public class UserRegisterRequestParam implements Serializable {

    private static final long serialVersionUID = -68830304922946056L;

    private String userAccount;
    private String userPassword;
    private String checkPassword;


}
