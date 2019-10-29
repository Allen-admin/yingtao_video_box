package com.k365.video_site_api.controller;


import com.k365.user_service.UserFeedbackService;
import com.k365.video_base.model.po.UserFeedback;
import com.k365.video_base.common.ResultFactory;
import com.k365.video_common.annotation.TokenVerify;
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
 * 用户反馈意见表 前端控制器
 * </p>
 *
 * @author Gavin
 * @since 2019-07-17
 */
@RestController
@RequestMapping("/user-feedback")
@Api(tags = "反馈意见")
public class UserFeedbackController {

    @Autowired
    private UserFeedbackService userFeedbackService;

    @ApiOperation(value = "新增用户反馈")
    @PostMapping("/add")
    @TokenVerify
    @ApiImplicitParam(paramType = "header", name = "Token", value = "身份认证Token")
    public String add(@RequestBody UserFeedback userFeedback){
        userFeedbackService.add(userFeedback);
        return ResultFactory.buildSuccessResult();
    }


}

