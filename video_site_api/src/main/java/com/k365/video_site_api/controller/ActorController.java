package com.k365.video_site_api.controller;


import com.k365.video_base.model.dto.ActorDTO;
import com.k365.video_common.annotation.SysLogs;
import com.k365.video_base.common.ResultFactory;
import com.k365.video_service.ActorService;
import io.swagger.annotations.Api;
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
 * @author Gavin
 * @since 2019-07-17
 */
@RestController
@RequestMapping("/actor")
@Api(tags = "女优")
public class ActorController {

     @Autowired
     public ActorService actorService;

    @PostMapping(value = "/list")
    @ApiOperation(value = "女优信息查询")
    public  String find(@RequestBody @Validated @ApiParam("女优信息查询")ActorDTO actorDTO){
        return ResultFactory.buildSuccessResult( actorService.findActor(actorDTO));

    }

}

