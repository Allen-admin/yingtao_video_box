package com.k365.video_manager_api.controller;

import com.k365.manager_service.SysConfParamService;
import com.k365.video_base.model.dto.SysConfParamDTO;
import com.k365.video_common.annotation.SysLogs;
import com.k365.video_base.common.ResultFactory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author Gavin
 * @date 2019/7/2 10:07
 * @description：
 */
@RestController
@RequestMapping(value = {"/sys/param"})
@Api(tags = {"系统参数设置"})
public class SysConfParamController {

    @Autowired
    private SysConfParamService sysConfParamService;

    @PostMapping("/add")
    @ApiOperation(value = "新增系统参数")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String add(@RequestBody @Validated(SysConfParamDTO.Add.class) SysConfParamDTO sysConfParamDTO) {
        sysConfParamService.add(sysConfParamDTO);
        return ResultFactory.buildSuccessResult();
    }

    @PostMapping("/update")
    @ApiOperation(value = "修改系统参数")
    @SysLogs("修改系统参数")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String update(@RequestBody @Validated(SysConfParamDTO.Add.class) SysConfParamDTO sysConfParamDTO) {
        sysConfParamService.updateByIdOrName(sysConfParamDTO);
        return ResultFactory.buildSuccessResult();
    }

    @ApiOperation(value = "查询所有系统参数")
    @GetMapping("/list")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String list() {
        return ResultFactory.buildSuccessResult(sysConfParamService.findAll());
    }

    @PostMapping(value = {"/remove/{id}"})
    @ApiOperation(value = "删除系统参数")
    @SysLogs("删除系统参数")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String remove(@PathVariable("id") Integer id) {
        sysConfParamService.remove(id);
        return ResultFactory.buildSuccessResult();
    }
}
