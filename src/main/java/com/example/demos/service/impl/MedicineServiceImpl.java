package com.example.demos.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demos.Param.MedicineAddRequest;
import com.example.demos.dto.MedicineDto;
import com.example.demos.error.ErrorCode;
import com.example.demos.exception.BaseExceptionHandler;
import com.example.demos.mapper.MedicineMapper;
import com.example.demos.pojo.Medicine;
import com.example.demos.service.MedicineService;
import com.example.demos.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 86175
 * @description 针对表【medicine(药品表)】的数据库操作Service实现
 * @createDate 2024-06-11 15:34:30
 */
@Service
public class MedicineServiceImpl extends ServiceImpl<MedicineMapper, Medicine>
        implements MedicineService {

    @Resource
    private MedicineMapper medicineMapper;

    @Resource
    private UserService userService;

    @Override
    public int addMedicine(MedicineAddRequest medicineAddRequest) {
        if (medicineAddRequest == null) {
            throw new BaseExceptionHandler(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        String medicineDescription = medicineAddRequest.getMedicineDescription();
        String medicineName = medicineAddRequest.getMedicineName();
        String medicineInventory = medicineAddRequest.getMedicineInventory();
        String medicineManufacturer = medicineAddRequest.getMedicineManufacturer();
        int medicinePrice = medicineAddRequest.getMedicinePrice();
        String medicineSpecification = medicineAddRequest.getMedicineSpecification();
        if (StringUtils.isAnyBlank(medicineDescription, medicineSpecification, medicineManufacturer, medicineName, medicineInventory)) {
            throw new BaseExceptionHandler(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        //参数长度判断
        if (medicineName.length() < 2 || medicineName.length() > 15) {
            throw new BaseExceptionHandler(ErrorCode.PARAMS_ERROR, "药品名称过长");
        }
        if (medicinePrice < 0) {
            throw new BaseExceptionHandler(ErrorCode.PARAMS_ERROR, "药品价格不符合要求");
        }
        //根据药品名称和规格和生产厂家判断是否重复
        QueryWrapper<Medicine> medicineQueryWrapper = new QueryWrapper<>();
        medicineQueryWrapper.eq("medicine_name", medicineName);
        medicineQueryWrapper.eq("medicine_specification", medicineSpecification);
        medicineQueryWrapper.eq("medicine_manufacturer", medicineManufacturer);
        long count = this.count(medicineQueryWrapper);
        if (count >= 1) {
            throw new BaseExceptionHandler(ErrorCode.PARAMS_ERROR, "药品已存在");
        }
        //将符合要求的药品添加到数据库中
        Medicine medicine = new Medicine();
        medicine.setMedicineName(medicineName);
        medicine.setMedicineDescription(medicineDescription);
        medicine.setMedicinePrice(medicinePrice);
        medicine.setMedicineSpecification(medicineSpecification);
        medicine.setMedicineManufacturer(medicineManufacturer);
        medicine.setMedicineInventory(medicineInventory);
        int insert = medicineMapper.insert(medicine);
        if (insert < 0) {
            throw new BaseExceptionHandler(ErrorCode.SYSTEM_ERROR, "插入失败");
        }
        return medicine.getId();
    }

    @Override
    public List<Medicine> getMedicineList(HttpServletRequest request) {
        if (request == null) {
            throw new BaseExceptionHandler(ErrorCode.PARAMS_ERROR, "用户未登录");
        }
        //如果用户不是管理员就没权限
        if (!userService.isAdmin(request)) {
            throw new BaseExceptionHandler(ErrorCode.NO_AUTH, "该用户无权限");
        }
        //获取药品列表
        List<Medicine> list = this.list();
        if (list.isEmpty()) {
            throw new BaseExceptionHandler(ErrorCode.PARAMS_ERROR, "没有药品");
        }
        return list;
    }

    @Override
    public boolean deleteMedicine(String medicineName, String medicineSpecification, String medicineManufacturer) {
        //参数为空判断
        if (medicineName == null) {
            throw new BaseExceptionHandler(ErrorCode.PARAMS_ERROR, "药品名称为空");
        }
        if (medicineSpecification == null) {
            throw new BaseExceptionHandler(ErrorCode.PARAMS_ERROR, "药品规格为空");
        }
        if (medicineManufacturer == null) {
            throw new BaseExceptionHandler(ErrorCode.PARAMS_ERROR, "药品生产厂家为空");
        }
        //根据medicine_name和medicine_specification共同查出唯一的medicine
        QueryWrapper<Medicine> medicineQueryWrapper = new QueryWrapper<>();
        medicineQueryWrapper.eq("medicine_name", medicineName);
        medicineQueryWrapper.eq("medicine_specification", medicineSpecification);
        medicineQueryWrapper.eq("medicine_manufacturer", medicineManufacturer);
        Medicine medicine = this.getOne(medicineQueryWrapper);
        if (medicine == null) {
            throw new BaseExceptionHandler(ErrorCode.PARAMS_ERROR, "该药品不存在");
        }
        //删除该药品
        boolean result = this.removeById(medicine.getId());
        if (!result) {
            throw new BaseExceptionHandler(ErrorCode.SYSTEM_ERROR, "删除异常");
        }
        return true;
    }

    @Override
    public boolean updateMedicine(String medicineName, String medicineSpecification, MedicineDto medicineDto, String medicineManufacturer) {
        //参数为空判断
        if (medicineName == null) {
            throw new BaseExceptionHandler(ErrorCode.PARAMS_ERROR, "药品名称为空");
        }
        if (medicineSpecification == null) {
            throw new BaseExceptionHandler(ErrorCode.PARAMS_ERROR, "药品规格为空");
        }
        if (medicineManufacturer == null) {
            throw new BaseExceptionHandler(ErrorCode.PARAMS_ERROR, "药品生产厂家为空");
        }
        if (medicineDto == null) {
            throw new BaseExceptionHandler(ErrorCode.PARAMS_ERROR, "传入的药品信息为空");
        }
        //根据medicine_name和medicine_specification,medicine_manufacturer共同查出唯一的medicine
        QueryWrapper<Medicine> medicineQueryWrapper = new QueryWrapper<>();
        medicineQueryWrapper.eq("medicine_name", medicineName);
        medicineQueryWrapper.eq("medicine_specification", medicineSpecification);
        medicineQueryWrapper.eq("medicine_manufacturer", medicineManufacturer);
        Medicine medicine = this.getOne(medicineQueryWrapper);
        //判断
        if (medicine == null) {
            throw new BaseExceptionHandler(ErrorCode.PARAMS_ERROR, "该药品不存在");
        }
        Integer id = medicine.getId();
        //将修改后的medicine_name,medicine_specification,medicine_manufacturer查询出来
        String newMedicineName = medicineDto.getMedicineName();
        String newMedicineSpecification = medicineDto.getMedicineSpecification();
        String newMedicineManufacturer = medicineDto.getMedicineManufacturer();
        //给medicine1设置上值
        Medicine medicine1 = new Medicine();
        medicine1.setMedicineName(medicine.getMedicineName());
        medicine1.setMedicineSpecification(medicine.getMedicineSpecification());
        medicine1.setMedicineManufacturer(medicine.getMedicineManufacturer());
        //根据值查询是否存在
        medicineQueryWrapper = new QueryWrapper<>();
        medicineQueryWrapper.eq("medicine_name", newMedicineName);
        medicineQueryWrapper.eq("medicine_specification", newMedicineSpecification);
        medicineQueryWrapper.eq("medicine_manufacturer", newMedicineManufacturer);
        Medicine newMedicine = this.getOne(medicineQueryWrapper);

        if (newMedicine == null) {
            //调用插入方法
            //将表格的值赋给newMedicine
            BeanUtils.copyProperties(medicineDto, medicine1);
        }
        //如果传来的药品和要修改的药品，不是同一个，不让修改,比较名称和规格和生产厂家
        if (medicine1.getMedicineName().equals(medicine.getMedicineName()) && medicine1.getMedicineManufacturer().equals(medicine.getMedicineManufacturer()) && medicine1.getMedicineSpecification().equals(medicine.getMedicineSpecification())) {
            //相等说明是同一个执行更新逻辑
            BeanUtils.copyProperties(medicineDto, medicine);
            //设置id，不然会更新失败
            medicine.setId(id);
            boolean insert = this.save(medicine1);
            if (!insert) {
                throw new BaseExceptionHandler(ErrorCode.PARAMS_ERROR, "插入失败");
            }
            if (medicine1.getId().equals(medicine.getId())) {
                throw new BaseExceptionHandler(ErrorCode.PARAMS_ERROR, "该药品已存在");
            }
            boolean result = this.updateById(medicine);
            if (!result) {
                throw new BaseExceptionHandler(ErrorCode.SYSTEM_ERROR, "更新异常");
            }
            boolean removeById = this.removeById(medicine1.getId());
            if (!removeById){
                throw new BaseExceptionHandler(ErrorCode.PARAMS_ERROR,"删除异常");
            }
            return true;
        }
        //如果不一样
        BeanUtils.copyProperties(medicineDto,medicine);
        medicine.setId(id);
        boolean result = this.updateById(medicine);
        if (!result) {
            throw new BaseExceptionHandler(ErrorCode.SYSTEM_ERROR, "更新异常");
        }
        return true;
    }

    @Override
    public List<Medicine> selectMedicineByName(String medicineName) {
        //参数判断
        if (medicineName == null) {
            throw new BaseExceptionHandler(ErrorCode.PARAMS_ERROR, "药品名称为空");
        }
        //根据药品名称查询药品
        QueryWrapper<Medicine> medicineQueryWrapper = new QueryWrapper<>();
        medicineQueryWrapper.eq("medicine_name", medicineName);
        List<Medicine> medicineList = this.list(medicineQueryWrapper);
        if (medicineList.isEmpty()) {
            return new ArrayList<>();
        }
        return medicineList;
    }
}




