package com.huifu.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huifu.reggie.dto.SetmealDto;
import com.huifu.reggie.entity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {

    /**
     * 新增套餐，同时需要保存套餐和菜品的关联关系
     * @param setmealDto
     */
    void saveWithDish(SetmealDto setmealDto);

    /**
     * 删除套餐同时需删除套餐和菜品的关联关系
     * @param ids
     */
    void removeWithDish(List<Long> ids);
}
