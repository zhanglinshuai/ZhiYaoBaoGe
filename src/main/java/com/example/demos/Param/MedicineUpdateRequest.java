package com.example.demos.Param;

import com.example.demos.dto.MedicineDto;
import lombok.Data;

import java.io.Serializable;

/**
 * 药品信息修改请求体
 */
@Data
public class MedicineUpdateRequest implements Serializable {

    private static final long serialVersionUID = -8473604748067688450L;

    private String medicineName;

    private String medicineSpecification;

    private String medicineManufacturer;

    private MedicineDto medicineDto;
}
