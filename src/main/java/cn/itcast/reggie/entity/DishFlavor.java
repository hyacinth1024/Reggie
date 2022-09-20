package cn.itcast.reggie.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

@Data
public class DishFlavor extends BasePojo {

  @JsonSerialize(using = ToStringSerializer.class)
  private long id;
  private long dishId;
  private String name;
  private String value;
  private long isDeleted;//0为没有删除,1删除


}
