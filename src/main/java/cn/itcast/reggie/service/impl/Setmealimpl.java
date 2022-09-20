package com.itcast.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itcast.reggie.common.*;
import com.itcast.reggie.dto.SetmealDTO;
import com.itcast.reggie.mapper.CategoryMapper;
import com.itcast.reggie.mapper.SetmealDishMapper;
import com.itcast.reggie.mapper.SetmealMapper;
import com.itcast.reggie.service.SetmealService;
import com.itcast.reggie.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class Setmealimpl implements SetmealService {

    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;

    /**
     * 分页查询
     * @Param:
     * @return:
     */
    @Override
    public Result findSPage(Integer page, Integer pageSize, String name) {
        if (page == null || page <= 0){
            page = 1;
        }
        if (pageSize == null || pageSize > 20){
            pageSize = 10;
        }

        IPage<Setmeal> iPage = new Page<>(page,pageSize);
        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasLength(name),Setmeal::getName,name);
        wrapper.eq(Setmeal::getIsDeleted,0);
        iPage = setmealMapper.selectPage(iPage,wrapper);
        List<Setmeal> list = iPage.getRecords();

        List<SetmealVO> voList = list.stream().map(setmeal -> {
            Category category = categoryMapper.selectById(setmeal.getCategoryId());
            SetmealVO setmealVO = new SetmealVO();
            BeanUtils.copyProperties(setmeal,setmealVO);
            setmealVO.setCategoryName(category.getName());
            return setmealVO;
        }).collect(Collectors.toList());

        PageResult<SetmealVO> pageResult = new PageResult<>(voList,iPage.getTotal());
        return Result.success(pageResult);
    }

    @Override
    public Result addSetmeal(SetmealDTO dto, Long cuId) {
        if(dto==null) return Result.error("参数不合法");
        //填补其他数据
        dto.setCreateTime(LocalDateTime.now());
        dto.setUpdateTime(LocalDateTime.now());
        dto.setCreateUser(cuId);
        dto.setUpdateUser(cuId);
        dto.setIsDeleted(0);
        SetmealMapper.insert(dto);
        Long setmealId = dto.getId();

        List<SetmealDish> setmealDishList = dto.getSetmealDishes();
        if(setmealDishList!=null && setmealDishList.size()>0){
            setmealDishList.stream().forEach(setmealDish -> {
                setmealDish.setSetmealId(setmealId);
                //填补其他数据
                setmealDish.setCreateTime(LocalDateTime.now());
                setmealDish.setUpdateTime(LocalDateTime.now());
                setmealDish.setCreateUser(cuId);
                setmealDish.setUpdateUser(cuId);
                setmealDish.setIsDeleted(0);
                SetmealDishMapper.insert(setmealDish);
            });
        }
        return Result.success(null);
    }

    @Override
    public Result editSetmeal(SetmealDTO dto, Long cuId) {
        if(dto==null) return Result.error("参数不合法");
        //填补其他数据
        dto.setUpdateTime(LocalDateTime.now());
        dto.setUpdateUser(cuId);
        dto.setIsDeleted(0);
        SetmealMapper.updateById(dto);

        Long setmealId = dto.getId();
        QueryWrapper<SetmealDish> wrapper = new QueryWrapper<>();
        wrapper.eq("setmeal_id",setmealId);
        SetmealDishMapper.delete(wrapper);

        List<SetmealDish> setmealDishList = dto.getSetmealDishes();
        if(setmealDishList!=null && setmealDishList.size()>0){
            setmealDishList.stream().forEach(setmealDish -> {
                setmealDish.setSetmealId(setmealId);
                //填补其他数据
                setmealDish.setCreateTime(LocalDateTime.now());
                setmealDish.setCreateUser(cuId);
                setmealDish.setUpdateTime(LocalDateTime.now());
                setmealDish.setUpdateUser(cuId);
                setmealDish.setIsDeleted(0);
                SetmealDishMapper.insert(setmealDish);
            });
        }
        return Result.success(null);
    }

    @Override
    public Result deleteSetmeal(String ids, Long cuId) {
        String[] idArr = ids.split(",");
        for(int i=0;i<idArr.length;i++){
            Setmeal setmeal = new Setmeal();
            setmeal.setIsDeleted(1);
            setmeal.setUpdateTime(LocalDateTime.now());
            setmeal.setUpdateUser(cuId);
            QueryWrapper<Setmeal> wrapper = new QueryWrapper<>();
            wrapper.eq("id",idArr[i]);
            SetmealMapper.update(setmeal,wrapper);

            SetmealDish setmealDish = new SetmealDish();
            setmealDish.setIsDeleted(1);
            setmealDish.setUpdateTime(LocalDateTime.now());
            setmealDish.setUpdateUser(cuId);
            QueryWrapper<SetmealDish> wrapper1 = new QueryWrapper<>();
            wrapper1.eq("setmeal_id",idArr[i]);
            SetmealDishMapper.update(setmealDish,wrapper1);
        }

        return Result.success(null);
    }

    @Override
    public Result setmealStatusByStatus(Integer status, String ids, Long cuId) {
        String[] idArr = ids.split(",");
        for(int i=0;i<idArr.length;i++){
            Setmeal setmeal = new Setmeal();
            setmeal.setStatus(status);
            setmeal.setUpdateTime(LocalDateTime.now());
            setmeal.setUpdateUser(cuId);
            QueryWrapper<Setmeal> wrapper = new QueryWrapper<>();
            wrapper.eq("id",idArr[i]);
            SetmealMapper.update(setmeal,wrapper);
        }

        return Result.success(null);
    }
    
    @Override
    public Result querySetmealById(Long setmealId) {
        if(setmealId==null) return Result.error("没有主键值");
        QueryWrapper<Setmeal> wrapper = new QueryWrapper<>();
        wrapper.eq("id",setmealId);
        wrapper.select("id","name","category_id","price","image","description");
        Setmeal setmeal = SetmealMapper.selectOne(wrapper);
        //拿到全部菜品信息
        Map map = new HashMap<>();
        map.put("id",setmeal.getId()+"");
        map.put("name",setmeal.getName());
        map.put("price",setmeal.getPrice());
        map.put("image",setmeal.getImage());
        map.put("description",setmeal.getDescription());
        map.put("categoryName",CategoryMapper.selectById(setmeal.getCategoryId()).getName());
        map.put("categoryId",setmeal.getCategoryId()+"");

        QueryWrapper<SetmealDish> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("setmeal_id",setmeal.getId()+"");
        System.out.println(setmeal.getId());
        List<SetmealDish> setmealDishes = SetmealDishMapper.selectList(wrapper1);
        System.out.println(setmealDishes.size());

        map.put("setmealDishes",setmealDishes);
        return Result.success(map);
    }
}
