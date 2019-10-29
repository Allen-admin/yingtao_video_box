package com.k365.video_site_api.controller;


import com.k365.user_service.UserSaveVideoRecordService;
import com.k365.video_base.model.dto.UserSaveVideoRecordDTO;
import com.k365.video_common.annotation.TokenVerify;
import com.k365.video_base.common.ResultFactory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Gavin
 * @since 2019-07-17
 */
@RestController
@RequestMapping(value = {"/user-save-video-record"})
@Api(tags = "用户下载记录")
public class UserSaveVideoRecordController {

   /* @Autowired
    public UserSaveVideoRecordService userSaveVideoRecordService;

    @PostMapping("/list")
    @ApiOperation(value = "下载记录查询")
    @TokenVerify
    @ApiImplicitParam(paramType = "header", name = "Token", value = "身份认证Token")
    public  String get(@RequestBody @Validated @ApiParam(value = "下载记录查询") UserSaveVideoRecordDTO userSaveVideoRecordDTO){
        return ResultFactory.buildSuccessResult(userSaveVideoRecordService.find(userSaveVideoRecordDTO));
    }*/


}

