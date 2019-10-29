package com.k365.video_manager_api.controller;


import com.k365.manager_service.SysLogService;
import com.k365.video_base.common.ResultFactory;
import com.k365.video_base.model.so.SysLogSO;
import com.k365.video_common.annotation.SysLogs;
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
 * @since 2019-09-11
 */
@RestController
@RequestMapping("/platform/sys-log")
@Api(tags = {"系统日志管理"})
public class SysLogController {

    @Autowired
    private SysLogService sysLogService;

    @PostMapping("/search")
    @ApiOperation(value = "分页查询系统日志")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String list(@RequestBody SysLogSO sysLogSO) {
        return ResultFactory.buildSuccessResult(sysLogService.search(sysLogSO));
    }

    @PostMapping("/remove/{id}")
    @ApiOperation(value = "删除系统日志")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String remove( @PathVariable("id") Integer id) {
        sysLogService.removeById(id);
        return ResultFactory.buildSuccessResult();
    }

}

