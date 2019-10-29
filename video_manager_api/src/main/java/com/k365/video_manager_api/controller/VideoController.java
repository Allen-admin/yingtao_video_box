package com.k365.video_manager_api.controller;


import com.k365.video_base.common.ResultFactory;
import com.k365.video_base.model.dto.VideoDTO;
import com.k365.video_base.model.dto.VideoExcelImportDTO;
import com.k365.video_base.model.dto.VideoInfoDTO;
import com.k365.video_base.model.so.VideoSO;
import com.k365.video_common.annotation.SysLogs;
import com.k365.video_service.VideoImportService;
import com.k365.video_service.VideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
@RequestMapping("/video-source/video")
@Api(tags = {"视频管理"})
public class VideoController {

    @Autowired
    private VideoService videoService;

    @Autowired
    private VideoImportService videoImportService;

    @PostMapping("/import")
    @ApiOperation(value = "视频信息导入")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String importVideoInfo(@RequestBody VideoInfoDTO videoInfoDTO) {
        return ResultFactory.buildSuccessResult(videoImportService.importVideo(videoInfoDTO));
    }

    @PostMapping("/excel-import")
    @SysLogs("Excel模板导入视频")
    @ApiOperation(value = "Excel模板导入视频")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String importExcelVideo(MultipartFile file) {
        return ResultFactory.buildSuccessResult(videoImportService.importExcel(file, VideoExcelImportDTO.class));
    }


    @PostMapping("/add")
    @ApiOperation(value = "新增视频")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String add(@RequestBody @Validated @ApiParam(value = "新增视频") VideoDTO videoDTO) {
        videoService.add(videoDTO);
        return ResultFactory.buildSuccessResult();
    }

    @PostMapping("/update")
    @ApiOperation(value = "修改视频")
    @SysLogs("修改视频")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String update(@RequestBody @Validated @ApiParam(value = "修改视频") VideoDTO videoDTO) {
        videoService.update(videoDTO);
        return ResultFactory.buildSuccessResult();
    }

    @PostMapping("/remove/{id}")
    @ApiOperation(value = "删除视频")
    @SysLogs("删除视频")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String remove(@PathVariable("id") String id) {
        videoService.removeByVideoId(id);
        return ResultFactory.buildSuccessResult();
    }

    @PostMapping("/find/{id}")
    @ApiOperation(value = "查询视频")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String find(@PathVariable("id") String id) {
        return ResultFactory.buildSuccessResult(videoService.findById(id));
    }

    @PostMapping("/list")
    @ApiOperation(value = "视频列表")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String list(@RequestBody VideoDTO videoDTO,ServletRequest request) {
        return ResultFactory.buildSuccessResult(videoService.getByPage(videoDTO,request));
    }

    @PostMapping("/search")
    @ApiOperation(value = "筛选视频列表")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String search(@RequestBody VideoSO videoSO,ServletRequest request) {
        return ResultFactory.buildSuccessResult(videoService.searchByVideoType(videoSO,request));
    }

}


