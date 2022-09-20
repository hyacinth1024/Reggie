package cn.itcast.reggie.service;

import cn.itcast.reggie.common.Result;
import cn.itcast.reggie.entity.Employee;

public interface EmployeeService {

    /*登录*/
    public Result login(String username,String password);
    /*新增员工*/
    public Result addEmployee(Employee employee,Long userId);
    /*查询*/
    public Result findPage(Integer page,Integer pagesize,String name);
    /*根据id查询*/
    public Result findById(Long id);
    /*修改*/
    public Result updateEmployee(Employee employee, Long userId);
}
