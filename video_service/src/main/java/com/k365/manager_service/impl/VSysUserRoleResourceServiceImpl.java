package com.k365.manager_service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.k365.manager_service.VSysUserRoleResourceService;
import com.k365.video_base.mapper.VSysUserRoleResourceMapper;
import com.k365.video_base.model.po.VSysUserRoleResource;
import com.k365.video_common.constant.ResourceTypeEnum;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Gavin
 * @date 2019/7/7 16:56
 * @description：
 */
@Service
public class VSysUserRoleResourceServiceImpl extends
        ServiceImpl<VSysUserRoleResourceMapper,VSysUserRoleResource> implements VSysUserRoleResourceService {


    @Override
    public List<VSysUserRoleResource> findListByUsername(String username) {
        List<VSysUserRoleResource> vSysUserRolePermits = this.list(
                new QueryWrapper<VSysUserRoleResource>().eq("username", username).or()
                        .eq("resource_verification",false).orderByAsc("resource_sort"));

        return vSysUserRolePermits;
    }

    @Override
    public List<VSysUserRoleResource> findListByRoleId(Integer roleId) {
        return this.list(new QueryWrapper<VSysUserRoleResource>().eq("role_id",roleId)
                .orderByAsc("resource_sort"));
    }

    @Override
    public List<VSysUserRoleResource> findButtonByParentId(String username, Integer parentId) {

        List<VSysUserRoleResource> vSysUserRoleResources = this.list(
                new QueryWrapper<VSysUserRoleResource>()
                        .eq("username", username)
                        .eq("resource_type_code", ResourceTypeEnum.BUTTON.key())
                        .eq("resource_parent_id", parentId)
                        .or().eq("resource_verification",false)
                        .orderByAsc("resource_sort"));

        return vSysUserRoleResources;
    }

    @Override
    public List<VSysUserRoleResource> findListByResourceId(Integer resourceId) {
        return this.list(new QueryWrapper<VSysUserRoleResource>()
                .eq("resource_id", resourceId).orderByAsc("resource_type_code"));
    }


}
