package com.k365.video_site_api.controller;


import com.k365.user_service.UserTaskRecordService;
import com.k365.video_common.annotation.SysLogs;
import com.k365.video_common.annotation.TokenVerify;
import com.k365.video_base.common.ResultFactory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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
@RequestMapping("/user-task-record")
@Api(tags = "用户任务记录")
public class UserTaskRecordController {

    @Autowired
    private UserTaskRecordService userTaskRecordService;

    @GetMapping(value = "/save-qr-img")
    @ApiOperation(value = "用户保存二维码")
    @TokenVerify
    @ApiImplicitParam(paramType = "header", name = "Token", value = "身份认证Token")
    public String saveQrImg() {
        userTaskRecordService.toDoSaveQrImg();
        return ResultFactory.buildSuccessResult();
    }

}

