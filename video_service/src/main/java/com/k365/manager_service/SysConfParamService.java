package com.k365.manager_service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.k365.video_base.model.dto.SysConfParamDTO;
import com.k365.video_base.model.po.SysConfParam;
import com.k365.video_common.constant.SysParamNameEnum;
import com.k365.video_common.constant.SysParamValueNameEnum;

import java.util.List;
import java.util.Map;

/**
 * @author Gavin
 * @date 2019/7/2 10:08
 * @description：
 */
public interface SysConfParamService extends IService<SysConfParam> {

    /**
     * 根据参数名查询参数值
     */
    Map<String, Object> findParamValByName(SysParamNameEnum sysParamName);

    /**
     * 根据参数值名称获取值
     */
    Object getValByValName(SysParamValueNameEnum paramValueName);

    /**
     * 新增系统参数
     */
    void add(SysConfParamDTO sysConfParamDTO);

    /**
     * 根据id或名称修改系统参数
     */
    void updateByIdOrName(SysConfParamDTO sysConfParamDTO);

    /**
     * 查询所有参数
     */
    List<SysConfParam> findAll();

    /**
     * 根据Id删除参数
     */
    void remove(Integer id);

    /**
     * 根据参数名修改
     */
//    void updateByName(SysConfParamDTO sysConfParamDTO);

    /**
     * 修改系统参数
     */
//    void updateById(SysConfParamDTO sysConfParamDTO);

}
