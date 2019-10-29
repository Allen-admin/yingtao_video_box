package com.k365.manager_service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.k365.global.ShiroService;
import com.k365.video_base.common.SysUserContext;
import com.k365.manager_service.*;
import com.k365.video_base.mapper.SysRoleMapper;
import com.k365.video_base.model.dto.SysRoleDTO;
import com.k365.video_base.model.po.*;
import com.k365.video_base.model.vo.SysResourceVO;
import com.k365.video_base.model.vo.SysRoleVO;
import com.k365.video_base.model.vo.SysUserVO;
import com.k365.video_common.exception.ResponsiveException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.thymeleaf.util.ListUtils;
import org.thymeleaf.util.StringUtils;

import java.util.*;

/**
 * @author Gavin
 * @date 2019/6/30 10:44
 * @description：
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    @Autowired
    private SysUserRoleService sysUserRoleService;

    @Autowired
    private VSysUserRoleResourceService vSysUserRoleResourceService;

    @Autowired
    private SysRoleResourceService sysRoleResourceService;

    @Autowired
    private SysResourceService sysResourceService;

    @Autowired
    private ShiroService shiroService;

    @Override
    public List<SysRole> findRoleByUserId(String uid) {
        if (StringUtils.isEmpty(uid))
            return null;

        List<SysUserRole> list = sysUserRoleService.findByUid(uid);

        if (!ListUtils.isEmpty(list)) {
            Set<Integer> ids = new HashSet<>();
            list.forEach(sysUserRole -> ids.add(sysUserRole.getId()));
            List<SysRole> sysRoles = (List<SysRole>) this.listByIds(ids);
            if (!CollectionUtils.isEmpty(sysRoles)) {
                return sysRoles;
            }
        }

        return null;
    }

    @Override
    public Map<String, Object> findPageList(SysRoleDTO sysRoleDTO) {
        IPage<SysRole> page = this.page(new Page<SysRole>().setSize(sysRoleDTO.getPageSize())
                        .setCurrent(sysRoleDTO.getPage()),
                new QueryWrapper<SysRole>().orderByAsc("sort"));

        long total = page.getTotal();
        List<SysRole> list = page.getRecords();

        if (ListUtils.isEmpty(list))
            return null;

        list.forEach(sysRole -> {
            //角色权限包含菜单、按钮权限
            Collection<Integer> resourcesIDs = new ArrayList<>();
            sysRoleResourceService.findByRoleId(sysRole.getId()).forEach(sysRoleResource -> {
                resourcesIDs.add(sysRoleResource.getSysResourceId());
            });

            //过滤按钮权限
            if (resourcesIDs.size()>0){
                Collection<Integer> resIDs = new ArrayList<>();
                sysResourceService.findByTypeCodeAndIDs(3,resourcesIDs).forEach(sysResource -> {
                    resIDs.add(sysResource.getId());
                });

                sysRole.setResourcesIDs(resIDs);
            }
        });

        Map<String, Object> map = new HashMap<>();
        map.put("total", total);
        map.put("list", list);
        return map;

    }


    @Override
    public SysRoleVO findSysRoleInfo(Integer id) {
        SysRole sysRole = this.getById(id);
        if (sysRole == null) {
            throw new ResponsiveException("角色不存在或已被删除");
        }
        SysRoleVO sysRoleVO = SysRoleVO.builder().build();
        BeanUtils.copyProperties(sysRole, sysRoleVO);

        List<VSysUserRoleResource> vSysUserRoleResourceList = vSysUserRoleResourceService.findListByRoleId(id);
        if (ListUtils.isEmpty(vSysUserRoleResourceList))
            return sysRoleVO;

        Set<Integer> resourceContains = new HashSet<>();
        List<SysResourceVO> resources = new ArrayList<>();

        Set<String> sysUserContains = new HashSet<>();
        List<SysUserVO> sysUsers = new ArrayList<>();
        vSysUserRoleResourceList.forEach(v -> {
            //加载角色下权限信息
            if (!resourceContains.contains(v.getResourceId())) {
                resources.add(SysResourceVO.builder().permitCode(v.getResourcePermitCode())
                        .url(v.getResourceUrl())
                        .typeCode(v.getResourceTypeCode())
                        .id(v.getResourceId())
                        .build());
                resourceContains.add(v.getResourceId());
            }


            //加载角色下系统用户信息
            if (!sysUserContains.contains(v.getUserId())) {
                sysUsers.add(SysUserVO.builder().username(v.getUsername())
                        .status(v.getUserStatus())
                        .id(v.getUserId())
                        .build());
                sysUserContains.add(v.getUserId());
            }

        });

        sysRoleVO.setResources(resources);
        sysRoleVO.setSysUsers(sysUsers);

        return sysRoleVO;

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public void add(SysRoleDTO sysRoleDTO) {
        SysRole role = this.getOne(new QueryWrapper<SysRole>().eq("name", sysRoleDTO.getName()));
        if (role != null)
            throw new ResponsiveException("角色名称已存在");

        role = SysRole.builder().build();
        BeanUtils.copyProperties(sysRoleDTO, role);
        role.setCreateDate(new Date());

        boolean saveSuccess = this.save(role);
        if (saveSuccess) {
            //新增角色权限信息
            if (!CollectionUtils.isEmpty(sysRoleDTO.getResourcesIDs())) {
                List<SysRoleResource> sysRoleResourceList = new ArrayList<>();
                for (Integer resourceId : sysRoleDTO.getResourcesIDs()) {
                    sysRoleResourceList.add(SysRoleResource.builder().sysResourceId(resourceId).sysRoleId(role.getId()).build());
                }

                sysRoleResourceService.saveBatch(sysRoleResourceList);
            }

            //重新加载角色、权限缓存信息
            shiroService.reloadPerms();
        }

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public void update(SysRoleDTO sysRoleDTO) {
        SysRole sysRole = SysRole.builder().build();
        BeanUtils.copyProperties(sysRoleDTO, sysRole);
        this.updateById(sysRole);
        if (sysRoleDTO.getResourcesIDs()!=null && sysRoleDTO.getResourcesIDs().size()>0){
            //更新角色权限
            this.updateRolePermits(sysRoleDTO);
        }else {
            //删除角色资源关联记录
            sysRoleResourceService.removeByRoleId(sysRoleDTO.getId());
        }
        //重新加载角色、权限缓存信息
        shiroService.reloadPerms();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public void updateRolePermits(SysRoleDTO sysRoleDTO) {

        //更新角色权限
        Collection<Integer> resourceIds = sysRoleDTO.getResourcesIDs();
        if (!CollectionUtils.isEmpty(resourceIds)) {

            //删除角色资源关联记录
            sysRoleResourceService.removeByRoleId(sysRoleDTO.getId());

            List<SysRoleResource> sysRoleResourceList = new ArrayList<>();
            resourceIds.forEach(resourceId ->
                    sysRoleResourceList.add(SysRoleResource.builder()
                            .sysResourceId(resourceId).sysRoleId(sysRoleDTO.getId()).build())
            );
            //写入新权限信息
            sysRoleResourceService.saveBatch(sysRoleResourceList);
            //重新加载角色、权限缓存信息
            shiroService.reloadPerms();
        }

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public void remove(Integer roleId) {
        SysUser currentUser = SysUserContext.getCurrentSysUser();
        if (currentUser != null && !CollectionUtils.isEmpty(currentUser.getRoles())) {
            Set<Integer> roleIds = new HashSet<>();
            currentUser.getRoles().forEach(role -> roleIds.add(role.getId()));
            if (roleIds.contains(roleId))
                throw new ResponsiveException("不能删除当前用户所属角色");

        }

        //删除角色用户关联记录
        sysUserRoleService.removeByRoleId(roleId);
        //删除角色资源关联记录
        sysRoleResourceService.removeByRoleId(roleId);
        //删除角色记录
        this.removeById(roleId);
        //重新加载角色、权限缓存信息
        shiroService.reloadPerms();
    }
}
