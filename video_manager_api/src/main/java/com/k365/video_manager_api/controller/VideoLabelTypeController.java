package com.k365.video_manager_api.controller;


import com.k365.video_base.model.po.VideoLabelType;
import com.k365.video_common.annotation.SysLogs;
import com.k365.video_base.common.ResultFactory;
import com.k365.video_service.VideoLabelTypeService;
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
 * @since 2019-08-13
 */
@RestController
@RequestMapping("/video-source/video-label-type")
@Api(tags = {"视频标签类型"})
public class VideoLabelTypeController {

    @Autowired
    private VideoLabelTypeService videoLabelTypeService;

    @GetMapping("/list")
    @ApiOperation(value = "查询视频标签类型")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String getList() {
        return ResultFactory.buildSuccessResult(videoLabelTypeService.findAll());
    }

    @PostMapping("/add")
    @ApiOperation(value = "新增视频标签类型")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String add(@RequestBody VideoLabelType videoLabelType) {
        videoLabelTypeService.add(videoLabelType);
        return ResultFactory.buildSuccessResult();
    }

    @PostMapping("/update")
    @ApiOperation(value = "修改视频标签类型")
    @SysLogs("修改视频标签类型")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String update(@RequestBody VideoLabelType videoLabelType) {
        videoLabelTypeService.update(videoLabelType);
        return ResultFactory.buildSuccessResult();
    }

    @PostMapping("/remove/{id}")
    @ApiOperation(value = "删除视频标签类型")
    @SysLogs("删除视频标签类型")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String getList(@PathVariable("id") Integer id) {
        videoLabelTypeService.remove(id);
        return ResultFactory.buildSuccessResult();
    }

}

