package com.k365.video_site_api.controller;


import com.k365.video_base.common.ResultFactory;
import com.k365.video_base.model.dto.VActorVideoDTO;
import com.k365.video_base.model.dto.VideoDTO;
import com.k365.video_base.model.so.VideoSO;
import com.k365.video_common.annotation.Armor;
import com.k365.video_common.annotation.TokenVerify;
import com.k365.video_common.constant.OSEnum;
import com.k365.video_common.constant.StatusEnum;
import com.k365.video_service.VActorVideoService;
import com.k365.video_service.VideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Gavin
 * @since 2019-07-17
 */
@RestController
@RequestMapping("/video")
@Api(tags = "视频相关功能")
public class VideoController {

    @Autowired
    private VideoService videoService;

    @Autowired
    private VActorVideoService vActorVideoService;

    @PostMapping("/get/search")
    @ApiOperation(value = "视频搜索")
    public String search(@RequestBody VideoSO videoSO,ServletRequest request) {
        videoSO.setStatus(StatusEnum.ENABLE.key());
        return ResultFactory.buildSuccessResult(videoService.searchByKeyword(videoSO,request));
    }

    @PostMapping("/get/featured")
    @ApiOperation(value = "编辑精选视频")
    @TokenVerify
    @Armor(target = {OSEnum.IOS, OSEnum.ANDROID})
    @ApiImplicitParam(paramType = "header", name = "Token", value = "身份认证Token")
    public String findFeatured(@RequestBody VideoDTO videoDTO,ServletRequest request) {
        return ResultFactory.buildSuccessResult(videoService.findFeatured(videoDTO,request));
    }

    @PostMapping("/get/u-likes")
    @ApiOperation(value = "智能推荐")
    @TokenVerify
    @Armor(target = {OSEnum.IOS, OSEnum.ANDROID})
    @ApiImplicitParam(paramType = "header", name = "Token", value = "身份认证Token")
    public String findULikes(@RequestBody VideoDTO videoDTO,ServletRequest request) {
        return ResultFactory.buildSuccessResult(videoService.findULikes(videoDTO,request));
    }

    @PostMapping("/get/latest")
    @ApiOperation(value = "最新视频")
    @TokenVerify
    @Armor(target = {OSEnum.IOS, OSEnum.ANDROID})
    @ApiImplicitParam(paramType = "header", name = "Token", value = "身份认证Token")
    public String findLatest(@RequestBody VideoDTO videoDTO,ServletRequest request) {
        return ResultFactory.buildSuccessResult(videoService.findLatest(videoDTO,request));
    }

    @PostMapping("/get/hottest")
    @ApiOperation(value = "最热视频")
    @TokenVerify
    @Armor(target = {OSEnum.IOS, OSEnum.ANDROID})
    @ApiImplicitParam(paramType = "header", name = "Token", value = "身份认证Token")
    public String findHottest(@RequestBody VideoDTO videoDTO,ServletRequest request) {
        return ResultFactory.buildSuccessResult(videoService.findHottest(videoDTO,request));
    }

    @PostMapping("/get/channel-video")
    @ApiOperation(value = "自选频道视频")
    @TokenVerify
    @ApiImplicitParam(paramType = "header", name = "Token", value = "身份认证Token")
    public String findByChannelType(@RequestBody VideoSO videoSO,ServletRequest request) {
        return ResultFactory.buildNotNullResult(videoService.findByChannelType(videoSO,request));
    }

    @PostMapping("/get/vip-video")
    @ApiOperation(value = "vip视频")
    @TokenVerify
    @ApiImplicitParam(paramType = "header", name = "Token", value = "身份认证Token")
    public String findVipVideo(@RequestBody VideoSO videoSO,ServletRequest request) {
        return ResultFactory.buildNotNullResult(videoService.findVipVideo(videoSO,request));
    }

    @PostMapping("/get/relevant")
    @ApiOperation(value = "相关视频")
    @TokenVerify
    @Armor(target = {OSEnum.IOS, OSEnum.ANDROID})
    @ApiImplicitParam(paramType = "header", name = "Token", value = "身份认证Token")
    public String findRandomRelevant(@RequestBody VideoDTO videoDTO,ServletRequest request) {
        return ResultFactory.buildSuccessResult(videoService.findRandomRelevant(videoDTO,request));
    }

    @PostMapping("/get/find-by-label")
    @ApiOperation(value = "根据标签筛选")
    @TokenVerify
    @ApiImplicitParam(paramType = "header", name = "Token", value = "身份认证Token")
    public String findByLabels(@RequestBody VideoSO videoSO,ServletRequest request) {
        videoSO.setStatus(StatusEnum.ENABLE.key());
        return ResultFactory.buildSuccessResult(videoService.findByLabels(videoSO,request));
    }

    @PostMapping("/get/find-by-subject")
    @ApiOperation(value = "根据专题筛选")
    @TokenVerify
    @ApiImplicitParam(paramType = "header", name = "Token", value = "身份认证Token")
    public String findBySubject(@RequestBody VideoSO videoSO,ServletRequest request) {
        return ResultFactory.buildSuccessResult(videoService.findBySubject(videoSO,request));
    }

    @PostMapping("/get/play-info")
    @ApiOperation(value = "获取播放页数据")
    @TokenVerify
    @Armor(target = {OSEnum.IOS, OSEnum.ANDROID})
    @ApiImplicitParam(paramType = "header", name = "Token", value = "身份认证Token")
    public String findPlayVideo(@RequestBody VideoSO videoSO,ServletRequest request) {
        return ResultFactory.buildSuccessResult(videoService.findPlayVideo(videoSO,false,request));
    }

    @PostMapping("/get/save-authority")
    @ApiOperation(value = "获取视频下载路径")
    @TokenVerify
    @ApiImplicitParam(paramType = "header", name = "Token", value = "身份认证Token")
    public String getSaveAuth(@RequestBody VideoSO videoSO) {
        return ResultFactory.buildNotNullResult(videoService.getSaveVideoUrl(videoSO));
    }

    @PostMapping("/get/find-by-actor")
    @ApiOperation(value = "根据女优查询视频")
    @TokenVerify
    @ApiImplicitParam(paramType = "header", name = "Token", value = "身份认证Token")
    public String findByActor(@RequestBody VActorVideoDTO vActorVideoDTO) {
        return ResultFactory.buildSuccessResult(vActorVideoService.findByActorId(vActorVideoDTO));
    }

    @PostMapping("/get/vip-play-info")
    @ApiOperation(value = "查询vip视频播放信息")
    @TokenVerify
    @Armor(target = {OSEnum.IOS, OSEnum.ANDROID})
    @ApiImplicitParam(paramType = "header", name = "Token", value = "身份认证Token")
    public String findVipPlayVideo(@RequestBody VideoSO videoSO,ServletRequest request) {
        return ResultFactory.buildNotNullResult(videoService.findVipPlayVideo(videoSO,request));
    }

    @PostMapping("/get/getViderSimpInfo")
    @ApiOperation(value = "查询视频相关简略信息")
    public String getViderSimpInfo(String id) {
        return ResultFactory.buildNotNullResult(videoService.getViderSimpInfo(id));
    }

}
