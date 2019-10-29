package com.k365.video_site_api.controller;


import com.k365.manager_service.AppVersionService;
import com.k365.video_base.common.ResultFactory;
import com.k365.video_common.util.AppInfo;
import com.k365.video_common.util.HttpUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping("/app-version")
@Api(tags = "APP版本管理")
public class AppVersionController {

    @Autowired
    private AppVersionService appVersionService;

    @ApiOperation(value = "版本检测")
    @GetMapping("/check")
    public String check(ServletRequest request) {
        AppInfo appInfo = HttpUtil.getAppInfo(request);
        return ResultFactory.buildNotNullResult(appVersionService.findNewest(appInfo));
    }

    @ApiOperation(value = "查询伪装数据开关")
    @GetMapping("/get/data-switch")
    public String getArmorDataSwitch(ServletRequest request) {
        AppInfo appInfo = HttpUtil.getAppInfo(request);
        return ResultFactory.buildNotNullResult(appVersionService.findArmorDataSwitch(appInfo));
    }


}

