package com.example.demos.controller;

import com.example.demos.Param.OrderAddRequestParam;
import com.example.demos.Param.OrdersUpdateRequest;
import com.example.demos.error.BaseResponse;
import com.example.demos.error.ErrorCode;
import com.example.demos.error.ResultUtils;
import com.example.demos.exception.BaseExceptionHandler;
import com.example.demos.pojo.Orders;
import com.example.demos.service.OrdersService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.xml.transform.Result;
import java.util.List;

@RestController
@RequestMapping("/order")
@CrossOrigin(value = "http://localhost:5173", allowCredentials = "true")
public class OrderController {

    @Resource
    private OrdersService orderService;

    @PostMapping("/add")
    public BaseResponse<Integer> addOrder(OrderAddRequestParam orderAddRequestParam){
        if (orderAddRequestParam==null){
            throw new BaseExceptionHandler(ErrorCode.PARAMS_ERROR,"参数为空");
        }
        //调用业务
        int orderId = orderService.addOrder(orderAddRequestParam);
        if (orderId<=0){
            throw new BaseExceptionHandler(ErrorCode.PARAMS_ERROR,"插入错误");
        }
        return ResultUtils.success(orderId);
    }

    @GetMapping("/getList")
    public BaseResponse<List<Orders>> getOrdersList(HttpServletRequest request){
        if (request==null){
            throw new BaseExceptionHandler(ErrorCode.PARAMS_ERROR);
        }
        //调用业务
        List<Orders> orders = orderService.getOrders(request);
        if (orders.isEmpty()){
            throw new BaseExceptionHandler(ErrorCode.PARAMS_ERROR,"订单列表为空");
        }
        return ResultUtils.success(orders);
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteOrders(Integer orderNumber){
        if (orderNumber==null){
            throw new BaseExceptionHandler(ErrorCode.PARAMS_ERROR,"订单编号错误");
        }
        boolean result = orderService.deleteOrders(orderNumber);
        if (!result){
            throw new BaseExceptionHandler(ErrorCode.SYSTEM_ERROR,"删除异常");
        }
        return ResultUtils.success(true);
    }


    @PostMapping("/update")
    public BaseResponse<Boolean> updateOrders(@RequestBody OrdersUpdateRequest ordersUpdateRequest){

        if (ordersUpdateRequest==null){
            throw new BaseExceptionHandler(ErrorCode.PARAMS_ERROR,"参数为空");
        }
        //调用业务
        boolean result = orderService.updateOrders(ordersUpdateRequest);
        if (!result){
            throw new BaseExceptionHandler(ErrorCode.SYSTEM_ERROR,"更新失败");
        }
        return ResultUtils.success(true);
    }


    @GetMapping("/search")
    public BaseResponse<Orders> searchOrders(Integer orderNumber){
        if (orderNumber==null){
            throw new BaseExceptionHandler(ErrorCode.PARAMS_ERROR,"订单编号异常");
        }
        //调用业务
        Orders orders = orderService.searchOrders(orderNumber);
        if (orders==null){
            throw new BaseExceptionHandler(ErrorCode.PARAMS_ERROR,"未找到该订单信息");
        }
        return ResultUtils.success(orders);
    }

}
