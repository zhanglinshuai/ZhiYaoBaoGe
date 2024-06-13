package com.example.demos.service;

import com.example.demos.Param.OrderAddRequestParam;
import com.example.demos.Param.OrdersUpdateRequest;
import com.example.demos.pojo.Orders;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author 86175
* @description 针对表【orders(销售表)】的数据库操作Service
* @createDate 2024-06-13 22:10:19
*/
public interface OrdersService extends IService<Orders> {
    /**
     * 添加订单
     * @param orderAddRequestParam
     * @return
     */
    int addOrder(OrderAddRequestParam orderAddRequestParam);

    /**
     * 获取所有订单列表
     * @param request
     * @return
     */
    List<Orders> getOrders(HttpServletRequest request);

    /**
     * 删除订单
     * @param orderNumber
     * @return
     */
    boolean deleteOrders(Integer orderNumber);

    /**
     * 更新订单
     * @param ordersUpdateRequest
     * @return
     */
    boolean updateOrders(OrdersUpdateRequest ordersUpdateRequest);

    /**
     * 根据订单编号搜索订单
     * @param orderNumber
     * @return
     */
    Orders searchOrders(Integer orderNumber);

}



