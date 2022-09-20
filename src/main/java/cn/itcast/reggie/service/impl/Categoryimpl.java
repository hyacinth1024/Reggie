package cn.itcast.reggie.service.impl;

import cn.itcast.reggie.common.Result;
import cn.itcast.reggie.entity.Category;
import cn.itcast.reggie.common.PageResult;
import cn.itcast.reggie.mapper.CategoryMapper;
import cn.itcast.reggie.service.CategoryService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class Categoryimpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 分页查询菜品分类
     * @Param:
     * @return:
     */
    @Override
    public Result findCPage(Integer page, Integer pagesize) {
        //page未传值或者page<=0则设置等于1
        if (page==null || page<=0){
            page=1;
        }
        //若pagesize未传值或pagesize>20,设置为=10
        if (pagesize==null || pagesize>20){
            pagesize=20;
        }
        IPage<Category> iPage = new Page<>(page,pagesize);  //构建分页查询对象
        QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
        //调用MybatisPlus提供的分页条件查询方法
        iPage = categoryMapper.selectPage(iPage,queryWrapper);
        //iPage.getRecords()返回当前的列表合集List
        //iPage.getTotal()返回条件的总数
        PageResult<Category> pageResult = new PageResult<>(iPage.getRecords(),iPage.getTotal());
        return Result.success(pageResult);
    }

    /**
     * 新增菜品分类
     * @Param:
     * @return:
     */
    @Override
    public Result addCategory(Category category, long id) {
        //判断传入对象是否为null
        if (category == null) {
            return Result.error("输入数据不合法!");
        }

        if (!StringUtils.hasLength(category.getName())){
            return Result.error("分类名不能为空!");
        }
        if (category.getType() != 1 && category.getType() != 2){
            category.setType(1);
        }
        //获取系统时间
        category.setCreateTime(LocalDateTime.now());
        category.setUpdateTime(LocalDateTime.now());
        //获取创建人和修改人-->在登陆成功后,数据存储在session中
        category.setCreateUser(id);
        category.setUpdateUser(id);
        categoryMapper.insert(category);
        //System.out.println("impl");
        return Result.success(null);
    }

    /**
     * 删除分类
     * @Param:
     * @return:
     */
    @Override
    public Result deleById(Long id) {
        if (id == null){
            return Result.error("id为空!");
        }
        int i = categoryMapper.deleteById(id);
        if (i==0){
            return Result.error("没有删除数据！");
        }
        return Result.success(null);
    }

    /**
     * 修改菜品分类
     * @Param:  null
     * @return:
     */
    @Override
    public Result updateCategory(Category category, Long id) {
        if (category == null){
            return Result.error("修改数据错误!");
        }

        if (!StringUtils.hasLength(category.getName())){
            return Result.error("分类名不能为空!");
        }
        Category temp = categoryMapper.selectById(category.getId());
        if (temp == null){
            return Result.error("排序为空！");
        }
        category.setUpdateUser(id);
        category.setUpdateTime(LocalDateTime.now());
        int i = categoryMapper.updateById(category);
        //i表示影响数据的行数，i=0代表未修改数据
        if (i==0){
            return Result.error("未修改数据！");
        }
        return Result.success(null);
    }

    /**
     * 按照分类查询菜品
     * @Param:
     * @return:
     */
    @Override
    public Result findListByType(Integer type) {
        if (type != 1){
            type=1;
        }
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Category::getType,type);
        List<Category> list = categoryMapper.selectList(wrapper);
        return Result.success(list);
    }

}
