package com.k365.video_manager_api.newcontroller;

import com.k365.new_manager_service.ManagerEmployeeService;
import com.k365.video_base.common.ResultFactory;
import com.k365.video_base.model.dto.ManagerEmployeeDTO;
import com.k365.video_common.annotation.SysLogs;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Baozi
 * @since 2019-10-21
 */
@RestController
@RequestMapping("/new/employee")
@Api(tags = {"推广后台管理后台-员工对应接口"})
public class ManagerEmployeeController {

    @Autowired
    @Lazy(true)
    private ManagerEmployeeService managerEmployeeService;

    @PostMapping(value = "/sign-in")
    @ApiOperation(value = "登录")
    @SysLogs("登录")
    public String signIn(@RequestBody @Validated @ApiParam(value = "登录数据", required = true)
                                     ManagerEmployeeDTO managerEmployeeDTO,
                         ServletRequest request, ServletResponse response) {

        return ResultFactory.buildSuccessResult(managerEmployeeService.signIn(managerEmployeeDTO));
    }

    @PostMapping(value = "/getAllUser")
    @ApiOperation(value = "查询所有用户列表")
    public String getAllUser(@RequestBody ManagerEmployeeDTO managerEmployeeDTO) {
        return ResultFactory.buildSuccessResult(managerEmployeeService.getAllUser(managerEmployeeDTO));
    }

    @PostMapping(value = "/getLevel1List")
    @ApiOperation(value = "查询组长列表")
    public String getLevel1List(@RequestBody ManagerEmployeeDTO managerEmployeeDTO) {
        return ResultFactory.buildSuccessResult(managerEmployeeService.getLevel1List(managerEmployeeDTO));
    }

    @PostMapping(value = "/getLevel2List")
    @ApiOperation(value = "查询组员列表")
    public String getLevel2List(@RequestBody ManagerEmployeeDTO managerEmployeeDTO) {
        return ResultFactory.buildSuccessResult(managerEmployeeService.getLevel2List(managerEmployeeDTO));
    }

    @PostMapping("/add")
    @ApiOperation(value = "新增员工")
    public String add(@RequestBody ManagerEmployeeDTO managerEmployeeDTO) {
        return ResultFactory.buildSuccessResult(managerEmployeeService.add(managerEmployeeDTO));
    }

    @PostMapping("/addAdmin")
    @ApiOperation(value = "新增管理员")
    public String addAdmin(@RequestBody ManagerEmployeeDTO managerEmployeeDTO) {
        return ResultFactory.buildSuccessResult(managerEmployeeService.addAdmin(managerEmployeeDTO));
    }

    @PostMapping("/update")
    @ApiOperation(value = "修改员工信息")
    @SysLogs("修改员工信息")
    public String update(@RequestBody ManagerEmployeeDTO managerEmployeeDTO) {
        managerEmployeeService.update(managerEmployeeDTO);
        return ResultFactory.buildSuccessResult();
    }

    @PostMapping("/remove/{id}")
    @ApiOperation(value = "删除员工")
    @SysLogs("删除员工")
    public String remove(@PathVariable("id") Integer id) {
        managerEmployeeService.removeByEmployeeId(id);
        return ResultFactory.buildSuccessResult();
    }

    @PostMapping(value = "/getPersonalInfo/{id}")
    @ApiOperation(value = "查询员工个人信息")
    public String getPersonalInfo(@PathVariable("id") Integer id) {
        return ResultFactory.buildSuccessResult(managerEmployeeService.getPersonalInfo(id));
    }

    @PostMapping(value = "/getPersonalCommonend")
    @ApiOperation(value = "查询员工个人推广信息")
    public String getPersonalCommonend(@RequestBody ManagerEmployeeDTO managerEmployeeDTO) {
        return ResultFactory.buildSuccessResult(managerEmployeeService.getPersonalCommonend(managerEmployeeDTO));
    }

}
