package com.k365.video_manager_api.controller;


import com.k365.video_base.model.dto.VideoSubjectDTO;
import com.k365.video_base.model.po.VideoSubject;
import com.k365.video_common.annotation.SysLogs;
import com.k365.video_base.common.ResultFactory;
import com.k365.video_common.constant.StatusEnum;
import com.k365.video_service.VideoSubjectService;
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
 * @since 2019-08-08
 */
@RestController
@RequestMapping("/video-source/video-subject")
@Api(tags = {"视频专题"})
public class VideoSubjectController {

    @Autowired
    private VideoSubjectService videoSubjectService;

    @PostMapping("/list")
    @ApiOperation(value = "查询所有视频专题")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String getList(@RequestBody VideoSubjectDTO videoSubjectDTO) {
        videoSubjectDTO.setVsStatus(StatusEnum.ENABLE.key());
        return ResultFactory.buildSuccessResult(videoSubjectService.findAll(videoSubjectDTO));
    }

    @PostMapping("/find")
    @ApiOperation(value = "查询分页视频专题")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String find(@RequestBody VideoSubjectDTO videoSubjectDTO) {
        videoSubjectDTO.setVsStatus(StatusEnum.ENABLE.key());
        return ResultFactory.buildSuccessResult(videoSubjectService.findPage(videoSubjectDTO));
    }

    @PostMapping("/add")
    @ApiOperation(value = "新增视频专题")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String add(@RequestBody VideoSubject videoSubject) {
        videoSubjectService.add(videoSubject);
        return ResultFactory.buildSuccessResult();
    }

    @PostMapping("/update")
    @ApiOperation(value = "修改视频专题")
    @SysLogs("修改视频专题")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String update(@RequestBody VideoSubject videoSubject) {
        videoSubjectService.update(videoSubject);
        return ResultFactory.buildSuccessResult();
    }

    @PostMapping("/remove/{id}")
    @ApiOperation(value = "删除视频专题")
    @SysLogs("删除视频专题")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String remove(@PathVariable("id") Integer id) {
        videoSubjectService.remove(id);
        return ResultFactory.buildSuccessResult();
    }

}

