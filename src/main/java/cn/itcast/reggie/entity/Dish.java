package cn.itcast.reggie.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

@Data
public class Dish extends BasePojo{

  @JsonSerialize(using = ToStringSerializer.class)
  private long id;
  private String name;
  @JsonSerialize(using = ToStringSerializer.class)
  private long categoryId;
  private double price;
  private String code;
  private String image;
  private String description;
  private long status;
  private long sort;
  private long isDeleted;

}
