package com.example.demos.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
@Data
public class MedicineDto implements Serializable {

    private static final long serialVersionUID = 8440584817208801101L;

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
}
