package com.k365.video_manager_api.controller;


import com.k365.video_base.common.ResultFactory;
import com.k365.video_base.model.dto.VideoInfoDTO;
import com.k365.video_common.annotation.SysLogs;
import com.k365.video_service.VideoImportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ALLen
 * @since 2019-09-18
 */
@RestController
@RequestMapping("/video-source/video-import")
@Api(tags = {"视频信息导入管理"})
public class VideoImportController {

    @Autowired
    private VideoImportService videoImportService;

    @PostMapping("/list")
    @ApiOperation(value = "视频信息导入列表")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String search(@RequestBody VideoInfoDTO videoInfoDTO) {
        return ResultFactory.buildSuccessResult(videoImportService.findAll(videoInfoDTO));
    }

    @PostMapping("/remove/{id}")
    @ApiOperation(value = "删除视频导入信息")
    @SysLogs("删除视频导入信息")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String remove(@PathVariable("id") String id) {
        videoImportService.removeById(id);
        return ResultFactory.buildSuccessResult();
    }

}
