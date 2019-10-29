package com.k365.manager_service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.k365.global.ShiroService;
import com.k365.manager_service.SysResourceService;
import com.k365.manager_service.SysRoleResourceService;
import com.k365.manager_service.SysUserService;
import com.k365.manager_service.VSysUserRoleResourceService;
import com.k365.video_base.mapper.SysResourceMapper;
import com.k365.video_base.model.dto.SysResourceDTO;
import com.k365.video_base.model.po.SysResource;
import com.k365.video_base.model.po.VSysUserRoleResource;
import com.k365.video_base.model.vo.SysResourceVO;
import com.k365.video_base.model.vo.SysUserVO;
import com.k365.video_common.constant.ResourceTypeEnum;
import com.k365.video_common.exception.ResponsiveException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.ListUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.*;

/**
 * @author Gavin
 * @date 2019/6/29 21:33
 * @description：
 */
@Service
@Transactional
@Slf4j
public class SysResourceServiceImpl extends ServiceImpl<SysResourceMapper, SysResource>
        implements SysResourceService {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private VSysUserRoleResourceService vSysUserRoleResourceService;

    @Autowired
    private SysRoleResourceService sysRoleResourceService;

    @Autowired
    private ShiroService shiroService;

    @Override
    public List<SysResource> list() {
        return this.list(new QueryWrapper<SysResource>()
                .orderByDesc("type_code").orderByAsc("sort"));
    }

    public List<SysResourceVO> getMenuVoListAndSort() {
        List<SysResource> sysResourceList = this.page(new Page<>(), new QueryWrapper<SysResource>()
                .orderByAsc("sort")).getRecords();

        List<SysResourceVO> resultVoList = new ArrayList<>();
        if (ListUtils.isEmpty(sysResourceList))
            return resultVoList;

        //找出一级菜单
        Map<Integer, SysResourceVO> map = new HashMap<>();
        List<SysResourceVO> menus = new ArrayList<>();
        sysResourceList.forEach(sysResource -> {
            if (sysResource.getTypeCode() == 0 && sysResource.getParentId() == 0) {
                SysResourceVO vo = new SysResourceVO();
                BeanUtils.copyProperties(sysResource, vo);
                vo.setSubmenus(new ArrayList<>());
                //一级菜单
                menus.add(vo);
                map.put(sysResource.getId(), vo);
            }
        });

        //一级菜单排序
        menus.sort(Comparator.comparing(SysResourceVO::getSort));

        sysResourceList.forEach(sysResource -> {
            if (sysResource.getTypeCode() == 0 && sysResource.getParentId() != 0) {
                //二级菜单
                SysResourceVO parentMenus = map.get(sysResource.getParentId());
                if (parentMenus != null && !ListUtils.isEmpty(parentMenus.getSubmenus())) {
                    SysResourceVO vo = new SysResourceVO();
                    BeanUtils.copyProperties(sysResource, vo);
                    parentMenus.getSubmenus().add(vo);
                    //二级菜单排序
                    parentMenus.getSubmenus().sort(Comparator.comparing(SysResourceVO::getSort));
                }
            }
        });




        return resultVoList;
    }

    @Override
    public List<SysResourceVO> findResourceByUsername(ServletRequest request, ServletResponse response) {
        SysUserVO currentUser = sysUserService.getCurrentSysUser(request);

        List<VSysUserRoleResource> vSysUserRoleResources =
                vSysUserRoleResourceService.findListByUsername(currentUser.getUsername());

        List<SysResourceVO> result = new ArrayList<>();
        Set<Integer> resourceIds = new HashSet<>();
        Map<Integer, SysResourceVO> map = new HashMap<>();
        //遍历一级菜单
        vSysUserRoleResources.forEach(vSysUserRoleResource -> {
            if (vSysUserRoleResource.getResourceId() != null
                    && !resourceIds.contains(vSysUserRoleResource.getResourceId())
                    && vSysUserRoleResource.getResourceTypeCode() != null
                    && ResourceTypeEnum.MENU.key() == vSysUserRoleResource.getResourceTypeCode()) {

                SysResourceVO resourceVO = SysResourceVO.builder().url(vSysUserRoleResource.getResourceUrl())
                        .icon(vSysUserRoleResource.getResourceIcon())
                        .name(vSysUserRoleResource.getResourceName())
                        .sort(vSysUserRoleResource.getResourceSort())
                        .id(vSysUserRoleResource.getResourceId())
                        .parentId(vSysUserRoleResource.getResourceParentId())
                        .typeCode(vSysUserRoleResource.getResourceTypeCode())
                        .permitCode(vSysUserRoleResource.getResourcePermitCode())
                        .verification(vSysUserRoleResource.getResourceVerification())
                        .submenus(new ArrayList<>()).build();

                resourceIds.add(vSysUserRoleResource.getResourceId());
                result.add(resourceVO);
                map.put(vSysUserRoleResource.getResourceId(), resourceVO);
            }
        });
        //一级菜单排序
        result.sort(Comparator.comparing(SysResourceVO::getSort));

        //遍历二级菜单
        vSysUserRoleResources.forEach(vSysUserRoleResource -> {
            if (vSysUserRoleResource.getResourceId() != null
                    && !resourceIds.contains(vSysUserRoleResource.getResourceId())
                    && vSysUserRoleResource.getResourceTypeCode() != null
                    && ResourceTypeEnum.SUBMENU.key() == vSysUserRoleResource.getResourceTypeCode()
                    && null != map.get(vSysUserRoleResource.getResourceParentId())) {

                SysResourceVO resourceVO = SysResourceVO.builder().url(vSysUserRoleResource.getResourceUrl())
                        .name(vSysUserRoleResource.getResourceName())
                        .sort(vSysUserRoleResource.getResourceSort())
                        .id(vSysUserRoleResource.getResourceId())
                        .parentId(vSysUserRoleResource.getResourceParentId())
                        .typeCode(vSysUserRoleResource.getResourceTypeCode())
                        .permitCode(vSysUserRoleResource.getResourcePermitCode())
                        .verification(vSysUserRoleResource.getResourceVerification())
                        .icon(vSysUserRoleResource.getResourceIcon())
                        .build();


                map.get(vSysUserRoleResource.getResourceParentId()).getSubmenus().add(resourceVO);
                resourceIds.add(vSysUserRoleResource.getResourceId());
                //二级菜单排序
                map.get(vSysUserRoleResource.getResourceParentId())
                        .getSubmenus().sort(Comparator.comparing(SysResourceVO::getSort));
            }
        });

        return result;
    }


    @Override
    public List<SysResourceVO> findButtonByParentId(ServletRequest request, ServletResponse response,
                                                    SysResourceDTO sysResourceDTO) {

        SysUserVO currentUser = sysUserService.getCurrentSysUser(request);

        List<VSysUserRoleResource> vSysUserRoleResources =
                vSysUserRoleResourceService.findButtonByParentId(currentUser.getUsername(), sysResourceDTO.getParentId());

        List<SysResourceVO> result = new ArrayList<>();
        Set<Integer> resourceIds = new HashSet<>();
        if (!ListUtils.isEmpty(vSysUserRoleResources)) {
            vSysUserRoleResources.forEach(vSysUserRoleResource -> {
                if (!resourceIds.contains(vSysUserRoleResource.getResourceId())) {
                    SysResourceVO resourceVO = SysResourceVO.builder().url(vSysUserRoleResource.getResourceUrl())
                            .name(vSysUserRoleResource.getResourceName())
                            .sort(vSysUserRoleResource.getResourceSort())
                            .id(vSysUserRoleResource.getResourceId())
                            .build();

                    resourceIds.add(vSysUserRoleResource.getResourceId());
                    result.add(resourceVO);
                }
            });
        }

        return result;
    }

    @Override
    public List<SysResourceVO> findResourcePermits() {
        List<SysResource> resources = this.list(
                new QueryWrapper<SysResource>()
//                        .eq("verification", Boolean.TRUE)
                        .orderByAsc("type_code").orderByAsc("sort"));

        return getSysResourceVOList(resources);
    }

    private List<SysResourceVO> getSysResourceVOList(List<SysResource> resources) {
        List<SysResourceVO> voList = new LinkedList<>();
        if (!ListUtils.isEmpty(resources)) {
            Map<Integer, SysResourceVO> map = new HashMap<>();

            resources.forEach(resource -> {
                SysResourceVO vo = SysResourceVO.builder()
                        .id(resource.getId())
                        .name(resource.getName())
                        .submenus(new LinkedList<>())
                        .parentId(resource.getParentId())
                        .typeCode(resource.getTypeCode())
                        .permitCode(resource.getPermitCode())
                        .verification(resource.getVerification())
                        .icon(resource.getIcon())
                        .sort(resource.getSort())
                        .url(resource.getUrl())
                        .build();

//                log.info("source id:{}",resource.getId());

                map.put(resource.getId(), vo);

                ResourceTypeEnum resourceType = ResourceTypeEnum.getResourceType(resource.getTypeCode());
                switch (resourceType) {
                    case MENU:
                        voList.add(vo);
                        break;
                    default:
//                        log.info("==>parent id:{}",resource.getParentId());
                        map.get(resource.getParentId()).getSubmenus().add(vo);
                        break;
                }
            });
        }
        return voList;
    }

    @Override
    public List<SysResourceVO> findSysUserPermits(ServletRequest request, ServletResponse response) {
        SysUserVO currentUser = sysUserService.getCurrentSysUser(request);

        List<VSysUserRoleResource> vSysUserRoleResources =
                vSysUserRoleResourceService.findListByUsername(currentUser.getUsername());


        if (ListUtils.isEmpty(vSysUserRoleResources))
            return null;

        Set<Integer> resourceIds = new HashSet<>();
        vSysUserRoleResources.forEach(v -> resourceIds.add(v.getResourceId()));

        List<SysResource> resources = this.list(
                new QueryWrapper<SysResource>().in("id", resourceIds)
                        .orderByAsc("type_code").orderByAsc("sort"));

        return getSysResourceVOList(resources);
    }

    @Override
    public void add(SysResourceDTO sysResourceDTO) {

        List<VSysUserRoleResource> list = vSysUserRoleResourceService.list(new QueryWrapper<VSysUserRoleResource>()
                .eq("resource_parent_id", sysResourceDTO.getParentId())
                .eq("resource_type_code", sysResourceDTO.getTypeCode())
                .eq("resource_permit_code", sysResourceDTO.getPermitCode()));

        if (!ListUtils.isEmpty(list))
            throw new ResponsiveException("资源已存在，如需修改请到编辑页面进行修改");

        SysResource sysResource = SysResource.builder().build();
        BeanUtils.copyProperties(sysResourceDTO, sysResource);
        sysResource.setCreateDate(new Date());
        boolean saved = this.save(sysResource);
        if(saved) {
            shiroService.reloadPerms();
        }

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public void remove(Integer resourceId, ServletRequest request, ServletResponse response) {

        SysResource sysResource = this.getById(resourceId);
        if (sysResource == null)
            throw new ResponsiveException("资源不存在或已被删除");

        this.list(new QueryWrapper<SysResource>().eq("parent_id",resourceId)).forEach(sysResource1 -> {
            this.remove(sysResource1.getId(),null,null);
            //删除子资源信息
            this.removeById(sysResource1.getId());
            //删除角色子资源关联记录
            sysRoleResourceService.removeByResourceId(sysResource1.getId());
        });

        //删除资源信息
        this.removeById(resourceId);
        //删除角色资源关联记录
        sysRoleResourceService.removeByResourceId(resourceId);
        //重新加载权限信息
        shiroService.reloadPerms();
    }


    @Override
    public List<SysResource> findByTypeCodeAndIDs(Integer typecode, Collection<Integer> ids){

        return this.list(new QueryWrapper<SysResource>().in("id",ids).eq("type_code",typecode));
    }

}
