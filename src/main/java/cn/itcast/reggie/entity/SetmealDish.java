package com.itcast.reggie.common;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

@Data
public class SetmealDish extends BasePojo{
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    private String name;
    private Long setmealId;
    private Long dishId;
    private Double price;
    private Integer copies;
    private Integer sort;
    private Integer isDeleted;  //1删除 0没删
}
