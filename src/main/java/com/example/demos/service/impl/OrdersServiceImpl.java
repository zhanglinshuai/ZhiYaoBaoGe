package com.example.demos.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demos.Param.OrderAddRequestParam;
import com.example.demos.Param.OrdersUpdateRequest;
import com.example.demos.error.ErrorCode;
import com.example.demos.exception.BaseExceptionHandler;
import com.example.demos.pojo.Medicine;
import com.example.demos.pojo.Orders;
import com.example.demos.pojo.User;
import com.example.demos.service.MedicineService;
import com.example.demos.service.OrdersService;
import com.example.demos.mapper.OrdersMapper;
import com.example.demos.service.UserService;
import com.example.demos.utils.SnowflakeIdWorker;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
* @author 86175
* @description 针对表【orders(销售表)】的数据库操作Service实现
* @createDate 2024-06-13 22:10:19
*/
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders>
    implements OrdersService{
    @Resource
    private UserService userService;

    @Resource
    private MedicineService medicineService;

    @Resource
    private OrdersMapper orderMapper;

    @Override
    public int addOrder(OrderAddRequestParam orderAddRequestParam) {
        //参数判空
        if (orderAddRequestParam == null) {
            throw new BaseExceptionHandler(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        String userAccount = orderAddRequestParam.getUserAccount();
        String medicineName = orderAddRequestParam.getMedicineName();
        Integer shoppingNumber = orderAddRequestParam.getShoppingNumber();
        int userGender = orderAddRequestParam.getUserGender();
        int userAge = orderAddRequestParam.getUserAge();
        String userName = orderAddRequestParam.getUserName();
        String userPhone = orderAddRequestParam.getUserPhone();
        String medicineManufacturer = orderAddRequestParam.getMedicineManufacturer();
        String medicineSpecification = orderAddRequestParam.getMedicineSpecification();

        if (StringUtils.isAnyBlank(userAccount, userName, userPhone, medicineName, medicineManufacturer, medicineSpecification)) {
            throw new BaseExceptionHandler(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (shoppingNumber < 0) {
            throw new BaseExceptionHandler(ErrorCode.PARAMS_ERROR, "购买的药品数量不符合要求");
        }
        if (userGender < 0 || userGender > 1) {
            throw new BaseExceptionHandler(ErrorCode.PARAMS_ERROR, "性别不符合要求");
        }
        if (userAge < 0) {
            throw new BaseExceptionHandler(ErrorCode.PARAMS_ERROR, "年龄不符合要求");
        }
        //根据user_account可以查出唯一user
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("user_account", userAccount);
        User user = userService.getOne(userQueryWrapper);
        if (user == null) {
            throw new BaseExceptionHandler(ErrorCode.PARAMS_ERROR, "用户账号不正确");
        }
        //根据medicine_name medicine_manufacturer   medicine_specification  可以查出唯一药品
        QueryWrapper<Medicine> medicineQueryWrapper = new QueryWrapper<>();
        medicineQueryWrapper.eq("medicine_name", medicineName);
        medicineQueryWrapper.eq("medicine_manufacturer", medicineManufacturer);
        medicineQueryWrapper.eq("medicine_specification", medicineSpecification);
        Medicine medicine = medicineService.getOne(medicineQueryWrapper);
        if (medicine == null) {
            throw new BaseExceptionHandler(ErrorCode.PARAMS_ERROR, "药品不存在");
        }
        //使用雪花算法自动生成订单编号
        SnowflakeIdWorker snowflakeIdWorker = new SnowflakeIdWorker(0, 0);
        int orderNumber = (int) snowflakeIdWorker.nextId();
        Orders order = new Orders();
        order.setOrderNumber(orderNumber);
        order.setUserId(user.getId());
        order.setUserAccount(userAccount);
        order.setUserName(userName);
        order.setMedicineId(medicine.getId());
        order.setMedicineName(medicineName);
        order.setShoppingNumber(shoppingNumber);
        order.setUserPhone(userPhone);
        order.setUserAge(userAge);
        order.setUserGender(userGender);
        order.setMedicineSpecification(medicineSpecification);
        order.setMedicineManufacturer(medicineManufacturer);
        //将order插入数据库中
        int insert = orderMapper.insert(order);
        if (insert<=0) {
            throw new BaseExceptionHandler(ErrorCode.SYSTEM_ERROR, "插入错误");
        }
        //根据购买的药品数量，修改药品的库存量
        String medicineInventory = medicine.getMedicineInventory();
        int InventoryNumber = Integer.parseInt(medicineInventory);
        int newInventoryNumber = InventoryNumber-shoppingNumber;
        //更新
        medicine.setMedicineInventory(String.valueOf(newInventoryNumber));
        boolean result = medicineService.updateById(medicine);
        if (!result){
            throw new BaseExceptionHandler(ErrorCode.PARAMS_ERROR,"库存更新失败");
        }
        return order.getId();

    }

    @Override
    public List<Orders> getOrders(HttpServletRequest request) {
        if (request==null){
            throw new BaseExceptionHandler(ErrorCode.PARAMS_ERROR);
        }
        //如果不是管理员
        if (!userService.isAdmin(request)){
            return new ArrayList<>();
        }
        //如果是管理员直接返回即可
        List<Orders> ordersList = this.list();
        if (ordersList.isEmpty()){
            throw new BaseExceptionHandler(ErrorCode.PARAMS_ERROR,"订单列表为空");
        }
        return ordersList;
    }

    @Override
    public boolean deleteOrders(Integer orderNumber) {
        QueryWrapper<Orders> ordersQueryWrapper = new QueryWrapper<>();
        ordersQueryWrapper.eq("order_number",orderNumber);
        int delete = orderMapper.delete(ordersQueryWrapper);
        if (delete<=0){
            throw new BaseExceptionHandler(ErrorCode.SYSTEM_ERROR,"删除异常");
        }
        return true;
    }

    @Override
    public boolean updateOrders(OrdersUpdateRequest ordersUpdateRequest) {
        if (ordersUpdateRequest==null){
            throw new BaseExceptionHandler(ErrorCode.PARAMS_ERROR,"参数为空");
        }
        Integer orderNumber = ordersUpdateRequest.getOrderNumber();
        Orders orders = ordersUpdateRequest.getOrders();
        if (orderNumber==null){
            throw new BaseExceptionHandler(ErrorCode.PARAMS_ERROR,"订单编号异常");
        }
        if (orders==null){
            throw new BaseExceptionHandler(ErrorCode.PARAMS_ERROR,"订单异常");
        }

        //根据 order_number查询订单
        QueryWrapper<Orders> ordersQueryWrapper = new QueryWrapper<>();
        ordersQueryWrapper.eq("order_number",orderNumber);
        Orders oldOrders = this.getOne(ordersQueryWrapper);
        if (oldOrders==null){
            throw new BaseExceptionHandler(ErrorCode.PARAMS_ERROR,"订单错误");
        }
        //通过orders的user_account来查询有没有这个人
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("user_account",orders.getUserAccount());
        User user = userService.getOne(userQueryWrapper);
        if (user==null){
            throw new BaseExceptionHandler(ErrorCode.PARAMS_ERROR,"用户账号错误,不存在此用户");
        }
        //通过orders的 medicine_name medicine_specification  medicine_manufacturer来查询有没有这个药品
        QueryWrapper<Medicine> medicineQueryWrapper = new QueryWrapper<>();
        medicineQueryWrapper.eq("medicine_name",orders.getMedicineName());
        medicineQueryWrapper.eq("medicine_specification",orders.getMedicineSpecification());
        medicineQueryWrapper.eq("medicine_manufacturer",orders.getMedicineManufacturer());
        Medicine medicine = medicineService.getOne(medicineQueryWrapper);
        if (medicine==null){
            throw new BaseExceptionHandler(ErrorCode.PARAMS_ERROR,"没有该药品信息");
        }
        Integer id = oldOrders.getId();
        //把orders赋值给oldOrders
        BeanUtils.copyProperties(orders,oldOrders);
        oldOrders.setId(id);
        oldOrders.setUserId(user.getId());
        oldOrders.setMedicineId(medicine.getId());
        //执行更新操作
        boolean result = this.updateById(oldOrders);
        if (!result){
            throw new BaseExceptionHandler(ErrorCode.SYSTEM_ERROR,"更新失败");
        }
        return true;
    }

    @Override
    public Orders searchOrders(Integer orderNumber) {
        if (orderNumber==null){
            throw new BaseExceptionHandler(ErrorCode.PARAMS_ERROR,"订单编号异常");
        }
        QueryWrapper<Orders> ordersQueryWrapper = new QueryWrapper<>();
        ordersQueryWrapper.eq("order_number",orderNumber);
        Orders orders = this.getOne(ordersQueryWrapper);
        if (orders==null){
            throw new BaseExceptionHandler(ErrorCode.PARAMS_ERROR,"未找到该订单信息");
        }
        return orders;
    }
}




