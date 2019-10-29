package com.k365.video_site_api.controller;


import com.k365.video_base.common.UserContext;
import com.k365.user_service.UserVideoCollectionService;
import com.k365.video_base.model.dto.VUserVideoCollectionDTO;
import com.k365.video_base.model.po.User;
import com.k365.video_base.model.po.UserVideoCollection;
import com.k365.video_common.annotation.Armor;
import com.k365.video_common.annotation.TokenVerify;
import com.k365.video_common.constant.OSEnum;
import com.k365.video_base.common.ResultFactory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Gavin
 * @since 2019-08-29
 */
@RestController
@RequestMapping("/user-video-collection")
@Api(tags = "我的收藏")
public class UserVideoCollectionController {

    @Autowired
    private UserVideoCollectionService userVideoCollectionService;

    @PostMapping("/list")
    @ApiOperation("分页查询收藏列表")
    @TokenVerify
    @Armor(target = {OSEnum.IOS,OSEnum.ANDROID})
    @ApiImplicitParam(paramType = "header", name = "Token", value = "身份认证Token")
    public String list(@RequestBody VUserVideoCollectionDTO vUserVideoCollectionDTO) {
        return ResultFactory.buildSuccessResult(userVideoCollectionService.getPage(vUserVideoCollectionDTO));
    }

    @PostMapping("/add")
    @ApiOperation("添加收藏视频")
    @TokenVerify
    @Armor(target = {OSEnum.IOS,OSEnum.ANDROID})
    @ApiImplicitParam(paramType = "header", name = "Token", value = "身份认证Token")
    public String add(@RequestBody VUserVideoCollectionDTO vUserVideoCollectionDTO) {
        userVideoCollectionService.addCollection(vUserVideoCollectionDTO.getVId());
        return ResultFactory.buildSuccessResult();
    }

    @PostMapping("/remove")
    @ApiOperation("取消收藏")
    @TokenVerify
    @Armor(target = {OSEnum.IOS,OSEnum.ANDROID})
    @ApiImplicitParam(paramType = "header", name = "Token", value = "身份认证Token")
    public String remove(@RequestBody VUserVideoCollectionDTO vUserVideoCollectionDTO) {
        User currentUser = UserContext.getCurrentUser();
        userVideoCollectionService.removeByVidOrUId(UserVideoCollection.builder().userId(currentUser.getId())
                .videoId(vUserVideoCollectionDTO.getVId()).build());
        return ResultFactory.buildSuccessResult();
    }

}

