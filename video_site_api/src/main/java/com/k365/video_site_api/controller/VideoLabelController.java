package com.k365.video_site_api.controller;


import com.k365.video_base.model.so.VideoSO;
import com.k365.video_base.common.ResultFactory;
import com.k365.video_service.VVideoChannelLabelService;
import com.k365.video_service.VideoLabelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/video-label")
@Api(tags = "视频标签")
public class VideoLabelController {

    @Autowired
    private VideoLabelService videoLabelService;

    @Autowired
    private VVideoChannelLabelService vVideoChannelLabelService;

    @GetMapping("/get/all")
    @ApiOperation(value = "获取所有标签类型及标签")
    public String getTopLabel() {
        return ResultFactory.buildSuccessResult(videoLabelService.findLabelAndType());
    }

    @PostMapping("/get/hot")
    @ApiOperation(value = "获取所有热门标签")
    public String getHotLabel(@RequestBody VideoSO videoSO) {
        return ResultFactory.buildSuccessResult(vVideoChannelLabelService.findHotLabelByHotVideo(videoSO));
    }

}

