package com.k365.video_manager_api.controller;


import com.k365.video_base.model.dto.VideoLabelDTO;
import com.k365.video_common.annotation.SysLogs;
import com.k365.video_base.common.ResultFactory;
import com.k365.video_service.VideoLabelService;
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
 * @since 2019-07-17
 */
@RestController
@RequestMapping("/video-source/video-label")
@Api(tags = {"视频标签管理"})
public class VideoLabelController {

    @Autowired
    private VideoLabelService videoLabelService;

    @GetMapping("/get/all")
    @ApiOperation(value = "查询所有视频标签")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String getAllList() {
        return ResultFactory.buildSuccessResult(videoLabelService.findAll());
    }

    @PostMapping("/search")
    @ApiOperation(value = "模糊查询视频标签")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String List(@RequestBody VideoLabelDTO videoLabelDTO) {
        return ResultFactory.buildSuccessResult(videoLabelService.search(videoLabelDTO));
    }

    @PostMapping("/list")
    @ApiOperation(value = "分页查询视频标签")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String getList(@RequestBody VideoLabelDTO videoLabelDTO) {
        return ResultFactory.buildSuccessResult(videoLabelService.findPage(videoLabelDTO));
    }

    @PostMapping("/add")
    @ApiOperation(value = "新增视频标签")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String add(@RequestBody VideoLabelDTO videoLabelDTO) {
        videoLabelService.add(videoLabelDTO);
        return ResultFactory.buildSuccessResult();
    }

    @PostMapping("/update")
    @ApiOperation(value = "修改视频标签")
    @SysLogs("修改视频标签")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String update(@RequestBody VideoLabelDTO videoLabelDTO) {
        videoLabelService.update(videoLabelDTO);
        return ResultFactory.buildSuccessResult();
    }

    @PostMapping("/remove/{id}")
    @ApiOperation(value = "删除视频标签")
    @SysLogs("删除视频标签")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String getList(@PathVariable("id") Integer id) {
        videoLabelService.remove(id);
        return ResultFactory.buildSuccessResult();
    }

}

