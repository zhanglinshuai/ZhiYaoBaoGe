package com.example.demos.controller;

import com.example.demos.Param.MedicineAddRequest;
import com.example.demos.Param.MedicineUpdateRequest;
import com.example.demos.dto.MedicineDto;
import com.example.demos.error.BaseResponse;
import com.example.demos.error.ErrorCode;
import com.example.demos.error.ResultUtils;
import com.example.demos.exception.BaseExceptionHandler;
import com.example.demos.pojo.Medicine;
import com.example.demos.service.MedicineService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/medicine")
@CrossOrigin(value = "http://localhost:5173", allowCredentials = "true")
public class MedicineController {

    @Resource
    private MedicineService medicineService;

    @PostMapping("/add")
    public BaseResponse<Integer> addMedicine( MedicineAddRequest medicineAddRequest){
        if (medicineAddRequest==null){
            throw new BaseExceptionHandler(ErrorCode.PARAMS_ERROR,"参数为空");
        }
        //调用业务
        int medicineId = medicineService.addMedicine(medicineAddRequest);
        if (medicineId<=0){
            throw new BaseExceptionHandler(ErrorCode.PARAMS_ERROR,"药品信息错误");
        }
        return ResultUtils.success(medicineId);

    }

    @GetMapping("/list")
    public BaseResponse<List<Medicine>> getMedicineList(HttpServletRequest request){
        if (request==null){
            throw new BaseExceptionHandler(ErrorCode.PARAMS_ERROR,"没有请求");
        }
        //调用业务
        List<Medicine> medicineList = medicineService.getMedicineList(request);
        if (medicineList.isEmpty()){
            throw new BaseExceptionHandler(ErrorCode.PARAMS_ERROR,"药品列表为空");
        }
        return ResultUtils.success(medicineList);
    }
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteMedicine(String medicineName,String medicineSpecification,String medicineManufacturer){
        if (medicineName==null){
            throw new BaseExceptionHandler(ErrorCode.PARAMS_ERROR,"该药品名称为空");
        }
        if (medicineSpecification==null){
            throw new BaseExceptionHandler(ErrorCode.PARAMS_ERROR,"该药品规格为空");
        }
        if (medicineManufacturer==null){
            throw new BaseExceptionHandler(ErrorCode.PARAMS_ERROR,"该药品生产厂家为空");
        }
        //调用业务
        boolean result = medicineService.deleteMedicine(medicineName, medicineSpecification,medicineManufacturer);
        if (!result){
            throw new BaseExceptionHandler(ErrorCode.SYSTEM_ERROR,"删除异常");
        }
        return ResultUtils.success(true);
    }
    @Transactional
    @PostMapping("/update")
    public BaseResponse<Boolean> updateMedicine(@RequestBody MedicineUpdateRequest medicineUpdateRequest){
        if (medicineUpdateRequest==null){
            throw new BaseExceptionHandler(ErrorCode.PARAMS_ERROR,"参数为空");
        }
        String medicineName = medicineUpdateRequest.getMedicineName();
        String medicineSpecification = medicineUpdateRequest.getMedicineSpecification();
        MedicineDto medicineDto = medicineUpdateRequest.getMedicineDto();
        String medicineManufacturer = medicineUpdateRequest.getMedicineManufacturer();
        if (medicineDto==null){
            throw new BaseExceptionHandler(ErrorCode.PARAMS_ERROR,"传入的药品信息为空");
        }
        if (medicineName==null){
            throw new BaseExceptionHandler(ErrorCode.PARAMS_ERROR,"该药品名称为空");
        }
        if (medicineSpecification==null){
            throw new BaseExceptionHandler(ErrorCode.PARAMS_ERROR,"该药品规格为空");
        }
        if (medicineManufacturer==null){
            throw new BaseExceptionHandler(ErrorCode.PARAMS_ERROR,"该药品生产厂家为空");
        }
        //调用业务
        boolean result = medicineService.updateMedicine(medicineName, medicineSpecification,medicineDto,medicineManufacturer);
        if (!result){
            throw new BaseExceptionHandler(ErrorCode.SYSTEM_ERROR,"更新失败");
        }
        return ResultUtils.success(true);
    }

    @GetMapping("/select")
    public BaseResponse<List<Medicine>> selectMedicineList(String medicineName){
        if (medicineName==null){
            throw new BaseExceptionHandler(ErrorCode.PARAMS_ERROR,"药品名称为空");
        }
        if (medicineName.isEmpty()){
            List<Medicine> list = medicineService.list();
            return ResultUtils.success(list);
        }
        //调用业务
        List<Medicine> medicineList = medicineService.selectMedicineByName(medicineName);
        if (medicineList.isEmpty()){
            return ResultUtils.success(new ArrayList<>());
        }
        return ResultUtils.success(medicineList);
    }
}
