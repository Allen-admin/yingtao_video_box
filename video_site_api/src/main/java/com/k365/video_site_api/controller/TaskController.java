package com.k365.video_site_api.controller;


import com.k365.user_service.UserTaskRecordService;
import com.k365.video_common.annotation.TokenVerify;
import com.k365.video_base.common.ResultFactory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
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
@RequestMapping(value = "/task")
@Api(tags = "任务管理")
public class TaskController {

    @Autowired
    private UserTaskRecordService userTaskRecordService;

    @ApiOperation(value = "特别推荐任务")
    @PostMapping(value = "/get/recommend")
    @TokenVerify
    @ApiImplicitParam(paramType = "header", name = "Token", value = "身份认证Token")
    public String getRecommend() {

        return ResultFactory.buildSuccessResult(userTaskRecordService.findRecommend());
    }

    @ApiOperation(value = "获取全部任务")
    @PostMapping(value = "/list")
    @TokenVerify
    @ApiImplicitParam(paramType = "header", name = "Token", value = "身份认证Token")
    public String getAll() {

        return ResultFactory.buildSuccessResult(userTaskRecordService.findAll());
    }

}

