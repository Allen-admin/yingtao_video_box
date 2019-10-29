package com.k365.video_manager_api.controller;


import com.k365.video_base.model.dto.ActorDTO;
import com.k365.video_common.annotation.SysLogs;
import com.k365.video_base.common.ResultFactory;
import com.k365.video_service.ActorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Gavin
 * @since 2019-07-17
 */
@RestController
@RequestMapping("/video-source/actor")
@Api(tags = {"女优管理"})
public class ActorController {

    @Autowired
    private ActorService actorService;

    @PostMapping(value = {"/add"})
    @ApiOperation(value = "新增女优")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String add(@RequestBody @Validated @ApiParam(value = "新增女优") ActorDTO actorDTO) {
        actorService.add(actorDTO);
        return ResultFactory.buildSuccessResult();
    }

    @PostMapping(value = "/update")
    @ApiOperation(value = "修改女优信息")
    @SysLogs("修改女优信息")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String update(@RequestBody @Validated @ApiParam(value = "修改女优信息") ActorDTO actorDTO) {
        actorService.update(actorDTO);
        return ResultFactory.buildSuccessResult();
    }

    @PostMapping(value = {"/remove/{id}"})
    @ApiOperation(value = "删除女优")
    @SysLogs("删除女优")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String remove(@PathVariable("id") Integer id) {
        actorService.remove(id);
        return ResultFactory.buildSuccessResult();
    }

    @GetMapping(value = {"/get/all"})
    @ApiOperation(value = "获取女优列表")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String list() {
        return ResultFactory.buildSuccessResult(actorService.findAll());
    }

    @PostMapping(value = {"/list"})
    @ApiOperation(value = "分页获取女优列表")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String findPage(@RequestBody ActorDTO actorDTO) {
        return ResultFactory.buildSuccessResult(actorService.findPage(actorDTO));
    }

}

