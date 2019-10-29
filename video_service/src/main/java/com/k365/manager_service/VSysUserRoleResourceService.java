package com.k365.manager_service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.k365.video_base.model.po.VSysUserRoleResource;

import java.util.List;

/**
 * @author Gavin
 * @date 2019/7/7 16:55
 * @description：
 */
public interface VSysUserRoleResourceService extends IService<VSysUserRoleResource>{

    /**
     * 根据用户名查询用户权限和角色信息
     */
    List<VSysUserRoleResource> findListByUsername(String username);

    /**
     * 根据角色id查询权限和角色信息
     */
    List<VSysUserRoleResource> findListByRoleId(Integer roleId);

    /**
     *根据用户和菜单id查询按钮
     */
    List<VSysUserRoleResource> findButtonByParentId(String username,Integer parentId);

    /**
     * 根据资源id查询
     */
    List<VSysUserRoleResource> findListByResourceId(Integer resourceId);

}
