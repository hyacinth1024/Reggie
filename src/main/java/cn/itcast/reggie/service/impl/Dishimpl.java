package cn.itcast.reggie.service.impl;

import cn.itcast.reggie.common.Result;
import cn.itcast.reggie.dto.DishDto;
import cn.itcast.reggie.entity.Category;
import cn.itcast.reggie.entity.Dish;
import cn.itcast.reggie.entity.DishFlavor;
import cn.itcast.reggie.common.PageResult;
import cn.itcast.reggie.mapper.CategoryMapper;
import cn.itcast.reggie.mapper.DishFlavorMapper;
import cn.itcast.reggie.mapper.DishMapper;
import cn.itcast.reggie.service.DishService;
import cn.itcast.reggie.vo.DishFlavorVO;
import cn.itcast.reggie.vo.DishVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class Dishimpl implements DishService{
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    /**
     * 分页查询菜品
     * @Param:
     * @return:
     */
    @Override
    public Result findDPage(Integer page, Integer pageSize,String name) {
        //page未传值或者page<=0则设置等于1
        if (page==null || page<=0){
            page=1;
        }
        //若pagesize未传值或pagesize>20,设置为=10
        if (pageSize==null || pageSize>20){
            pageSize=10;
        }
        IPage<Dish> ipage = new Page<>(page,pageSize);
//        QueryWrapper<Dish> wrapper = new QueryWrapper<>();
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        /*if (StringUtils.hasLength(name)){
            wrapper.like(Dish::getName,name);
        }*/
        //参数一：返回Boolean的表达式，返回true则添加查询条件，返回false则不添加查询条件
        //参数二:找对应的列名
        //参数三:前端传递的值
        wrapper.like(StringUtils.hasLength(name),Dish::getName,name);
        //select * form dish where is_delete
        //添加一个条件 is_delete=0 | is_delete=1
        wrapper.eq(Dish::getIsDeleted,0);
        ipage = dishMapper.selectPage(ipage,wrapper);
        //把集合中的Dish拿出来,根据Dish中的category_id从category表中查询分类信息,将dish+category=dishvo进行返回
        List<Dish> list = ipage.getRecords();
        /*List<DishVO> voList = new ArrayList<>();
        for (Dish dish : list){
            Category category = categoryMapper.selectById(dish.getCategoryId());
            DishVO vo = new DishVO();
           //vo.setId(dish.getId()); 一个个set比较麻烦
            BeanUtils.copyProperties(dish,vo); //将前一个参数中的数据拷贝到第二个对象，前提是两个对象的属性名和类型必须一致
            vo.setCategoryName(category.getName());
            voList.add(vo);
        }*/
        List<DishVO> voList = list.stream().map(dish -> {
            Category category = categoryMapper.selectById(dish.getCategoryId());
            DishVO vo = new DishVO();
            BeanUtils.copyProperties(dish,vo);
            vo.setCategoryName(category.getName());
            return vo;
        }).collect(Collectors.toList());
        //构建返回结果
        PageResult<DishVO> pageResult = new PageResult<>(voList,ipage.getTotal());
        return Result.success(pageResult);
    }

    /**
     * 添加菜品
     * @Param:
     * @return:
     */
    @Override
    public Result addDish(DishDto dishDto, Long id) {
        //校验数据
        if (dishDto == null){
            return Result.error("数据错误!");
        }
        if (!StringUtils.hasLength(dishDto.getName())){
            return Result.error("名称为空!");
        }
        if (id == null){
            return Result.error("id数据错误!");
        }
        //填补完数据
        dishDto.setCreateTime(LocalDateTime.now());
        dishDto.setUpdateTime(LocalDateTime.now());
        dishDto.setCreateUser(id);
        dishDto.setUpdateUser(id);
        dishDto.setIsDeleted(0);
        //完成dish表的新增
        dishMapper.insert(dishDto);
        //获取新增dish的主键
        long dishid = dishDto.getId();
        //获取dto中flavors集合
        List<DishFlavor> flavors = dishDto.getFlavors();
        //判断flavors集合不为空且长度不为0
        if (flavors != null && flavors.size() > 0){
            //循环遍历对象,将flavors中的数据存入dish_flavor表中
            flavors.stream().forEach(dishFlavor -> {
                dishFlavor.setDishId(dishid);
                //填补其他数据
                dishFlavor.setCreateTime(LocalDateTime.now());
                dishFlavor.setUpdateTime(LocalDateTime.now());
                dishFlavor.setCreateUser(id);
                dishFlavor.setUpdateUser(id);
                dishFlavor.setIsDeleted(0);
                dishFlavorMapper.insert(dishFlavor);
            });
        }
        return Result.success(null);
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
        QueryWrapper<Dish> wrapper = new QueryWrapper<>();
        wrapper.eq("id",id);
//        wrapper.select("id","name","category_Id","price","image","description","sort","code","update_time","update_user");
        Dish dish = dishMapper.selectOne(wrapper);

        QueryWrapper<DishFlavor> queryWrapper = new QueryWrapper<>();
        /*LambdaQueryWrapper<DishFlavor> flavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
        flavorLambdaQueryWrapper.eq(DishFlavor::getDishId,dish.getId());*/
        queryWrapper.eq("dish_id",id);
//        queryWrapper.select("id","name","value");
        List<DishFlavor> flavor = dishFlavorMapper.selectList(queryWrapper);

        DishVO voList = new DishVO();
        voList.setFlavors(flavor);
        BeanUtils.copyProperties(dish,voList);
//        voList.setDish(dish);
        /*voList.setName(dish.getName());
        voList.setSort(dish.getSort());
        voList.setCategoryId(dish.getCategoryId());
        voList.setId(dish.getId());
        voList.setPrice(dish.getPrice());
        voList.setImage(dish.getImage());
        voList.setDescription(dish.getDescription());
        voList.setUpdateTime(dish.getUpdateTime());
        voList.setUpdateUser(dish.getUpdateUser());
        voList.setCreateTime(dish.getCreateTime());
        voList.setCreateUser(dish.getCreateUser());*/
//        voList.setCode(dish.getCode());
        /*voList.setIsDeleted(dish.getIsDeleted());
        voList.setStatus(dish.getStatus());*/

        return Result.success(voList);
    }


    /**
     * 修改菜品
     * @Param:
     * @return:
     */
    @Override
    public Result updateDish(DishDto dishDto, Long empId) {
        //参数校验
        if (dishDto == null) {
            return Result.error("数据错误!");
        }
        /*if (!StringUtils.hasLength(dishDto.getName())) {
            return Result.error("名称为空!");
        }*/
        if (empId == null) {
            return Result.error("id数据错误!");
        }
        Long temp = dishDto.getSort();
        if (temp == null) {
            return Result.error("排序为空！");
        }
        //填补数据
//        dishDto.setCreateTime(LocalDateTime.now());
        dishDto.setUpdateTime(LocalDateTime.now());
//        dishDto.setCreateUser(empId);
        dishDto.setUpdateUser(empId);
//        dishDto.setIsDeleted(0);
        int i = dishMapper.updateById(dishDto);
        //处理口味表
        long dishid = dishDto.getId();
        List<DishFlavor> flavors = dishDto.getFlavors();

        /*LambdaQueryWrapper<DishFlavor> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DishFlavor::getDishId,dishDto.getId());
        dishFlavorMapper.delete(wrapper);*/

        HashMap<String, Object> map = new HashMap<>();
        map.put("dish_id",dishDto.getId());
        int x = dishFlavorMapper.deleteByMap(map);
        if (x == 0) {
            System.out.println(dishDto.getId());
            System.out.println("未删除原始数据");
        }
        //判断flavors集合不为空且长度不为0
        //循环遍历对象,将flavors中的数据存入dish_flavor表中
        flavors.stream().forEach(dishFlavor -> {
            dishFlavor.setDishId(dishid);
            //填补其他数据
            dishFlavor.setCreateTime(LocalDateTime.now());
            dishFlavor.setUpdateTime(LocalDateTime.now());
            dishFlavor.setCreateUser(empId);
            dishFlavor.setUpdateUser(empId);
            dishFlavor.setIsDeleted(0);
            dishFlavorMapper.updateById(dishFlavor);
        });
        if (i <= 0){
            return Result.error("更新失败!!");
        }
        return Result.success(null);
    }

    /**
     * 修改状态
     * @Param:
     * @return:
     */
    @Override
    public Result updateStatus(Integer status, String ids,Long currentid) {
        if (!StringUtils.hasLength(ids)){
            return Result.error("ids不存在");
        }
        String[] idArr = ids.split(",");
        Arrays.stream(idArr).forEach(id->{
            //第一种
            /*Dish dish = new Dish();
            dish.setId(Long.valueOf(id));
            dish.setStatus(status);
            dish.setUpdateTime(LocalDateTime.now());
            dish.setUpdateUser(currentid);
            dishMapper.updateById(dish);*/
            //第二种
            LambdaUpdateWrapper<Dish> wrapper = new LambdaUpdateWrapper<>();
            wrapper.eq(Dish::getId,Long.valueOf(ids));
            wrapper.set(Dish::getStatus,status)
                    .set(Dish::getUpdateTime,LocalDateTime.now())
                    .set(Dish::getUpdateUser,currentid);

            dishMapper.update(null,wrapper);
        });

        return Result.success(null);
    }

    /**
     * 删除菜品
     * @Param:
     * @return:
     */
    @Override
    public Result deleteById(String ids, Long currentId) {
        if (!StringUtils.hasLength(ids)){
            return Result.error("ids为空!");
        }
        String[] idArr = ids.split(",");
        Arrays.stream(idArr).forEach(id->{
            LambdaUpdateWrapper<Dish> wrapper = new LambdaUpdateWrapper<>();
            wrapper.eq(Dish::getId,Long.valueOf(ids));
            wrapper.set(Dish::getIsDeleted,1)
                    .set(Dish::getUpdateTime,LocalDateTime.now())
                    .set(Dish::getUpdateUser,currentId);
            dishMapper.update(null,wrapper);
        });
        return Result.success(null);
    }
}
