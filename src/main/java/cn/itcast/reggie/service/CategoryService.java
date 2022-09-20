package cn.itcast.reggie.service;

import cn.itcast.reggie.common.Result;
import cn.itcast.reggie.entity.Category;

public interface CategoryService {
    /**
     * 分类查询菜品
     * @Param:
     * @return:
     */
    public Result findCPage(Integer page, Integer pagesize);

    /**
     * 添加菜品
     * @Param:
     * @return:
     */
    public Result addCategory(Category category, long id);

    public Result deleById(Long id);

    /**
     * 修改菜品
     * @Param: null
     * @return:
     */
    public Result updateCategory(Category category, Long id);

    public Result findListByType(Integer type);
}
