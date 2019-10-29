package com.k365.video_manager_api.controller;


import com.k365.user_service.UserLevelService;
import com.k365.video_base.model.po.UserLevel;
import com.k365.video_base.common.ResultFactory;
import com.k365.video_common.annotation.SysLogs;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Gavin
 * @since 2019-07-17
 */
@RestController
@RequestMapping("/user/user-level")
@Api(tags = {"用户等级管理"})
public class UserLevelController {

    @Autowired
    private UserLevelService userLevelService;

    @ApiOperation(value = "查询所有用户等级")
    @GetMapping("/list")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String list(){
        return ResultFactory.buildSuccessResult(userLevelService.findAll());
    }

    @ApiOperation(value = "新增用户等级")
    @PostMapping("/add")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String add(@RequestBody UserLevel userLevel){
        userLevelService.add(userLevel);
        return ResultFactory.buildSuccessResult();
    }

    @ApiOperation(value = "修改用户等级")
    @PostMapping("/update")
    @SysLogs("修改用户等级")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String update(@RequestBody UserLevel userLevel){
        userLevelService.update(userLevel);
        return ResultFactory.buildSuccessResult();
    }

    @ApiOperation(value = "删除用户等级")
    @PostMapping("/remove/{id}")
    @SysLogs("删除用户等级")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String remove(@PathVariable("id") Integer id ){
        userLevelService.remove(id);
        return ResultFactory.buildSuccessResult();
    }

}

