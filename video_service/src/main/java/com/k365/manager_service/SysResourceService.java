package com.k365.manager_service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.k365.video_base.model.dto.SysResourceDTO;
import com.k365.video_base.model.po.SysResource;
import com.k365.video_base.model.vo.SysResourceVO;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.Collection;
import java.util.List;

/**
 * @author Gavin
 * @date 2019/6/29 21:32
 * @description：
 */
public interface SysResourceService extends IService<SysResource> {
    /**
     * 获取资源列表
     *
     * @return
     */
    List<SysResource> list();


    /**
     * 根據用户名查询菜单资源
     *
     * @return
     */
    List<SysResourceVO> findResourceByUsername(ServletRequest request, ServletResponse response);

    /**
     * 根据resourceid查询旗下按钮
     */
    List<SysResourceVO> findButtonByParentId(ServletRequest request, ServletResponse response, SysResourceDTO sysResourceDTO);

    /**
     * 获取资源权限列表
     */
    List<SysResourceVO> findResourcePermits();

    /**
     * 查询用户拥有的权限
     */
    List<SysResourceVO> findSysUserPermits(ServletRequest request, ServletResponse response);

    /**
     * 新增资源
     */
    void add(SysResourceDTO sysResourceDTO);

    /**
     * 删除资源
     */
    void remove(Integer resourceId,ServletRequest request, ServletResponse response);

    /**
     * 根据ID集合，查询指定类型的资源
     */
    List<SysResource> findByTypeCodeAndIDs(Integer typecode, Collection<Integer> ids);

}
