package com.k365.video_manager_api.controller;


import com.k365.manager_service.AppVersionService;
import com.k365.video_base.model.po.AppVersion;
import com.k365.video_base.common.ResultFactory;
import com.k365.video_common.annotation.SysLogs;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Gavin
 * @since 2019-07-17
 */
@RestController
@RequestMapping("/sys/app-version")
@Api(tags = {"APP版本管理"})
public class AppVersionController {

    @Autowired
    private AppVersionService appVersionService;

    @ApiOperation(value = "新增版本")
    @PostMapping("/add")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String add(@RequestBody AppVersion appVersion){
        appVersionService.add(appVersion);
        return ResultFactory.buildSuccessResult();
    }

    @ApiOperation(value = "修改版本")
    @PostMapping("/update")
    @SysLogs("修改版本")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String update(@RequestBody AppVersion appVersion){
        appVersionService.update(appVersion);
        return ResultFactory.buildSuccessResult();
    }

    @ApiOperation(value = "删除版本")
    @PostMapping("/remove/{id}")
    @SysLogs("删除版本")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String remove(@PathVariable("id") Integer id){
        appVersionService.removeById(id);
        return ResultFactory.buildSuccessResult();
    }

    @ApiOperation(value = "查询所有版本")
    @PostMapping("/list")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String list(){
        return ResultFactory.buildSuccessResult(appVersionService.findAll());
    }

}

