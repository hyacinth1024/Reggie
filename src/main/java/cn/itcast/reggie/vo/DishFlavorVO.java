package cn.itcast.reggie.vo;

import cn.itcast.reggie.dto.DishDto;
import cn.itcast.reggie.entity.DishFlavor;
import lombok.Data;

import java.util.List;
@Data
public class DishFlavorVO extends DishDto{
//    private Dish dish;
//    private List<DishFlavor> flavors;
    /*private long id;
    private String name;
    private long categoryId;
    private double price;
    private String image;
    private String description;
    private long status;
    private long sort;
    private long isDeleted;*/
    private List<DishFlavor> list;
}
