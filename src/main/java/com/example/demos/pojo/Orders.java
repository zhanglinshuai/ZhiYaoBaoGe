package com.example.demos.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 销售表
 * @TableName orders
 */
@TableName(value ="orders")
@Data
public class Orders implements Serializable {
    /**
     * 订单id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 订单编号
     */
    @TableField(value = "order_number")
    private Integer orderNumber;

    /**
     * 订单用户id
     */
    @TableField(value = "user_id")
    private Integer userId;

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
     * 药品id
     */
    @TableField(value = "medicine_id")
    private Integer medicineId;

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
     * 用户性别
     */
    @TableField(value = "user_gender")
    private Integer userGender;

    /**
     * 用户年龄
     */
    @TableField(value = "user_age")
    private Integer userAge;

    /**
     * 用户手机号
     */
    @TableField(value = "user_phone")
    private String userPhone;

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

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}