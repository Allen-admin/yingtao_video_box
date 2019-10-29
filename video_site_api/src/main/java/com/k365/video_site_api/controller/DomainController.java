package com.k365.video_site_api.controller;


import com.k365.manager_service.DomainService;
import com.k365.video_common.annotation.Armor;
import com.k365.video_common.annotation.TokenVerify;
import com.k365.video_common.constant.AppTypeEnum;
import com.k365.video_common.constant.OSEnum;
import com.k365.video_base.common.ResultFactory;
import com.k365.video_common.util.HttpUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
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
@RequestMapping("/domain")
@Api(tags = "APP域名处理")
public class DomainController {

    @Autowired
    private DomainService domainService;

    @GetMapping("/check")
    @ApiOperation(value = "域名检测")
    public String check() {
        return ResultFactory.buildSuccessResult();
    }

    @GetMapping("/get/usable")
    @ApiOperation(value = "获取可用域名列表")
    @Armor(target = {OSEnum.IOS, OSEnum.ANDROID})
    public String list(ServletRequest request) {
        AppTypeEnum appType = HttpUtil.getAppType(request);
        return ResultFactory.buildSuccessResult(domainService.findUsable(appType));
    }

    @GetMapping("/get/spread-domain")
    @ApiOperation(value = "获取推广域名")
    @TokenVerify
    @ApiImplicitParam(paramType = "header", name = "Token", value = "身份认证Token")
    public String getSpreadDomain() {

        return ResultFactory.buildSuccessResult(domainService.getSpreadDomain());
    }


}

