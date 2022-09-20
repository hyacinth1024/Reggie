package cn.itcast.reggie.service;

import cn.itcast.reggie.common.Result;
import cn.itcast.reggie.dto.DishDto;

public interface DishService {
    /**
     * 查询菜品
     * @Param:
     * @return:
     */
    public Result findDPage(Integer page, Integer pageSize,String name);

    /**
     * 添加菜品
     * @Param:
     * @return:
     */
    public Result addDish(DishDto dishDto, Long id);

    /**
     * 按照id查询
     * @Param:
     * @return:
     */
    public Result findById(Long id);

    /**
     * 修改菜品
     * @Param:
     * @return:
     */
    public Result updateDish(DishDto dishDto, Long empId);

    /**
     * 修改状态
     * @Param:
     * @return:
     */
    public Result updateStatus(Integer status, String ids,Long currentid);

    /**
     * 删除菜品
     * @Param:
     * @return:
     */
    public Result deleteById(String ids, Long currentId);
}
