package com.k365.video_manager_api.controller;

import com.k365.global.ShiroService;
import com.k365.manager_service.SysResourceService;
import com.k365.video_base.model.dto.SysResourceDTO;
import com.k365.video_base.model.po.SysResource;
import com.k365.video_common.annotation.SysLogs;
import com.k365.video_base.common.ResultFactory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * @author Gavin
 * @date 2019/7/9 18:19
 * @description：
 */

@RestController
@RequestMapping(value = {"/sys/resource"})
@Api(tags = {"系统资源管理"})
public class SysResourceController {

    @Autowired
    private SysResourceService sysResourceService;

    @Autowired
    private ShiroService shiroService;

    @GetMapping("/get/menus")
    @ApiOperation(value = "获取菜单资源")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String getMenus(ServletRequest request, ServletResponse response) {

        return ResultFactory.buildSuccessResult(sysResourceService.findResourceByUsername(request, response));
    }

    @PostMapping("/get/buttons")
    @ApiOperation(value = "获取二级菜单下的按钮")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String getButtons(ServletRequest request, ServletResponse response,
                             @RequestBody SysResourceDTO sysResourceDTO) {

        return ResultFactory.buildSuccessResult(sysResourceService.findButtonByParentId(request, response, sysResourceDTO));
    }

    @GetMapping("/get/permits")
    @ApiOperation(value = "获取资源权限列表")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String getPermits() {

        return ResultFactory.buildSuccessResult(sysResourceService.findResourcePermits());
    }

    @GetMapping("/get/user-permits")
    @ApiOperation(value = "获取资源用户权限列表")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String getSysUserPermits(ServletRequest request, ServletResponse response) {

        return ResultFactory.buildSuccessResult(sysResourceService.findSysUserPermits(request,response));
    }

    @PostMapping("/add")
    @ApiOperation(value = "添加资源")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String add(@RequestBody SysResourceDTO sysResourceDTO) {
        sysResourceService.add(sysResourceDTO);
        return ResultFactory.buildSuccessResult();
    }

    @PostMapping("/remove/{id}")
    @ApiOperation(value = "删除资源")
    @SysLogs("删除资源")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String remove(@PathVariable("id") Integer id,ServletRequest request, ServletResponse response) {
        sysResourceService.remove(id,request,response);
        return ResultFactory.buildSuccessResult();
    }

    @PostMapping("/update")
    @ApiOperation(value = "更新资源")
    @SysLogs("更新资源")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String update(@RequestBody SysResource sysResource) {
        shiroService.reloadPerms();
        sysResourceService.updateById(sysResource);
        return ResultFactory.buildSuccessResult();
    }

    @GetMapping("/tree")
    @ApiOperation(value = "获取全部资源")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String tree(ServletRequest request, ServletResponse response) {

        return ResultFactory.buildSuccessResult(sysResourceService.findResourcePermits());
    }

    @GetMapping("/refresh-auth")
    @ApiOperation(value = "刷新所有资源缓存")
    @SysLogs("刷新所有资源缓存")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String refreshAuth() {
        shiroService.reloadPerms();
        return ResultFactory.buildSuccessResult();
    }
}
