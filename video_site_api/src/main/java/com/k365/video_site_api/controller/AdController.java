package com.k365.video_site_api.controller;


import com.k365.manager_service.AdService;
import com.k365.manager_service.SysConfParamService;
import com.k365.video_base.common.AppDisplayTypeEnum;
import com.k365.video_base.common.ResultFactory;
import com.k365.video_common.annotation.Armor;
import com.k365.video_common.annotation.TokenVerify;
import com.k365.video_common.constant.OSEnum;
import com.k365.video_common.constant.SysParamValueNameEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Gavin
 * @since 2019-07-17
 */
@RestController
@RequestMapping(value = {"/ad"})
@Api(tags = "广告")
public class AdController {

    @Autowired
    private AdService adService;

    @Autowired
    private SysConfParamService sysConfParamService;

    @GetMapping("/get/ad/{key}")
    @ApiOperation(value = "根据类型查询广告")
    @TokenVerify
    @Armor(target = {OSEnum.IOS, OSEnum.ANDROID})
    @ApiImplicitParam(paramType = "header", name = "Token", value = "身份认证Token")
    public String findAdByType(@PathVariable("key") Integer key,ServletRequest request) {
        AppDisplayTypeEnum adtEnum = AppDisplayTypeEnum.getAdtEnum(key, AppDisplayTypeEnum.AD.type());
        return ResultFactory.buildNotNullResult(adtEnum == null ? null : adService.findAdsByType(adtEnum,request));
    }

    @GetMapping("/get/banner/{key}")
    @ApiOperation(value = "根据类型查询轮播图")
    @TokenVerify
    @Armor(target = {OSEnum.IOS, OSEnum.ANDROID})
    @ApiImplicitParam(paramType = "header", name = "Token", value = "身份认证Token")
    public String findBannerByType(@PathVariable("key") Integer key,ServletRequest request) {
        AppDisplayTypeEnum adtEnum = AppDisplayTypeEnum.getAdtEnum(key, AppDisplayTypeEnum.BANNER.type());
        return ResultFactory.buildSuccessResult(adtEnum == null ? null : adService.findAdsByType(adtEnum,request));
    }

    @GetMapping("/get/app-up-ad")
    @ApiOperation(value = "获取app启动页广告")
    @TokenVerify
    @Armor(target = {OSEnum.IOS, OSEnum.ANDROID})
    @ApiImplicitParam(paramType = "header", name = "Token", value = "身份认证Token")
    public String getAppUpAd(ServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        Object valByValName = sysConfParamService.getValByValName(SysParamValueNameEnum.AD_COUNTDOWN_SECOND);
        map.put("countdown", valByValName == null ? 5 : valByValName);
        map.put("appUpAd", adService.findAdsByType(AppDisplayTypeEnum.AD_DISPLAY_FOR_APP_UP,request));
        return ResultFactory.buildNotNullResult(map);
    }

    @GetMapping("/get/countdown")
    @ApiOperation(value = "获取广告倒计时")
    public String getAdCountdown() {
        Integer valByValName = Integer.valueOf((String) sysConfParamService.getValByValName(SysParamValueNameEnum.AD_COUNTDOWN_SECOND));
        valByValName = valByValName == null ? 5 : valByValName;
        return ResultFactory.buildNotNullResult(valByValName);
    }

    @PostMapping("/click")
    @ApiOperation(value = "点击广告")
    @TokenVerify
    @Armor(target = {OSEnum.IOS, OSEnum.ANDROID})
    @ApiImplicitParam(paramType = "header", name = "Token", value = "身份认证Token")
    public String clickAd() {
        adService.clickAd();
        return ResultFactory.buildSuccessResult();
    }

}

