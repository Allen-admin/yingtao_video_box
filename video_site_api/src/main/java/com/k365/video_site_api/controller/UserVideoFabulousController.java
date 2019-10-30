package com.k365.video_site_api.controller;


import com.k365.user_service.UserVideoFabulousService;
import com.k365.video_base.common.ResultFactory;
import com.k365.video_base.model.dto.VUserVideoFabulousDTO;
import com.k365.video_common.annotation.TokenVerify;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Gavin
 * @since 2019-08-29
 */
@RestController
@RequestMapping("/user-video-fabulous")
@Api(tags = "视频点赞")
public class UserVideoFabulousController {

    @Autowired
    private UserVideoFabulousService userVideofabulousService;

    @PostMapping("/add")
    @ApiOperation("视频点赞")
    @TokenVerify
    @ApiImplicitParam(paramType = "header", name = "Token", value = "身份认证Token")
    public String add(@RequestBody VUserVideoFabulousDTO vUserVideoFabulousDTO ) {
        userVideofabulousService.addFabulous(vUserVideoFabulousDTO.getVId());
        return ResultFactory.buildSuccessResult();
    }

}
