package com.k365.video_manager_api.controller;

import com.k365.manager_service.SysUserService;
import com.k365.manager_service.VSysUserRoleService;
import com.k365.video_base.model.dto.SysUserDTO;
import com.k365.video_common.annotation.SysLogs;
import com.k365.video_common.constant.UserStatusEnum;
import com.k365.video_base.common.ResultFactory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * @author Gavin
 * @date 2019/6/29 21:57
 * @description：
 */
@RestController
@RequestMapping(value = {"/manage/user"})
@Api(tags = {"系统用户管理"})
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private VSysUserRoleService vSysUserRoleService;

    @PostMapping("/list")
    @ApiOperation(value = "分页获取用户数据")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String get(@RequestBody @Validated @ApiParam(value = "用户获取过滤条件") SysUserDTO sysUserDTO) {
        return ResultFactory.buildSuccessResult(vSysUserRoleService.findPage(sysUserDTO));
    }

    @PostMapping(value = {"/add"})
    @ApiOperation(value = "添加用户")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String add(@RequestBody @Validated @ApiParam(value = "用户数据") SysUserDTO sysUserDTO) {
        sysUserService.add(sysUserDTO);
        return ResultFactory.buildSuccessResult();
    }


    @PostMapping("/get/user-info/{id}")
    @ApiOperation(value = "根据ID获取用户信息")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String getById(@PathVariable("id") @ApiParam(value = "用户ID") String id) {
        return ResultFactory.buildSuccessResult(sysUserService.findSysUserInfoById(id));
    }

    @PostMapping(value = {"/lock/{id}"})
    @ApiOperation(value = "锁定用户")
    @SysLogs("锁定用户")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String lock(@PathVariable("id") @ApiParam(value = "用户标识ID") String id) {
        sysUserService.statusChangeByUId(id, UserStatusEnum.LOCKED);
        return ResultFactory.buildSuccessResult();
    }

    @PostMapping(value = {"/unlock/{id}"})
    @ApiOperation(value = "解锁用户")
    @SysLogs("解锁用户")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String unlock(@PathVariable("id") @ApiParam(value = "用户标识ID") String id) {
        sysUserService.statusChangeByUId(id, UserStatusEnum.NORMAL);
        return ResultFactory.buildSuccessResult();
    }

    @PostMapping(value = {"/remove/{id}"})
    @ApiOperation(value = "删除用户")
    @SysLogs("删除用户")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String remove(@PathVariable("id") @ApiParam(value = "用户标识ID") String id) {
        sysUserService.removeUser(id);
        return ResultFactory.buildSuccessResult();
    }


    @PostMapping(value = {"/update/{id}"})
    @ApiOperation(value = "更新用户")
    @SysLogs("更新用户")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String update(@PathVariable("id") @ApiParam(value = "用户标识ID") String id,
                         @RequestBody @Validated @ApiParam(value = "用户数据") SysUserDTO sysUserDTO) {
        sysUserDTO.setId(id);
        sysUserService.update(sysUserDTO);
        return ResultFactory.buildSuccessResult();
    }

    @PostMapping(value = {"/reset-password"})
    @ApiOperation(value = "重置密码")
    @SysLogs("重置密码")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String resetPassword(@RequestBody @Validated @ApiParam(value = "用户及密码数据")
                                        SysUserDTO sysUserDTO, ServletRequest request, ServletResponse response) {
        sysUserService.resetPassword(request, response, sysUserDTO);
        return ResultFactory.buildSuccessResult();
    }

    @GetMapping(value = {"/regenerate-auth"})
    @ApiOperation(value = "重新生成身份验证码")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String regenerateAuth() {
        return ResultFactory.buildSuccessResult(sysUserService.regenerateAuth());
    }

    @PostMapping(value = "/current")
    @ApiOperation("获取当前用户信息")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String current(ServletRequest request) {
        return ResultFactory.buildSuccessResult(sysUserService.getCurrentSysUser(request));
    }

    @GetMapping(value = "/sign-out")
    @ApiOperation(value = "注销登录")
    @SysLogs("注销登录")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String signOut(ServletRequest request, ServletResponse response) {
        sysUserService.signOut(request, response);
        return ResultFactory.buildSuccessResult();
    }
}
