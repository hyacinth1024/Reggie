package cn.itcast.reggie.dto;

import cn.itcast.reggie.entity.Dish;
import cn.itcast.reggie.entity.DishFlavor;
import lombok.Data;

import java.util.List;

@Data
public class DishDto extends Dish {
    //接收josn数据中的flavor集合,函数名必须保持一至
    private List<DishFlavor> flavors;
}
