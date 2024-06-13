package com.example.demos.Param;

import com.example.demos.pojo.Orders;
import lombok.Data;

import java.io.Serializable;
@Data
public class OrdersUpdateRequest implements Serializable {

    private static final long serialVersionUID = -4923268197922931265L;


    private Integer orderNumber;


    private Orders orders;
}
