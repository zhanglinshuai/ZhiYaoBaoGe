package com.example.demos.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 用户表
 * @TableName user
 */
@TableName(value ="user")
@Data
public class User implements Serializable {
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
     * 用户身份 0 - 普通用户 1- 管理员
     */
    @TableField(value = "user_role")
    private Integer userRole;

    /**
     * 用户手机号
     */
    @TableField(value = "user_phone")
    private String userPhone;
    /**
     * 用户性别
     */
    @TableField(value = "user_age")
    private int userAge;
    /**
     * 用户性别
     */
    @TableField(value = "user_gender")
    private int userGender;

    /**
     * 是否删除 0-不删除  1- 删除
     */
    @TableField(value = "isDelete")
    private Integer isdelete;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}