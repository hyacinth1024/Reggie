package cn.itcast.reggie.controller;

import cn.itcast.reggie.common.Result;
import cn.itcast.reggie.entity.Employee;
import cn.itcast.reggie.dto.LoginDto;
import cn.itcast.reggie.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/employee")
public class employeeController {
    @Autowired
    private EmployeeService employeeService;

    /*登录*/
    @PostMapping("/login")
    //Dto实体类专门用来接收数据
    public Result login(@RequestBody LoginDto loginDto, HttpSession session){
        Result result = employeeService.login(loginDto.getUsername(),loginDto.getPassword());
        //将用户信息存入session
        session.setAttribute("employee",result.getData());
        return result;
    }

    /*登出*/
    @PostMapping("/logout")
    public Result logOut(HttpSession session){
        session.invalidate();
        return Result.success("登录成功");
    }

    /*添加员工*/
    @PostMapping
    public Result addEmployee(@RequestBody Employee employee,HttpSession session){
        //从session中获取当前登录的id
        Employee e = (Employee) session.getAttribute("employee");
        //System.out.println("controller");
        return employeeService.addEmployee(employee,e.getId());
    }

    /*分页查询员工*/
    @GetMapping("/page")
    public Result findPage(Integer page,Integer pagesize,String name){

        return employeeService.findPage(page,pagesize,name);
    }

    /*按照id查询完成数据回显*/
    @GetMapping("/{id}")
    public Result findById(@PathVariable Long id){
        return employeeService.findById(id);
    }

    /*修改员工信息*/
    @PutMapping
    public Result updateEmployee(@RequestBody Employee employee,HttpSession session){
        Employee nowe = (Employee) session.getAttribute("employee");
        return employeeService.updateEmployee(employee,nowe.getId());
    }
}
