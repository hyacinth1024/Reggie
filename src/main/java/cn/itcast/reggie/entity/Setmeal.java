package com.itcast.reggie.common;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

@Data
public class Setmeal extends BasePojo{
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    private String name;
    private Long categoryId;
    private Double price;
    private String code;
    private String image;
    private String description;
    private Integer status; //'0 停售 1 起售'
    private Integer isDeleted;  //是否删除 1 删除 0 未删除
}
