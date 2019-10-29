package com.k365.video_manager_api.controller;


import com.k365.manager_service.TaskService;
import com.k365.video_base.common.TaskTypeEnum;
import com.k365.video_base.model.po.Task;
import com.k365.video_common.annotation.SysLogs;
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
 * @since 2019-07-17
 */
@RestController
@RequestMapping(value = "/spread/task")
@Api(tags = {"任务管理"})
public class TaskController {

    @Autowired
    public TaskService taskService;

    @PostMapping(value = "/add")
    @ApiOperation(value = "新增任务")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String add(@RequestBody Task task) {
        taskService.add(task);
        return ResultFactory.buildSuccessResult();
    }

    @GetMapping(value = "/get/{type}")
    @ApiOperation(value = "获取任务列表")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String getByType(@PathVariable("type") Integer type) {
        TaskTypeEnum taskTypeEnum = TaskTypeEnum.getByKey(type);
        return ResultFactory.buildNotNullResult(taskTypeEnum == null ? null : taskService.findTasksByType(taskTypeEnum));
    }

    @PostMapping(value = "/update")
    @ApiOperation(value = "修改任务")
    @SysLogs("修改任务")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String update(@RequestBody Task task) {
        taskService.update(task);
        return ResultFactory.buildSuccessResult();
    }


    @GetMapping(value = "/remove/{id}")
    @ApiOperation(value = "删除任务")
    @SysLogs("删除任务")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String remove(@PathVariable("id") Integer id) {
        taskService.remove(id);
        return ResultFactory.buildSuccessResult();
    }

    /*@PostMapping(value = "/add/spread")
    @ApiOperation(value = "新增推广任务")
    @SysLogs("新增推广任务")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String addSpread(@RequestBody @Validated @ApiParam(value = "新增任务") List<TaskDTO> taskDTOList) {
        taskService.add(taskDTOList);
        return ResultFactory.buildSuccessResult();
    }*/

   /* @PostMapping(value = "/add/click-ad")
    @ApiOperation(value = "新增点击广告任务")
    @SysLogs("新增点击广告任务")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String addClickAd(@RequestBody Map<String,Object> map) {
        SysConfParamDTO paramDTO = SysConfParamDTO.builder().paramCode(SysParamCodeEnum.FRONT_CONTROL_PARAM.name())
                .paramName(SysParamNameEnum.MOBILE_AD_CLICK.code())
                .paramRemark(SysParamNameEnum.MOBILE_AD_CLICK.getName()).paramValue(map).build();
        sysConfParamService.add(paramDTO);
        return ResultFactory.buildSuccessResult();
    }*/

    /*@GetMapping(value = "/get/click-ad")
    @ApiOperation(value = "获取点击广告任务")
    @SysLogs("获取点击广告任务")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String getClickAd() {
        return ResultFactory.buildSuccessResult(sysConfParamService.findParamValByName(SysParamNameEnum.MOBILE_AD_CLICK));
    }*/


}

