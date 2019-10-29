package com.k365.manager_service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.k365.video_base.model.po.SysRoleResource;

import java.util.List;

/**
 * @author Gavin
 * @date 2019/7/21 15:23
 * @description：
 */
public interface SysRoleResourceService extends IService<SysRoleResource>{

    /**
     * 根据角色id 删除
     */
    void removeByRoleId(Integer roleId);

    /**
     * 根据资源id 删除
     */
    void removeByResourceId(Integer resourceId);

    /**
     * 根据角色id 查询角色权限
     */
    List<SysRoleResource> findByRoleId(Integer roleId);
}
