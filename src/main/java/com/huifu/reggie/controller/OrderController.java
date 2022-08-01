package com.huifu.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huifu.reggie.common.BaseContext;
import com.huifu.reggie.common.R;
import com.huifu.reggie.entity.Employee;
import com.huifu.reggie.entity.OrderDetail;
import com.huifu.reggie.entity.Orders;
import com.huifu.reggie.service.OrderDetailService;
import com.huifu.reggie.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderDetailService orderDetailService;

    /**
     * 用户下单
     *
     * @param orders
     * @return
     */
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders) {
        orderService.submit(orders);
        return R.success("提交菜单成功");
    }


    @GetMapping("/userPage")
    public R<Page> page(int page, int pageSize) {
        Page pageInfo = new Page(page, pageSize);

        Long currentId = BaseContext.getCurrentId();
        LambdaQueryWrapper<Orders> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Orders::getUserId, currentId);
        wrapper.orderByDesc(Orders::getOrderTime);
        orderService.page(pageInfo, wrapper);
        return R.success(pageInfo);
    }
}
