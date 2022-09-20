package com.itcast.reggie.dto;

import com.itcast.reggie.common.Dish;
import com.itcast.reggie.common.DishFlavor;
import com.itcast.reggie.common.Setmeal;
import com.itcast.reggie.common.SetmealDish;
import lombok.Data;

import java.util.List;

@Data
public class SetmealDTO extends Setmeal {
    //接收json中的setmealDishes集合 名称不能任意修改
    //套餐里面有几个菜
    private List<SetmealDish> setmealDishes;
}
