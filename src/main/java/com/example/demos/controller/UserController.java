package com.example.demos.controller;

import com.example.demos.Param.UserLoginRequestParam;
import com.example.demos.Param.UserRegisterRequestParam;
import com.example.demos.Param.UserUpdateRequestParam;
import com.example.demos.dto.UserDto;
import com.example.demos.error.BaseResponse;
import com.example.demos.error.ErrorCode;
import com.example.demos.error.ResultUtils;
import com.example.demos.exception.BaseExceptionHandler;
import com.example.demos.pojo.User;
import com.example.demos.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/user")
@CrossOrigin(value = "http://localhost:5173", allowCredentials = "true")
public class UserController {

    @Resource
    private UserService userService;


    @PostMapping("/register")
    public BaseResponse<Integer> userRegister(@RequestBody UserRegisterRequestParam userRegisterRequestParam) {
        if (userRegisterRequestParam == null) {
            throw new BaseExceptionHandler(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        //调用业务
        int userId = userService.UserRegister(userRegisterRequestParam);
        if (userId <= 0) {
            throw new BaseExceptionHandler(ErrorCode.PARAMS_ERROR, "用户id错误");
        }
        return ResultUtils.success(userId);
    }

    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequestParam userLoginRequestParam, HttpServletRequest request){
        if (userLoginRequestParam==null){
            throw new BaseExceptionHandler(ErrorCode.PARAMS_ERROR,"参数为空");
        }
        //调用业务
        User user = userService.UserLogin(userLoginRequestParam,request);
        if (user==null){
            throw new BaseExceptionHandler(ErrorCode.PARAMS_ERROR,"用户为空");
        }
        return ResultUtils.success(user);
    }

    @GetMapping("/getUser")
    public BaseResponse<User> getUserInfo(HttpServletRequest request){
        if (request==null){
            throw new BaseExceptionHandler(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getUserInfo(request);
        if (user==null){
            throw new BaseExceptionHandler(ErrorCode.PARAMS_ERROR);
        }
        return ResultUtils.success(user);
    }
    @PostMapping("/update")
    public BaseResponse<Boolean> updateUserInfo(@RequestBody UserUpdateRequestParam userUpdateRequestParam, HttpServletRequest request){
        if (userUpdateRequestParam==null){
            throw new BaseExceptionHandler(ErrorCode.PARAMS_ERROR,"参数为空");
        }
        String userAccount = userUpdateRequestParam.getUserAccount();
        UserDto userDto = userUpdateRequestParam.getUserDto();
        if (userAccount==null){
            throw new BaseExceptionHandler(ErrorCode.PARAMS_ERROR);
        }
        if (userDto==null){
            throw new BaseExceptionHandler(ErrorCode.PARAMS_ERROR,"参数为空");
        }
        if (request==null){
            throw new BaseExceptionHandler(ErrorCode.PARAMS_ERROR,"参数为空");
        }
        //调用业务
        boolean result = userService.updateUserInfo(userAccount,userDto, request);
        if (!result){
            throw new BaseExceptionHandler(ErrorCode.SYSTEM_ERROR,"更新错误");
        }
        return ResultUtils.success(true);

    }

    @GetMapping("/list")
    public BaseResponse<List<User>> getUserList(HttpServletRequest request){
        if (request==null){
            throw new BaseExceptionHandler(ErrorCode.PARAMS_ERROR);
        }
        //调用业务
        List<User> userList = userService.getUserList(request);
        if (userList.isEmpty()){
            throw new BaseExceptionHandler(ErrorCode.PARAMS_ERROR);
        }
        return ResultUtils.success(userList);
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUser(String userAccount){
        if (userAccount==null){
            throw new BaseExceptionHandler(ErrorCode.PARAMS_ERROR);
        }
        //调用业务
        boolean result = userService.deleteUser(userAccount);
        if (!result){
            throw new BaseExceptionHandler(ErrorCode.PARAMS_ERROR);
        }
        return ResultUtils.success(true);
    }

    @PostMapping("/updatePassword")
    public BaseResponse<Boolean> updatePassword(HttpServletRequest request,String userAccount,String newPassword,String userPassword){
        if (request==null){
            throw new BaseExceptionHandler(ErrorCode.PARAMS_ERROR,"请求为空");
        }
        if (userAccount==null){
            throw new BaseExceptionHandler(ErrorCode.PARAMS_ERROR,"用户账号为空");
        }
        if (userPassword==null){
            throw new BaseExceptionHandler(ErrorCode.PARAMS_ERROR,"新密码为空");
        }
        //调用业务
        boolean result = userService.updatePassword(userAccount, newPassword, request,userPassword);
        if (!result){
            throw new BaseExceptionHandler(ErrorCode.SYSTEM_ERROR,"更新失败");
        }
        return ResultUtils.success(true);
    }

}
