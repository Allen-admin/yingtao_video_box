package com.k365.video_site_api.controller;


import com.k365.user_service.UserVideoChannelService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Gavin
 * @since 2019-08-12
 */
@RestController
@RequestMapping("/user-video-channel")
@Api(tags = "用户已选频道")
public class UserVideoChannelController {

    /*@Autowired
    private UserVideoChannelService userVideoChannelService;

    @GetMapping(value = "/get")
    @ApiOperation(value = "获取APP头部频道列表")
    @TokenVerify
    @Armor
    @ApiImplicitParam(paramType = "header", name = "Token", value = "身份认证Token")
    public String get() {
        return ResultFactory.buildSuccessResult(userVideoChannelService.getUserVideoChannel());
    }*/

}

