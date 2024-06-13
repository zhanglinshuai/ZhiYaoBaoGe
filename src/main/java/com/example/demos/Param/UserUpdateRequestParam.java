package com.example.demos.Param;

import com.example.demos.dto.UserDto;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户更新请求参数封装体
 */
@Data
public class UserUpdateRequestParam implements Serializable {

    private static final long serialVersionUID = -4149882831412904617L;

    private String userAccount;

    private UserDto userDto;

}
