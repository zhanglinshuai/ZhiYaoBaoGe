package com.example.demos.service.impl;

import java.util.Date;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demos.Param.UserLoginRequestParam;
import com.example.demos.Param.UserRegisterRequestParam;
import com.example.demos.dto.UserDto;
import com.example.demos.error.ErrorCode;
import com.example.demos.exception.BaseExceptionHandler;
import com.example.demos.pojo.User;
import com.example.demos.service.UserService;
import com.example.demos.mapper.UserMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.example.demos.contants.UserConstants.*;

/**
 * @author 86175
 * @description 针对表【user(用户表)】的数据库操作Service实现
 * @createDate 2024-06-11 15:35:42
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public int UserRegister(UserRegisterRequestParam userRegisterRequestParam) {
        //参数非空判断
        if (userRegisterRequestParam == null) {
            throw new BaseExceptionHandler(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        String userPassword = userRegisterRequestParam.getUserPassword();
        String userAccount = userRegisterRequestParam.getUserAccount();
        String checkPassword = userRegisterRequestParam.getCheckPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            throw new BaseExceptionHandler(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        //参数规则判断
        if (userAccount.length() < 4 || userAccount.length() > 10) {
            throw new BaseExceptionHandler(ErrorCode.PARAMS_ERROR, "账号长度不符合要求");
        }
        if (userPassword.length() < 6 || checkPassword.length() < 6) {
            throw new BaseExceptionHandler(ErrorCode.PARAMS_ERROR, "密码不符合要求");
        }
        if (!userPassword.equals(checkPassword)) {
            throw new BaseExceptionHandler(ErrorCode.PARAMS_ERROR, "密码和校验密码不相等");
        }
        //判断账号是否含有非法字符
        String regEx = "[\\u00A0\\s\"`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern compile = Pattern.compile(regEx);
        Matcher matcher = compile.matcher(userAccount);
        if (matcher.find()) {
            throw new BaseExceptionHandler(ErrorCode.PARAMS_ERROR, "账号含有非法字符");
        }
        //校验账号是否重复
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("user_account",userAccount);
        Long count = userMapper.selectCount(userQueryWrapper);
        if (count>=1){
            throw new BaseExceptionHandler(ErrorCode.PARAMS_ERROR,"账号重复");
        }
        //对密码进行加密
        String safetyPassword = DigestUtils.md5DigestAsHex((userPassword + SALT_PASSWORD).getBytes());
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(safetyPassword);
        //将safetyUser插入数据库中
        int insert = userMapper.insert(user);
        if (insert <= 0) {
            throw new BaseExceptionHandler(ErrorCode.SYSTEM_ERROR, "插入失败");
        }
        return user.getId();
    }

    @Override
    public User getSafetyUser(User originUser) {
        if (originUser == null) {
            throw new BaseExceptionHandler(ErrorCode.PARAMS_ERROR, "用户为空");
        }
        User user = new User();
        user.setUserName(originUser.getUserName());
        user.setUserAccount(originUser.getUserAccount());
        user.setUserRole(originUser.getUserRole());
        user.setUserPhone(originUser.getUserPhone());
        user.setIsdelete(originUser.getIsdelete());
        user.setCreateTime(originUser.getCreateTime());
        user.setUpdateTime(originUser.getUpdateTime());
        user.setUserAge(originUser.getUserAge());
        user.setUserGender(originUser.getUserGender());
        return user;
    }

    @Override
    public User UserLogin(UserLoginRequestParam userLoginRequestParam, HttpServletRequest request) {
        if (userLoginRequestParam==null){
            throw new BaseExceptionHandler(ErrorCode.PARAMS_ERROR,"登录请求参数为空");
        }
        String userAccount = userLoginRequestParam.getUserAccount();
        String userPassword = userLoginRequestParam.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount,userPassword)){
            throw new BaseExceptionHandler(ErrorCode.PARAMS_ERROR,"参数为空");
        }
        //参数规则判断
        if (userAccount.length() < 4 || userAccount.length() > 10) {
            throw new BaseExceptionHandler(ErrorCode.PARAMS_ERROR, "账号长度不符合要求");
        }
        if (userPassword.length() < 6 ) {
            throw new BaseExceptionHandler(ErrorCode.PARAMS_ERROR, "密码不符合要求");
        }
        //判断账号是否含有非法字符
        String regEx = "[\\u00A0\\s\"`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern compile = Pattern.compile(regEx);
        Matcher matcher = compile.matcher(userAccount);
        if (matcher.find()) {
            throw new BaseExceptionHandler(ErrorCode.PARAMS_ERROR, "账号含有非法字符");
        }
        //校验账号是否重复
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("user_account",userAccount);
        Long count = userMapper.selectCount(userQueryWrapper);
        if (count>1){
            throw new BaseExceptionHandler(ErrorCode.PARAMS_ERROR,"账号重复");
        }
        //对密码进行加密
        String safetyPassword = DigestUtils.md5DigestAsHex((userPassword + SALT_PASSWORD).getBytes());
        //根据user_account和user_password查出唯一用户
        userQueryWrapper.eq("user_account",userAccount);
        userQueryWrapper.eq("user_password",safetyPassword);
        User user = userMapper.selectOne(userQueryWrapper);
        if (user==null){
            throw new BaseExceptionHandler(ErrorCode.PARAMS_ERROR,"用户不存在");
        }
        User safetyUser = getSafetyUser(user);
        //设置登录态
        request.getSession().setAttribute(LOGIN_STATUS,user);

        return safetyUser;
    }

    @Override
    public User getUserInfo(HttpServletRequest request) {
        if (request==null){
            throw new BaseExceptionHandler(ErrorCode.PARAMS_ERROR,"未登录");
        }
        Object attribute = request.getSession().getAttribute(LOGIN_STATUS);
        User user = (User) attribute;
        if (user==null){
            throw new BaseExceptionHandler(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = this.getById(user.getId());
        //将用户进行脱敏
        User safetyUser = getSafetyUser(loginUser);
        if (safetyUser==null){
            throw new BaseExceptionHandler(ErrorCode.PARAMS_ERROR);
        }
        return safetyUser;
    }

    @Override
    public boolean updateUserInfo(String userAccount,UserDto userDto,HttpServletRequest request) {
        if (userAccount==null){
            throw new BaseExceptionHandler(ErrorCode.PARAMS_ERROR,"参数错误");
        }
        if (request==null){
            throw new BaseExceptionHandler(ErrorCode.PARAMS_ERROR);
        }
        if (userDto==null){
            throw new BaseExceptionHandler(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = (User) request.getSession().getAttribute(LOGIN_STATUS);
        if (loginUser==null){
            throw new BaseExceptionHandler(ErrorCode.NOT_LOGIN,"用户未登录");
        }
        Integer loginUserId = loginUser.getId();
        //从数据库中查询
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("user_account",userAccount);
        User oldUser = this.getOne(userQueryWrapper);
        //如果loginUser的id和 传来的id不相同，说明不是登陆的用户修改的信息
        if (!loginUserId.equals(oldUser.getId())){
            throw new BaseExceptionHandler(ErrorCode.PARAMS_ERROR,"修改的用户不同");
        }
        //将userDto的值赋给oldUser
        BeanUtils.copyProperties(userDto,oldUser);
        //oldUser设置id值
        oldUser.setId(loginUserId);
        //如果修改前后的用户信息相等，直接返回错误,省去修改操作
        if (loginUser.equals(oldUser)){
            throw new BaseExceptionHandler(ErrorCode.PARAMS_ERROR,"更新前后两次信息相同");
        }
        //执行更新操作
        boolean result = this.updateById(oldUser);
        if (!result){
            throw new BaseExceptionHandler(ErrorCode.SYSTEM_ERROR,"更新失败");
        }
        return true;
    }

    @Override
    public List<User> getUserList(HttpServletRequest request) {
        if (request==null){
            throw new BaseExceptionHandler(ErrorCode.PARAMS_ERROR,"参数为空");
        }
        //如果不是管理员没权限
        if (!isAdmin(request)){
            throw new BaseExceptionHandler(ErrorCode.NO_AUTH,"该用户不是管理员");
        }
        //将所有用户都查询出来
        List<User> list = this.list();
        //将所有用户脱敏
        return list.stream().map(this::getSafetyUser).collect(Collectors.toList());

    }

    @Override
    public boolean isAdmin(HttpServletRequest request) {
        if (request==null){
            throw new BaseExceptionHandler(ErrorCode.PARAMS_ERROR,"参数为空");
        }
        User user = (User) request.getSession().getAttribute(LOGIN_STATUS);
        User loginUser = this.getById(user.getId());
        if (user==null){
            throw new BaseExceptionHandler(ErrorCode.PARAMS_ERROR);
        }
        Integer userRole = loginUser.getUserRole();
        if (!userRole.equals(ADMIN_ROLE)){
            throw new BaseExceptionHandler(ErrorCode.NO_AUTH,"该用户不是管理员");
        }
        return true;
    }

    @Override
    public boolean deleteUser(String userAccount) {
        if (userAccount==null){
            throw new BaseExceptionHandler(ErrorCode.PARAMS_ERROR,"用户账号为空");
        }
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("user_account",userAccount);
        boolean remove = this.remove(userQueryWrapper);
        if (!remove){
            throw new BaseExceptionHandler(ErrorCode.SYSTEM_ERROR,"更新失败");
        }
        return true;
    }

    @Override
    public boolean updatePassword(String userAccount,String newPassword,HttpServletRequest request,String userPassword) {
        if (userAccount==null){
            throw new BaseExceptionHandler(ErrorCode.PARAMS_ERROR,"用户账号为空");
        }
        if (newPassword==null){
            throw new BaseExceptionHandler(ErrorCode.PARAMS_ERROR,"新密码为空");
        }
        if (request==null){
            throw new BaseExceptionHandler(ErrorCode.PARAMS_ERROR,"请求为空");
        }
        User loginUser = (User) request.getSession().getAttribute(LOGIN_STATUS);
        if (loginUser==null){
            throw new BaseExceptionHandler(ErrorCode.NOT_LOGIN,"用户未登录");
        }
        //对user_password加密
        String string = DigestUtils.md5DigestAsHex((userPassword + SALT_PASSWORD).getBytes());
        //根据用户账号查询唯一用户
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("user_account",userAccount);
        userQueryWrapper.eq("user_password",string);
        User user = this.getOne(userQueryWrapper);
        if (user==null){
            throw new BaseExceptionHandler(ErrorCode.PARAMS_ERROR,"用户为空");
        }
        //判断根据user_account查出的用户和登陆的用户id是否相等
        if (!loginUser.getId().equals(user.getId())){
            throw new BaseExceptionHandler(ErrorCode.NO_AUTH,"该用户没有权限修改密码");
        }
        //对新密码进行加密
        String safetyPassword = DigestUtils.md5DigestAsHex((newPassword + SALT_PASSWORD).getBytes());
        //如果新密码和老密码相等，直接返回不用更新
        if (userPassword.equals(safetyPassword)){
            throw new BaseExceptionHandler(ErrorCode.PARAMS_ERROR,"两次密码一致");
        }
        //给用户设置新密码
        user.setUserPassword(safetyPassword);
        //更新用户
        boolean result = this.updateById(user);
        if (!result){
            throw new BaseExceptionHandler(ErrorCode.SYSTEM_ERROR,"更新失败");
        }
        return true;
    }
}




