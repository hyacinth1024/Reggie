package cn.itcast.reggie.vo;

import cn.itcast.reggie.entity.Dish;
import cn.itcast.reggie.entity.DishFlavor;
import lombok.Data;

import java.util.List;

@Data
public class DishVO extends Dish {
    private String categoryName;
    private List<DishFlavor> flavors;
}
