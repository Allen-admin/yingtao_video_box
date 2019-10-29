package com.k365.video_site_api.controller;


import com.k365.video_common.annotation.Armor;
import com.k365.video_common.annotation.TokenVerify;
import com.k365.video_common.constant.OSEnum;
import com.k365.video_base.common.ResultFactory;
import com.k365.video_service.VideoChannelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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
@RequestMapping("/video-channel")
@Api(tags = {"视频频道"})
public class VideoChannelController {

    @Autowired
    private VideoChannelService videoChannelService;

    @GetMapping("/get/all")
    @ApiOperation("获取所有视频频道")
    @TokenVerify
    @Armor(target = {OSEnum.IOS,OSEnum.ANDROID})
    @ApiImplicitParam(paramType = "header", name = "Token", value = "身份认证Token")
    public String getAllChannel() {

        return ResultFactory.buildSuccessResult(videoChannelService.getAllChannels());
    }

}

