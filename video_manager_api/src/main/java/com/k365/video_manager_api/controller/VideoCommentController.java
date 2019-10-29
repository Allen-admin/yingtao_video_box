package com.k365.video_manager_api.controller;


import com.k365.video_base.model.dto.VideoCommentDTO;
import com.k365.video_common.annotation.SysLogs;
import com.k365.video_base.common.ResultFactory;
import com.k365.video_service.VideoCommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
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
@RequestMapping("/video-source/video-comment")
@Api(tags = {"视频评论管理"})
public class VideoCommentController {

    @Autowired
    private VideoCommentService videoCommentService;

    @PostMapping(value = {"/remove/{id}"})
    @ApiOperation(value = "删除视频评论")
    @SysLogs("删除视频评论")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String remove(@PathVariable("id") String id) {
        videoCommentService.removeById(id);
        return ResultFactory.buildSuccessResult();
    }

    @PostMapping(value = "/list")
    @ApiOperation(value = "视频评论查询")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public  String find(@RequestBody @Validated @ApiParam("视频评论信息查询") VideoCommentDTO videoCommentDTO) {
        return ResultFactory.buildSuccessResult(videoCommentService.findAll(videoCommentDTO));
    }

    @PostMapping(value = "/list/by-vid-uid")
    @ApiOperation(value = "视频评论根据拥护id或视频id查询")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public  String findByVidOrUId(@RequestBody VideoCommentDTO videoCommentDTO) {
        return ResultFactory.buildSuccessResult(videoCommentService.findByVIdOrUId(videoCommentDTO));
    }

    @PostMapping(value = "/search")
    @ApiOperation(value = "视频评论根据拥护id或视频id搜索")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public  String search(@RequestBody VideoCommentDTO videoCommentDTO) {
        return ResultFactory.buildSuccessResult(videoCommentService.search(videoCommentDTO));
    }

}

