package com.example.demos.service;

import com.example.demos.Param.MedicineAddRequest;
import com.example.demos.dto.MedicineDto;
import com.example.demos.pojo.Medicine;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author 86175
* @description 针对表【medicine(药品表)】的数据库操作Service
* @createDate 2024-06-11 15:34:30
*/
public interface MedicineService extends IService<Medicine> {
    /**
     * 添加药品
     * @param medicineAddRequest
     * @return
     */
    int addMedicine(MedicineAddRequest medicineAddRequest);

    /**
     * 获取药品列表
     * @param request
     * @return
     */
    List<Medicine> getMedicineList(HttpServletRequest request);

    /**
     * 删除药品
     * @param medicineName
     * @param medicineSpecification
     * @param medicineManufacturer
     * @return
     */
    boolean deleteMedicine(String medicineName,String medicineSpecification,String medicineManufacturer);

    /**
     * 更新药品
     * @param medicineName
     * @param medicineSpecification
     * @return
     */
    boolean updateMedicine(String medicineName, String medicineSpecification, MedicineDto medicineDto,String medicineManufacturer);

    /**
     * 根据药品名称搜索药品
     * @param medicineName
     * @return
     */
    List<Medicine> selectMedicineByName(String medicineName);

}
