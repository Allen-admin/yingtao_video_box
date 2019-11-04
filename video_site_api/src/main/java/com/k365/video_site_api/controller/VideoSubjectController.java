package com.k365.video_site_api.controller;


import com.k365.video_base.model.dto.VideoSubjectDTO;
import com.k365.video_common.annotation.TokenVerify;
import com.k365.video_base.common.ResultFactory;
import com.k365.video_common.constant.StatusEnum;
import com.k365.video_service.VideoSubjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletRequest;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Gavin
 * @since 2019-08-08
 */
@RestController
@RequestMapping("/video-subject")
@Api(tags = "视频主题")
public class VideoSubjectController {

    @Autowired
    private VideoSubjectService videoSubjectService;

    @ApiOperation(value = "获取视频主题")
    @PostMapping("/list")
    @TokenVerify
    @ApiImplicitParam(paramType = "header", name = "Token", value = "身份认证Token")
    public String list(@RequestBody VideoSubjectDTO videoSubjectDTO,ServletRequest request) {
        videoSubjectDTO.setVStatus(StatusEnum.ENABLE.key());
        videoSubjectDTO.setVsStatus(StatusEnum.ENABLE.key());
        return ResultFactory.buildSuccessResult(videoSubjectService.pageList(videoSubjectDTO,request));
    }



}

