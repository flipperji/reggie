package com.huifu.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huifu.reggie.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper extends BaseMapper<Orders> {
}
