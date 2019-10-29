package com.k365.manager_service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.k365.manager_service.SysUserRoleService;
import com.k365.video_base.mapper.SysUserRoleMapper;
import com.k365.video_base.model.po.SysUserRole;
import com.k365.video_common.exception.ResponsiveException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.ListUtils;

import java.util.List;

/**
 * @author Gavin
 * @date 2019/7/17 17:45
 * @description：
 */
@Service
@Slf4j
@Transactional(readOnly = true)
public class SysUserRoleServiceImpl extends
        ServiceImpl<SysUserRoleMapper, SysUserRole> implements SysUserRoleService {

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public void add(SysUserRole sysUserRole) {
        this.save(sysUserRole);
    }

    @Override
    public List<SysUserRole> findByRoleId(Integer roleId) {
        return this.list(new QueryWrapper<SysUserRole>().eq("sys_role_id", roleId));
    }

    @Override
    public List<SysUserRole> findByUid(String uid) {
        return this.list(new QueryWrapper<SysUserRole>().eq("sys_user_id", uid));
    }

    @Override
    public void removeByRoleId(Integer roleId) {
        List<SysUserRole> sysUserRoles = this.findByRoleId(roleId);
        if (!ListUtils.isEmpty(sysUserRoles)) {
            throw new ResponsiveException("角色正在使用中，不允许删除");
        }
        this.remove(new UpdateWrapper<SysUserRole>().eq("sys_role_id", roleId));
    }

    @Override
    public void removeBySysUserId(String userId) {
        this.remove(new UpdateWrapper<SysUserRole>().eq("sys_user_id", userId));
    }
}
