package com.huifu.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huifu.reggie.entity.Orders;

public interface OrderService extends IService<Orders> {

    void submit(Orders orders);
}
