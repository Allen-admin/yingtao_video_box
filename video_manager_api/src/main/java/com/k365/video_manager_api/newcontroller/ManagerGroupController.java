package com.k365.video_manager_api.newcontroller;

import com.k365.new_manager_service.ManagerGroupService;
import com.k365.video_base.common.ResultFactory;
import com.k365.video_base.model.dto.ManagerGroupDTO;
import com.k365.video_common.annotation.SysLogs;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Baozi
 * @since 2019-10-22
 */
@RestController
@RequestMapping("/new/group")
@Api(tags = {"推广后台管理后台-分组对应接口"})
public class ManagerGroupController {

    @Autowired
    private ManagerGroupService managerGroupService;

    @PostMapping("/add")
    @ApiOperation(value = "新增组")
    public String add(@RequestBody ManagerGroupDTO managerGroupDTO) {
        return ResultFactory.buildSuccessResult(managerGroupService.add(managerGroupDTO));
    }


    @PostMapping("/update")
    @ApiOperation(value = "修改组")
    @SysLogs("修改组")
    public String update(@RequestBody ManagerGroupDTO managerGroupDTO) {
        return ResultFactory.buildSuccessResult(managerGroupService.update(managerGroupDTO));
    }

    @PostMapping("/remove/{id}")
    @ApiOperation(value = "删除组")
    @SysLogs("删除组")
    public String remove(@PathVariable("id") Integer id) {
        managerGroupService.removeByGroupId(id);
        return ResultFactory.buildSuccessResult();
    }

    @GetMapping(value = {"/get/all"})
    @ApiOperation(value = "获取所有组列表")
    public String list() {

        return ResultFactory.buildSuccessResult(managerGroupService.findAll());
    }

}
