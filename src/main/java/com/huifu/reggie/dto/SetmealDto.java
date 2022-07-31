package com.huifu.reggie.dto;

import com.huifu.reggie.entity.Setmeal;
import com.huifu.reggie.entity.SetmealDish;
import lombok.Data;

import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
