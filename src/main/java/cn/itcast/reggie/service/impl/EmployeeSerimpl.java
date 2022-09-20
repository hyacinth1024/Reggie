package cn.itcast.reggie.service.impl;

import cn.itcast.reggie.common.Result;
import cn.itcast.reggie.entity.Employee;
import cn.itcast.reggie.common.PageResult;
import cn.itcast.reggie.mapper.EmployeeMapper;
import cn.itcast.reggie.service.EmployeeService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@Service
public class EmployeeSerimpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 登录
     * @Param:
     * @return:
     */
    @Override
    public Result login(String username, String password) {
        /*
            (1)判断用户名或密码是否为空，如果为空，直接提示用户名或密码错误
            (2)获取用户密码，对密码进行MD5加密，获取密文密码
            (3)根据用户和密文密码从数据库表emplyee中查询用户信息
            (4)如果查询的用户信息为NULL,说明用户名或密码错误
            (5)判断用户信息中的status是否为0，如果为0说明该用户已被冻结，不允许登录，返回错误提示
            (6)返回用户对象
        */
        if (!StringUtils.hasLength(username) || !StringUtils.hasLength(password)){
            return Result.error("用户名或密码输入不合法！");
        }
        String pwd = DigestUtils.md5DigestAsHex(password.getBytes());
        QueryWrapper<Employee> wrapper = new QueryWrapper<>();
        //wq:= lt:< le:<= gt:> ge:>=
        wrapper.eq("username",username);
        wrapper.eq("password",pwd);

        Employee employee = employeeMapper.selectOne(wrapper);
        if (employee==null){
            return Result.error("用户或者密码！");
        }
        if (employee.getStatus()==0){
            return Result.error("该用户被禁用!");
        }

        return Result.success(employee);
    }

    /**
     * 添加员工
     * @Param:
     * @return: 当前登录人的id
     */
    @Override
    public Result addEmployee(Employee employee,Long userId) {
        //判断传入对象是否为null
        if (employee == null) {
            return Result.error("输入数据不合法!");
        }

        if (!StringUtils.hasLength(employee.getName())){
            return Result.error("用户名不能为空!");
        }
        QueryWrapper<Employee> queryWrapper = new QueryWrapper<>();
        //select * from employee where username=#{}
        queryWrapper.eq("username",employee.getUsername());
        Employee emp = employeeMapper.selectOne(queryWrapper);
        if (emp!=null){
            return Result.error("用户名已存在!");
        }
        //密码加密
        String pwd = DigestUtils.md5DigestAsHex("123456".getBytes());
        employee.setPassword(pwd);
        employee.setStatus(1);
        //获取系统时间
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());
        //获取创建人和修改人-->在登陆成功后,数据存储在session中
        employee.setCreateUser(userId);
        employee.setUpdateUser(userId);
        employeeMapper.insert(employee);
        //System.out.println("impl");
        return Result.success(null);
    }

    /**
     * 查询用户
     * @Param: 页数,每页数据量,用户名
     * @return: pageResult
     */
    @Override
    public Result findPage(Integer page,Integer pagesize,String name) {
        //page未传值或者page<=0则设置等于1
        if (page==null || page<=0){
            page=1;
        }
        //若pagesize未传值或pagesize>20,设置为=10
        if (pagesize==null || pagesize>20){
            pagesize=20;
        }
        IPage<Employee> iPage = new Page<>(page,pagesize);  //构建分页查询对象
        QueryWrapper<Employee> queryWrapper = new QueryWrapper<>();
        if (StringUtils.hasLength(name)){   //前端传递name必须有值才按照条件查询
            // where name like '%???%'
            queryWrapper.like("name",name);
        }
        //调用MybatisPlus提供的分页条件查询方法
        iPage = employeeMapper.selectPage(iPage,queryWrapper);
        //iPage.getRecords()返回当前的列表合集List
        //iPage.getTotal()返回条件的总数
        PageResult<Employee> pageResult = new PageResult<>(iPage.getRecords(),iPage.getTotal());
        return Result.success(pageResult);
    }

    /**
     * 按照id查询
     * @Param:
     * @return:
     */
    @Override
    public Result findById(Long id) {
        if (id == null){
            return Result.error("id为空!");
        }
//        Employee employee = employeeMapper.selectById(id);
        QueryWrapper<Employee> objectQueryWrapper = new QueryWrapper<>();
        //设置查询条件
        objectQueryWrapper.eq("id",id);
        //设置查询字段
        objectQueryWrapper.select("id","name","username","phone","sex","id_number");
        Employee employee = employeeMapper.selectOne(objectQueryWrapper);
        //封装结果,返回
        return Result.success(employee);
    }

    /**
     * 修改员工信息
     * @Param:
     * @return:
     */
    @Override
    public Result updateEmployee(Employee employee, Long userId) {
        //校验employee对象是否为null
        if (employee == null) {
            return Result.error("输入数据不合法!");
        }
        //校验employee中的username是否为空
        //当status为空代表进行状态变化操作,不为空则为修改用户信息
        if (!StringUtils.hasLength(employee.getName()) && employee.getStatus()==null){
            return Result.error("用户名不能为空!");
        }
        //获取创建人和修改人-->在登陆成功后,数据存储在session中
        employee.setUpdateUser(userId);
        //获取系统时间
        employee.setUpdateTime(LocalDateTime.now());
        //修改员工信息
        employeeMapper.updateById(employee);
        //构建返回值
        return Result.success(null);
    }
}
