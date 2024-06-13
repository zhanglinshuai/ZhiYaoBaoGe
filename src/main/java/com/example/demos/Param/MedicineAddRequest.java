package com.example.demos.Param;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;

/**
 * 添加药品参数请求体
 */
@Data
public class MedicineAddRequest implements Serializable {

    private static final long serialVersionUID = -5317796704562839610L;

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
