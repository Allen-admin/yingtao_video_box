package com.k365.video_site_api.controller;


import com.k365.video_base.model.dto.VideoCommentDTO;
import com.k365.video_base.model.po.VideoComment;
import com.k365.video_common.annotation.Armor;
import com.k365.video_common.annotation.SysLogs;
import com.k365.video_common.annotation.TokenVerify;
import com.k365.video_common.constant.OSEnum;
import com.k365.video_base.common.ResultFactory;
import com.k365.video_service.VideoCommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Gavin
 * @since 2019-07-17
 */
@RestController
@RequestMapping("/video-comment")
@Api(tags = {"视频评论"})
public class VideoCommentController {

    @Autowired
    public VideoCommentService videoCommentService;


    @PostMapping("/add")
    @ApiOperation(value = "新增视频评论")
    @TokenVerify
    @ApiImplicitParam(paramType = "header", name = "Token", value = "身份认证Token")
    public String add(@RequestBody VideoComment videoComment) {
        videoCommentService.addComment(videoComment);
        return ResultFactory.buildSuccessResult();
    }

    @PostMapping("/list")
    @ApiOperation(value = "获取评论列表")
    @TokenVerify
    @Armor(target = {OSEnum.IOS,OSEnum.ANDROID})
    @ApiImplicitParam(paramType = "header", name = "Token", value = "身份认证Token")
    public String list(@RequestBody VideoCommentDTO videoCommentDTO) {
        return ResultFactory.buildSuccessResult(videoCommentService.getPageByVId(videoCommentDTO));
    }
}

