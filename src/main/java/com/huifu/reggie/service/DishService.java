package com.huifu.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huifu.reggie.dto.DishDto;
import com.huifu.reggie.entity.Dish;

public interface DishService extends IService<Dish> {

    //新增菜品，同事插入菜品对应的口味数据，需要同时操作两个表: dish、dish_flavor
    void saveWithFlavor(DishDto dishDto);

    //根据id来查询菜品信息和口味信息
    DishDto getByIdWithFlavor(Long id);

    //更新菜品信息，同时更新对应的口味信息
    void updateWithFlavor(DishDto dishDto);
}

