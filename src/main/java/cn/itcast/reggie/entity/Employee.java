package cn.itcast.reggie.entity;

import lombok.Data;

@Data

public class Employee extends BasePojo{

  private Long id;
  private String name;
  private String username;
  private String password;
  private String phone;
  private String sex;
  private String idNumber;
  private Integer status;

}
