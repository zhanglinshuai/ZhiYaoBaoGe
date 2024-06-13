package com.example.demos.Param;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;

@Data
public class OrderAddRequestParam implements Serializable {

    private static final long serialVersionUID = -7365635560561866722L;

    /**
     * 订单用户账号
     */
    @TableField(value = "user_account")
    private String userAccount;

    /**
     * 订单用户名称
     */
    @TableField(value = "user_name")
    private String userName;

    /**
     * 药品名称
     */
    @TableField(value = "medicine_name")
    private String medicineName;

    /**
     * 购买药品数量
     */
    @TableField(value = "shopping_number")
    private Integer shoppingNumber;

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
     * 药品规格
     */
    @TableField(value = "medicine_specification")
    private String medicineSpecification;

    /**
     * 药品生产厂家
     */
    @TableField(value = "medicine_manufacturer")
    private String medicineManufacturer;
}
