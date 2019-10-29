package com.k365.video_site_api.controller;


import com.k365.manager_service.NoticeService;
import com.k365.video_base.common.AppDisplayTypeEnum;
import com.k365.video_base.common.UserContext;
import com.k365.video_base.model.po.User;
import com.k365.video_common.annotation.Armor;
import com.k365.video_common.annotation.TokenVerify;
import com.k365.video_common.constant.OSEnum;
import com.k365.video_base.common.ResultFactory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
@RequestMapping(value = {"/notice"})
@Api(tags = {"公告"})
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    @GetMapping("/get/{key}")
    @ApiOperation("获取公告")
    @TokenVerify
    @Armor(target = {OSEnum.IOS,OSEnum.ANDROID})
    @ApiImplicitParam(paramType = "header", name = "Token", value = "身份认证Token")
    public String getNotice(@PathVariable("key") Integer key) {
        User currentUser = UserContext.getCurrentUser();
        AppDisplayTypeEnum adtEnum = AppDisplayTypeEnum.getAdtEnum(key, AppDisplayTypeEnum.NOTICE.type());
        return ResultFactory.buildNotNullResult(adtEnum == null ? null : noticeService.findListByType(adtEnum,currentUser.getAppType()));
    }

}

