package com.k365.video_manager_api.controller;

import com.k365.manager_service.SysRoleService;
import com.k365.video_base.model.dto.SysRoleDTO;
import com.k365.video_common.annotation.SysLogs;
import com.k365.video_base.common.ResultFactory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author Gavin
 * @date 2019/7/19 19:15
 * @description：
 */
@RestController
@RequestMapping(value = {"/manage/role"})
@Api(tags = {"角色管理"})
public class SysRoleController {

    @Autowired
    private SysRoleService sysRoleService;

    @PostMapping("/list")
    @ApiOperation(value = "分页获取角色数据")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String list(@RequestBody @Validated @ApiParam(value = "角色获取过滤条件") SysRoleDTO sysRoleDTO) {
        return ResultFactory.buildSuccessResult(sysRoleService.findPageList(sysRoleDTO));
    }

    @PostMapping("/get/role-info/{id}")
    @ApiOperation(value = "获取角色详情")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String getRoleInfo(@PathVariable("id") @ApiParam(value = "角色id") Integer id){
        return ResultFactory.buildSuccessResult(sysRoleService.findSysRoleInfo(id));
    }

    @PostMapping("/add")
    @ApiOperation(value = "新增角色")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String add(@RequestBody @Validated(SysRoleDTO.Add.class) SysRoleDTO sysRoleDTO){
        sysRoleService.add(sysRoleDTO);
        return ResultFactory.buildSuccessResult();
    }

    @PostMapping("/update")
    @ApiOperation(value = "编辑角色信息")
    @SysLogs("编辑角色信息")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String update(@RequestBody @Validated(SysRoleDTO.Update.class) SysRoleDTO sysRoleDTO){
        sysRoleService.update(sysRoleDTO);
        return ResultFactory.buildSuccessResult();
    }

    @PostMapping("/update-permit")
    @ApiOperation(value = "编辑角色权限信息")
    @SysLogs("编辑角色权限信息")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String updatePermit(@RequestBody @Validated(SysRoleDTO.Update.class) SysRoleDTO sysRoleDTO){
        sysRoleService.updateRolePermits(sysRoleDTO);
        return ResultFactory.buildSuccessResult();
    }

    @PostMapping("/remove/{id}")
    @ApiOperation(value = "删除角色信息")
    @SysLogs("删除角色信息")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String remove(@PathVariable @ApiParam(value = "角色标识ID") Integer id){
        sysRoleService.remove(id);
        return ResultFactory.buildSuccessResult();
    }
}
