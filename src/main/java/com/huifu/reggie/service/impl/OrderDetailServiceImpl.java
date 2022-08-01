package com.huifu.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huifu.reggie.entity.OrderDetail;
import com.huifu.reggie.entity.Orders;
import com.huifu.reggie.mapper.OrderDetailMapper;
import com.huifu.reggie.mapper.OrderMapper;
import com.huifu.reggie.service.OrderDetailService;
import com.huifu.reggie.service.OrderService;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
