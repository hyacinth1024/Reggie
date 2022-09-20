package cn.itcast.reggie.controller;

import cn.itcast.reggie.common.Result;
import cn.itcast.reggie.dto.DishDto;
import cn.itcast.reggie.entity.Employee;
import cn.itcast.reggie.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/dish")
public class dishController {

    @Autowired
    private DishService dishService;

    /**
     * 分页查询菜品
     * @Param:
     * @return:
     */
    @GetMapping("/page")
    public Result findDPage(@RequestParam(defaultValue = "1") Integer page,
                            @RequestParam(defaultValue = "10") Integer pageSize, String name){
        return dishService.findDPage(page,pageSize,name);
    }

    /**
     * 添加菜品
     * @Param:
     * @return:
     */
    @PostMapping
    public Result addDish(@RequestBody DishDto dishDto, HttpSession session){
        //获取当前登录人
        Employee employee = (Employee) session.getAttribute("employee");
        Long id = employee.getId();
        //调用service
        return dishService.addDish(dishDto,id);
    }

    /**
     * 按照id查询回显数据
     * @Param:
     * @return:
     */
    @GetMapping("/{ids}")
    public Result findById(@PathVariable("ids") Long id){
        return dishService.findById(id);
    }

    /**
     * 修改菜品
     * @Param:
     * @return:
     */
    @PutMapping
    public Result updateDish(@RequestBody DishDto dishDto, HttpSession session){
        Employee employee = (Employee) session.getAttribute("employee");
        Long empId = employee.getId();
        return dishService.updateDish(dishDto,empId);
    }

    /**
     * 修改状态
     * @Param:
     * @return:
     */
    @PostMapping("/status/{status}")
    public Result updateStatus(@PathVariable Integer status, String ids,HttpSession session){
        Employee employee = (Employee) session.getAttribute("employee");
        Long currentid = employee.getId();
        return dishService.updateStatus(status,ids,currentid);
    }

    /**
     * 逻辑删除菜品
     * @Param:
     * @return:
     */
    @DeleteMapping
    public Result deleteById(String ids, HttpSession session){
        Employee e = (Employee) session.getAttribute("employee");
        Long currentId = e.getId();
        return dishService.deleteById(ids,currentId);
    }
}
