package com.k365.manager_service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.k365.manager_service.SysRoleResourceService;
import com.k365.video_base.mapper.SysRoleResourceMapper;
import com.k365.video_base.model.po.SysRoleResource;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @author Gavin
 * @date 2019/7/21 15:24
 * @description：
 */
@Service
public class SysRoleResourceServiceImpl extends
        ServiceImpl<SysRoleResourceMapper, SysRoleResource> implements SysRoleResourceService {


    @Override
    public void removeByRoleId(Integer roleId) {
        this.remove(new UpdateWrapper<SysRoleResource>().eq("sys_role_id", roleId));
    }

    @Override
    public void removeByResourceId(Integer resourceId) {
        this.remove(new UpdateWrapper<SysRoleResource>().eq("sys_resource_id",resourceId));
    }

    @Override
    public List<SysRoleResource> findByRoleId(Integer roleId){
       return this.list(new QueryWrapper<SysRoleResource>().eq("sys_role_id",roleId));
    }
}
