package com.k365.manager_service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.k365.video_base.model.po.SysUser;
import com.k365.video_base.model.po.SysUserRole;

import java.util.List;

/**
 * @author Gavin
 * @date 2019/7/17 17:45
 * @description：
 */
public interface SysUserRoleService extends IService<SysUserRole>{

    /**
     * 新增一条关联记录
     * @param sysUserRole
     */
    void add(SysUserRole sysUserRole);

    /**
     * 根据角色id 查询用户
     */
    List<SysUserRole> findByRoleId(Integer roleId);

    /**
     * 根据用户名查询
     */
    List<SysUserRole> findByUid(String uid);

    /**
     * 根据角色id删除
     */
    void removeByRoleId(Integer roleId);

    /**
     * 根据用户id删除
     */
    void removeBySysUserId(String userId);

}
