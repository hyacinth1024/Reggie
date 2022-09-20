package cn.itcast.reggie.controller;

import cn.itcast.reggie.common.Result;
import cn.itcast.reggie.entity.Category;
import cn.itcast.reggie.entity.Employee;
import cn.itcast.reggie.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/category")
public class categoryController {
    @Autowired
    private CategoryService categoryService;

    /*分类查询*/
    @GetMapping("/page")
    public Result findCPage(Integer page,Integer pagesize){

        return categoryService.findCPage(page,pagesize);
    }

    /*添加菜品*/
    @PostMapping
    public Result addCategory(@RequestBody Category category, HttpSession session){
            //从session中获取当前登录的id
            Employee e = (Employee) session.getAttribute("employee");
            //System.out.println("controller");
            return categoryService.addCategory(category,e.getId());
    }

    /*删除菜品*/
    @DeleteMapping
    public Result deleById(@RequestParam("ids") Long id){
//        System.out.println(ids);
        return categoryService.deleById(id);
    }

    /*修改菜品*/
    @PutMapping
    public Result updateCategory(@RequestBody Category category, HttpSession session){
        Employee e = (Employee) session.getAttribute("employee");
        return categoryService.updateCategory(category,e.getId());
    }

    /*按照分类查询*/
    @GetMapping("/list")
    public Result findList(Integer type){
        return categoryService.findListByType(type);
    }
}
