package com.example.demos.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 药品表
 * @TableName medicine
 */
@TableName(value ="medicine")
@Data
public class Medicine implements Serializable {
    /**
     * 药品id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 药品名称
     */
    @TableField(value = "medicine_name")
    private String medicineName;

    /**
     * 药品使用说明
     */
    @TableField(value = "medicine_description")
    private String medicineDescription;

    /**
     * 药品价格
     */
    @TableField(value = "medicine_price")
    private int medicinePrice;
    /**
     * 药品库存量
     */
    @TableField(value = "medicine_inventory")
    private String medicineInventory;

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