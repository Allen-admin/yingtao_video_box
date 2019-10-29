package com.k365.manager_service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.k365.video_base.model.dto.SysResourceDTO;
import com.k365.video_base.model.dto.SysRoleDTO;
import com.k365.video_base.model.dto.SysUserDTO;
import com.k365.video_base.model.po.SysRole;
import com.k365.video_base.model.vo.SysRoleVO;
import com.k365.video_base.model.vo.SysUserVO;

import java.util.List;
import java.util.Map;

/**
 * @author Gavin
 * @date 2019/6/30 10:43
 * @description：
 */
public interface SysRoleService extends IService<SysRole> {

    /**
     * 根据用户id查询角色
     */
    List<SysRole> findRoleByUserId(String uid);


    /**
     *分页查询角色数据
     */
    Map<String,Object> findPageList(SysRoleDTO sysRoleDTO);

    /**
     * 根据id查询role详情信息
     */
    SysRoleVO findSysRoleInfo(Integer id);

    /**
     * 添加 角色
     */
    void add(SysRoleDTO sysRoleDTO);

    /**
     * 修改 角色信息
     */
    void update(SysRoleDTO sysRoleDTO);

    /**
     * 修改角色权限信息
     */
    void updateRolePermits(SysRoleDTO sysRoleDTO);

    /**
     * 删除角色信息
     */
    void remove(Integer roleId);
}
