package cn.itcast.reggie.service;

import cn.itcast.reggie.common.Result;
import cn.itcast.reggie.vo.SetmealVO;

public interface SetmealService {

    /**
     * 分页查询
     * @Param:
     * @return:
     */
    public Result findSPage(Integer page, Integer pageSize, String name);

    /**
     * 添加
     * @Param:
     * @return:
     */
    Result addSetmeal(SetmealDTO dto, Long cuId);

    Result editSetmeal(SetmealDTO dto, Long cuId);

    Result deleteSetmeal(String ids, Long cuId);

    Result setmealStatusByStatus(Integer status, String ids, Long cuId);
}
