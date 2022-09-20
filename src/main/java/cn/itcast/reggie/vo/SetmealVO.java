package cn.itcast.reggie.vo;

import cn.itcast.reggie.entity.Setmeal;
import cn.itcast.reggie.entity.SetmealDish;
import lombok.Data;

import java.util.List;

@Data
public class SetmealVO extends Setmeal {
    private String categoryName;
}
