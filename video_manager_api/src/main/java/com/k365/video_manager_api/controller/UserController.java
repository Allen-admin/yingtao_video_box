package com.k365.video_manager_api.controller;


import com.k365.user_service.UserService;
import com.k365.video_base.common.ResultFactory;
import com.k365.video_base.model.dto.UserDTO;
import com.k365.video_base.model.po.User;
import com.k365.video_base.model.so.UserSO;
import com.k365.video_common.annotation.SysLogs;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Gavin
 * @since 2019-07-17
 */
@RestController
@RequestMapping("/user/user-list")
@Api(tags = {"用户列表管理"})
public class UserController {

    @Autowired
    public UserService userService;

    @PostMapping(value = "/list")
    @ApiOperation(value = "用户信息查询")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String findAll(@RequestBody @Validated @ApiParam("用户信息查询") UserDTO userDTO) {
        return ResultFactory.buildSuccessResult(userService.findAll(userDTO));

    }

    @PostMapping("/update")
    @ApiOperation(value = "修改用户信息")
    @SysLogs("修改用户信息")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String update(@RequestBody @Validated @ApiParam("修改用户信息") User user) {
        userService.updateUserById(user);
        return ResultFactory.buildSuccessResult();
    }

    @PostMapping("/get/{id}")
    @ApiOperation(value = "根据id查询用户")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String findById(@PathVariable("id") String id) {
        return ResultFactory.buildSuccessResult(userService.findById(id));
    }

    @PostMapping("/search")
    @ApiOperation(value = "模糊查詢")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String search(@RequestBody UserSO userSO) {
        return ResultFactory.buildSuccessResult(userService.search(userSO));
    }

    @PostMapping("/remove/{id}")
    @ApiOperation(value = "根据id删除用户")
    @SysLogs("根据id删除用户")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String removeById(@PathVariable("id") String id) {
        userService.remove(id);
        return ResultFactory.buildSuccessResult();
    }


}

