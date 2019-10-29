package com.k365.video_site_api.controller;


import com.k365.user_service.UserLevelService;
import com.k365.video_common.annotation.TokenVerify;
import com.k365.video_base.common.ResultFactory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Gavin
 * @since 2019-07-17
 */
@RestController
@RequestMapping("/user-level")
@Api(tags = "用户等级")
public class UserLevelController {

    @Autowired
    private UserLevelService userLevelService;

    @PostMapping(value = "/get/user-level")
    @ApiOperation(value = "获取用户等级")
    @TokenVerify
    @ApiImplicitParam(paramType = "header", name = "Token", value = "身份认证Token")
    public String getUserLevel(){

        return ResultFactory.buildSuccessResult(userLevelService.findCurrentLevel());
    }

}

