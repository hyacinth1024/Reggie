package cn.itcast.reggie.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

@Data
public class Category extends BasePojo{

  //将Long类型的id进行json转换时变成string类型，避免前端精度丢失
  @JsonSerialize(using = ToStringSerializer.class)
  private long id;
  private long type;
  private String name;
  private long sort;

}
