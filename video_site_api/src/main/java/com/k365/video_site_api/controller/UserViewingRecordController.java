package com.k365.video_site_api.controller;


import com.k365.video_base.common.UserContext;
import com.k365.user_service.UserViewingRecordService;
import com.k365.video_base.model.dto.UserViewingRecordDTO;
import com.k365.video_base.model.po.User;
import com.k365.video_common.annotation.Armor;
import com.k365.video_common.annotation.TokenVerify;
import com.k365.video_common.constant.OSEnum;
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
 * @author Allen
 * @since 2019-08-9
 */
@RestController
@RequestMapping(value = {"/user-viewing-record"})
@Api(tags = "用户观影记录")
public class UserViewingRecordController {


    @Autowired
    public UserViewingRecordService userViewingRecordService;

    @PostMapping("/list")
    @ApiOperation(value = "查看观影记录")
    @TokenVerify
    @Armor(target = {OSEnum.IOS,OSEnum.ANDROID})
    @ApiImplicitParam(paramType = "header", name = "Token", value = "身份认证Token")
    public  String get(@RequestBody @Validated @ApiParam(value = "查看历史记录") UserViewingRecordDTO userViewingRecordDTO){
        return ResultFactory.buildSuccessResult(userViewingRecordService.findPage(userViewingRecordDTO));
    }

    @PostMapping("/remove")
    @ApiOperation(value = "清除观影记录")
    @TokenVerify
    @Armor(target = {OSEnum.IOS,OSEnum.ANDROID})
    @ApiImplicitParam(paramType = "header", name = "Token", value = "身份认证Token")
    public  String remove(){
        User currentUser = UserContext.getCurrentUser();
        userViewingRecordService.removeByUId(currentUser.getId());
        return ResultFactory.buildSuccessResult();
    }


}

