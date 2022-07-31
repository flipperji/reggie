package com.huifu.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huifu.reggie.dto.DishDto;
import com.huifu.reggie.entity.Dish;
import com.huifu.reggie.entity.DishFlavor;
import com.huifu.reggie.mapper.DishMapper;
import com.huifu.reggie.service.DishFlavorService;
import com.huifu.reggie.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    @Autowired
    private DishFlavorService dishFlavorService;

    /**
     * 新增菜品同时保存对应的口味数据
     * 要注意开启事务控制
     *
     * @param dishDto
     */
    @Transactional
    public void saveWithFlavor(DishDto dishDto) {
        //保存到菜品表
        this.save(dishDto);
        //获取菜品id
        Long id = dishDto.getId();
        //保存到菜品口味表
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors.stream().map((item) -> {
            item.setDishId(id);
            return item;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(flavors);
    }

    @Override
    public DishDto getByIdWithFlavor(Long id) {
        Dish dish = this.getById(id);
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, dish.getId());
        List<DishFlavor> dishFlavorList = dishFlavorService.list(queryWrapper);
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish, dishDto);
        dishDto.setFlavors(dishFlavorList);
        return dishDto;
    }

    @Override
    @Transactional
    public void updateWithFlavor(DishDto dishDto) {
        //更新dish表基本信息
        this.updateById(dishDto);
        //清理当前菜品对应口味数据--dish_flavor表的delete操作
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, dishDto.getId());

        dishFlavorService.remove(queryWrapper);

        //添加当前提交过来的口味--dish_flavor表的insert操作
        List<DishFlavor> flavors = dishDto.getFlavors();

        flavors.stream().map((item) -> {
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());

        dishFlavorService.saveBatch(flavors);
    }
}
