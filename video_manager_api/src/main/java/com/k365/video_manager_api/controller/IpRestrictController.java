package com.k365.video_manager_api.controller;


import com.k365.manager_service.IpRestrictService;
import com.k365.video_base.common.ResultFactory;
import com.k365.video_base.model.dto.IpRestrictDTO;
import com.k365.video_base.model.po.IpRestrict;
import com.k365.video_common.annotation.SysLogs;
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
 * ip黑白名单限制表 前端控制器
 * </p>
 *
 * @author Gavin
 * @since 2019-09-11
 */
@RestController
@RequestMapping("/sys/ip-restrict")
@Api(tags = {"IP限制管理"})
public class IpRestrictController {

    @Autowired
    private IpRestrictService ipRestrictService;

    @PostMapping("/list")
    @ApiOperation(value = "分页查询IP限制列表")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String list(@RequestBody IpRestrictDTO ipRestrictDTO) {
        return ResultFactory.buildSuccessResult(ipRestrictService.getPage(ipRestrictDTO));
    }

    @PostMapping("/add")
    @ApiOperation(value = "新增IP限制")
    @SysLogs("新增IP限制")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String add(@RequestBody IpRestrict ipRestrict) {
        ipRestrictService.add(ipRestrict);
        return ResultFactory.buildSuccessResult();
    }

    @PostMapping("/update")
    @ApiOperation(value = "修改IP限制")
    @SysLogs("修改IP限制")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String update(@RequestBody IpRestrict ipRestrict) {
        ipRestrictService.update(ipRestrict);
        return ResultFactory.buildSuccessResult();
    }

    @PostMapping("/remove")
    @ApiOperation(value = "删除IP限制")
    @SysLogs("删除IP限制")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String remove(@RequestBody IpRestrict ipRestrict) {
        ipRestrictService.remove(ipRestrict);
        return ResultFactory.buildSuccessResult();
    }


}

