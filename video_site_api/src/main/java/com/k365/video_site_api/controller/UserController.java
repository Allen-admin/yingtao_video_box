package com.k365.video_site_api.controller;


import com.k365.global.CommonService;
import com.k365.user_service.UserService;
import com.k365.video_base.common.ResultFactory;
import com.k365.video_base.model.dto.UserDTO;
import com.k365.video_base.model.po.User;
import com.k365.video_common.annotation.Armor;
import com.k365.video_common.annotation.TokenVerify;
import com.k365.video_common.constant.OSEnum;
import com.k365.video_common.exception.ResponsiveException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
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
@RequestMapping("/user")
@Api(tags = {"用户相关功能"})
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private CommonService commonService;

    @PostMapping(value = "/visitor/auto-register")
    @ApiOperation(value = "游客自动注册")
    @Armor(target = {OSEnum.IOS, OSEnum.ANDROID})
    public String autoRegister(@RequestBody @Validated(UserDTO.Add.class) UserDTO userDTO, ServletRequest request) {
        return ResultFactory.buildSuccessResult(userService.visitorAutoRegister(request, userDTO));
    }

    @PostMapping(value = "/user-info")
    @ApiOperation(value = "获取当前用户信息")
    @TokenVerify
    @Armor(target = {OSEnum.IOS, OSEnum.ANDROID})
    @ApiImplicitParam(paramType = "header", name = "Token", value = "身份认证Token")
    public String userInfo() {

        return ResultFactory.buildSuccessResult(userService.getUserInfo());
    }


    @PostMapping(value = "/check-mobile/{mobile}")
    @ApiOperation(value = "手机号是否注册过")
    public String checkMobile(@PathVariable(value = "mobile") String mobile) {
        return ResultFactory.buildSuccessResult(userService.mobileIsExists(mobile));
    }

    @PostMapping(value = "/send-code/{mobile}")
    @ApiOperation(value = "发送短信验证码")
    public String sendVerifyCode(@PathVariable(value = "mobile") String mobile) {
        if (userService.mobileIsExists(mobile)) {
            throw new ResponsiveException("该手机号已被绑定！");
        }
        userService.sendVerifyCode(mobile);
        return ResultFactory.buildSuccessResult();
    }

    @PostMapping(value = "/upload/portrait")
    @ApiOperation(value = "上传用户头像")
    @TokenVerify
    @ApiImplicitParam(paramType = "header", name = "Token", value = "身份认证Token")
    public String uploadPortrait(MultipartFile file, String oldFilePath) {
        String filePath = commonService.uploadFile(file, oldFilePath);
        Boolean upSuccess = userService.uploadPortrait(filePath);
        return ResultFactory.buildNotNullResult(upSuccess ? filePath : null);
    }

    @PostMapping(value = "/update")
    @ApiOperation(value = "修改用户信息")
    @TokenVerify
    @ApiImplicitParam(paramType = "header", name = "Token", value = "身份认证Token")
    public String uploadPortrait(@RequestBody User user) {
        userService.updateUser(user);
        return ResultFactory.buildSuccessResult();
    }

    @PostMapping(value = "/bind-phone")
    @ApiOperation(value = "用户绑定手机号")
    @TokenVerify
    @ApiImplicitParam(paramType = "header", name = "Token", value = "身份认证Token")
    public String bindPhone(@RequestBody UserDTO userDTO) {
        return ResultFactory.buildSuccessResult(userService.bindPhone(userDTO.getPhone(), userDTO.getVerifyCode()));
    }

    @PostMapping(value = "/spread-register")
    @ApiOperation(value = "推广玩家注册")
    @TokenVerify
    @ApiImplicitParam(paramType = "header", name = "Token", value = "身份认证Token")
    public String spreadUserRegister(@RequestBody UserDTO userDTO) {
        userService.spreadRegister(userDTO.getSpreadCode());
        return ResultFactory.buildSuccessResult();
    }

    @GetMapping(value = "/spread-info")
    @ApiOperation(value = "推广详情")
    @TokenVerify
    @ApiImplicitParam(paramType = "header", name = "Token", value = "身份认证Token")
    public String spreadInfo() {
        return ResultFactory.buildSuccessResult(userService.spreadRecordList());
    }

    @GetMapping(value = "/viewing-info")
    @ApiOperation(value = "剩余观影信息")
    @TokenVerify
    @Armor(target = {OSEnum.IOS, OSEnum.ANDROID})
    @ApiImplicitParam(paramType = "header", name = "Token", value = "身份认证Token")
    public String viewingInfo() {
        return ResultFactory.buildSuccessResult(userService.getViewingCount());
    }

/*  @PostMapping(value = "/resend-code/{mobile}")
    @ApiOperation(value = "重新发送短信验证码")
    public String resendVerifyCode(@PathVariable(value = "mobile") String mobile) {
        userService.resendVerifyCode(mobile);
        return ResultFactory.buildSuccessResult();
    }

    @PostMapping(value = "/voice-code/{mobile}")
    @ApiOperation(value = "呼叫语音电话获取验证码")
    public String callVoiceVerifyCode(@PathVariable(value = "mobile") String mobile) {
        userService.callVoiceVerifyCode(mobile);
        return ResultFactory.buildSuccessResult();
    }*/


}

