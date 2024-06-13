package com.example.demos.service;

import com.example.demos.Param.UserLoginRequestParam;
import com.example.demos.Param.UserRegisterRequestParam;
import com.example.demos.dto.UserDto;
import com.example.demos.pojo.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author 86175
* @description 针对表【user(用户表)】的数据库操作Service
* @createDate 2024-06-11 15:35:42
*/
public interface UserService extends IService<User> {

    /**
     * 用户注册
     * @param userRegisterRequestParam
     * @return
     */
    int UserRegister(UserRegisterRequestParam userRegisterRequestParam);

    /**
     * 获取脱敏后的用户信息
     * @param originUser
     * @return
     */
    User getSafetyUser(User originUser);

    /**
     * 用户登录
     * @param userLoginRequestParam
     * @return
     */
    User UserLogin(UserLoginRequestParam userLoginRequestParam, HttpServletRequest request);

    /**
     * 获取当前登录用户信息
     * @param request
     * @return
     */
    User getUserInfo(HttpServletRequest request);

    /**
     * 更新用户信息
     * @param request
     * @return
     */
    boolean updateUserInfo(String userAccount,UserDto userDto,HttpServletRequest request);

    /**
     * 获取用户列表
     * @param request
     * @return
     */
    List<User> getUserList(HttpServletRequest request);

    /**
     * 判断用户是否为管理员
     * @param request
     * @return
     */
    boolean isAdmin(HttpServletRequest request);

    /**
     * 根据用户账号删除用户
     * @param userAccount
     * @return
     */
    boolean deleteUser(String userAccount);

    /**
     * 根据用户账号修改密码
     * @param userAccount
     * @param newPassword
     * @param request
     * @return
     */
    boolean updatePassword(String userAccount,String newPassword,HttpServletRequest request,String userPassword);

}
