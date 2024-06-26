package com.example.demos.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * 更改用户信息对象
 */
@Data
public class UserDto implements Serializable {

    private static final long serialVersionUID = 9197258338521708086L;
    /**
     * 用户id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;


    /**
     * 用户名
     */
    @TableField(value = "user_name")
    private String userName;

    /**
     * 用户账号
     */
    @TableField(value = "user_account")
    private String userAccount;

    /**
     * 用户密码
     */
    @TableField(value = "user_password")
    private String userPassword;

    /**
     * 用户手机号
     */
    @TableField(value = "user_phone")
    private String userPhone;
    /**
     * 用户年龄
     */
    @TableField(value = "user_age")
    private int userAge;
    /**
     * 用户性别
     */
    @TableField(value = "user_gender")
    private int userGender;
}
