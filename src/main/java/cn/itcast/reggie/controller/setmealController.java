package com.itcast.reggie.controller;

import com.itcast.reggie.common.Employee;
import com.itcast.reggie.common.Result;
import com.itcast.reggie.common.Setmeal;
import com.itcast.reggie.dto.SetmealDTO;
import com.itcast.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    @GetMapping("/page")
    public Result findSPage(@RequestParam(defaultValue = "1") Integer page,
                            @RequestParam(defaultValue = "10") Integer pageSize, String name){
        return setmealService.findSPage(page,pageSize,name);

    }

    @GetMapping("/{id}")
    public Result querySetmealById(@PathVariable Long id){
        return setmealService.querySetmealById(id);
    }
    @PostMapping
    public Result addSetmeal(@RequestBody SetmealDTO dto, HttpSession session){
        //1.获取登录用户id
        Employee employee = (Employee) session.getAttribute("employee");
        Long cuId = employee.getId();
        return setmealService.addSetmeal(dto,cuId);
    }
    @PutMapping
    public Result editSetmeal(@RequestBody SetmealDTO dto, HttpSession session){
        //1.获取登录用户id
        Employee employee = (Employee) session.getAttribute("employee");
        Long cuId = employee.getId();
        return setmealService.editSetmeal(dto,cuId);
    }
    @DeleteMapping
    public Result deleteSetmeal(@RequestParam String ids, HttpSession session){
        //1.获取登录用户id
        Employee employee = (Employee) session.getAttribute("employee");
        Long cuId = employee.getId();
        return setmealService.deleteSetmeal(ids,cuId);
    }
    @PostMapping("/status/{status}")
    public Result setmealStatusByStatus(@PathVariable Integer status,@RequestParam String ids, HttpSession session){
        //1.获取登录用户id
        Employee employee = (Employee) session.getAttribute("employee");
        Long cuId = employee.getId();
        return setmealService.setmealStatusByStatus(status,ids,cuId);
    }
}
