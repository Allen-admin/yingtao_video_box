package com.k365.video_manager_api.controller;


import com.k365.manager_service.AdService;
import com.k365.manager_service.SysConfParamService;
import com.k365.video_base.common.AppDisplayTypeEnum;
import com.k365.video_base.model.dto.AdDTO;
import com.k365.video_base.model.po.Ad;
import com.k365.video_common.annotation.SysLogs;
import com.k365.video_base.common.ResultFactory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
@Api(tags = {"广告管理"})
@RequestMapping("/spread/ad")
public class AdController {

    @Autowired
    private AdService adService;

/*    @Autowired
    private SysConfParamService sysConfParamService ;*/

    @PostMapping(value = {"/add"})
    @ApiOperation(value = "添加广告")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String add(@RequestBody  AdDTO adDTO) {
        adService.add(adDTO);
        return ResultFactory.buildSuccessResult();
    }

    @PostMapping(value = {"/update"})
    @ApiOperation(value = "修改广告")
    @SysLogs("修改广告")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String update(@RequestBody Ad ad) {
        adService.update(ad);
        return ResultFactory.buildSuccessResult();
    }

    @PostMapping(value = {"/remove/{id}"})
    @ApiOperation(value = "删除广告")
    @SysLogs("删除广告")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String remove(@PathVariable("id") String id) {
        adService.remove(id);
        return ResultFactory.buildSuccessResult();
    }

    @ApiOperation(value = "获取广告展示位置")
    @GetMapping("/get/show-position")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String getShowPosition() {
        return ResultFactory.buildSuccessResult(AppDisplayTypeEnum.getAdtEnumsByType(AppDisplayTypeEnum.AD));
    }

    @ApiOperation(value = "获取轮播图展示位置")
    @GetMapping("/get/show-banner-position")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String getShowBannerPosition() {
        return ResultFactory.buildSuccessResult(AppDisplayTypeEnum.getAdtEnumsByType(AppDisplayTypeEnum.BANNER));
    }

    @ApiOperation(value = "获取广告列表")
    @GetMapping("/list/{type}/{app}")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String getAdList(@PathVariable("type") Integer type,@PathVariable("app") Integer app,ServletRequest request) {
        return ResultFactory.buildSuccessResult(adService.getListByType(AppDisplayTypeEnum.getAdtEnum(type),app,request));
    }

   /* @ApiOperation(value = "修改广告点击配置")
    @PostMapping("/update/ad-click-conf")
    @SysLogs("修改广告点击配置")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String getAdList(Integer adDailyClicks, Integer adEachAward) {
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put(SysParamValueNameEnum.AD_EACH_AWARD.code(),adEachAward);
        paramMap.put(SysParamValueNameEnum.AD_DAILY_CLICKS.code(),adDailyClicks);
        sysConfParamService.updateByName(SysConfParamDTO.builder().paramName(SysParamNameEnum.MOBILE_AD_CLICK.code())
                .paramStatus(StatusEnum.ENABLE.key()).paramValue(paramMap).build());

        return ResultFactory.buildSuccessResult();
    }*/

}

