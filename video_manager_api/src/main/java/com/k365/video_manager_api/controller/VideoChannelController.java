package com.k365.video_manager_api.controller;


import com.k365.video_base.model.po.VideoChannel;
import com.k365.video_common.annotation.SysLogs;
import com.k365.video_base.common.ResultFactory;
import com.k365.video_service.VideoChannelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Gavin
 * @since 2019-08-12
 */
@RestController
@RequestMapping("/video-source/video-channel")
@Api(tags = "视频频道管理")
public class VideoChannelController {

    @Autowired
    private VideoChannelService videoChannelService;

    @GetMapping("/list")
    @ApiOperation(value = "查询视频频道")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String getList() {
        return ResultFactory.buildSuccessResult(videoChannelService.gatList());
    }

    @PostMapping("/add")
    @ApiOperation(value = "新增视频频道")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String add(@RequestBody VideoChannel videoChannel) {
        videoChannelService.add(videoChannel);
        return ResultFactory.buildSuccessResult();
    }

    @PostMapping("/update")
    @ApiOperation(value = "修改视频频道")
    @SysLogs("修改视频频道")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String update(@RequestBody VideoChannel videoChannel) {
        videoChannelService.update(videoChannel);
        return ResultFactory.buildSuccessResult();
    }

    @PostMapping("/remove/{id}")
    @ApiOperation(value = "删除视频频道")
    @SysLogs("删除视频频道")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String getList(@PathVariable("id") Integer id) {
        videoChannelService.remove(id);
        return ResultFactory.buildSuccessResult();
    }

}

