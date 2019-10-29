package com.k365.video_manager_api.controller;


import com.k365.manager_service.DomainService;
import com.k365.video_base.model.dto.DomainDTO;
import com.k365.video_common.annotation.SysLogs;
import com.k365.video_common.exception.GeneralException;
import com.k365.video_common.exception.ResponsiveException;
import com.k365.video_base.common.ResultFactory;
import com.k365.video_common.util.IPUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Gavin
 * @since 2019-07-17
 */

@RestController
@RequestMapping("/sys/domain")
@Api(tags = {"APP域名处理"})
public class DomainController {

    @Autowired
    private DomainService domainService;

    @PostMapping("/list")
    @ApiOperation(value = "获取可用域名列表")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String list(@RequestBody DomainDTO domainDTO,ServletRequest request) {
        return ResultFactory.buildSuccessResult(domainService.getByPage(domainDTO,request));
    }

    @PostMapping("/add")
    @ApiOperation(value = "新增域名")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String add(@RequestBody DomainDTO domainDTO) {
        domainService.add(domainDTO);
        return ResultFactory.buildSuccessResult();
    }


    @PostMapping("/update")
    @ApiOperation(value = "修改域名")
    @SysLogs("修改域名")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String update(@RequestBody DomainDTO domainDTO) {
        domainService.update(domainDTO);
        return ResultFactory.buildSuccessResult();
    }

    @GetMapping("/remove/{id}")
    @ApiOperation(value = "删除域名")
    @SysLogs("删除域名")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String remove(@PathVariable("id") Integer id) {
        domainService.remove(id);
        return ResultFactory.buildSuccessResult();
    }

    @PostMapping("/ping")
    @ApiOperation(value = "ping 域名")
    @ApiImplicitParam(paramType = "header", name = "Authorization", value = "身份认证Token")
    public String ping(@RequestBody DomainDTO domainDTO) {
        String domain = domainDTO.getDomain();
        if (StringUtils.isBlank(domain))
            throw new ResponsiveException("域名不能为空！");

        try {
            return ResultFactory.buildSuccessResult(IPUtil.pingDomain(domain, 30000));
        } catch (Exception e) {
            throw new GeneralException(String.format("域名 [%s] ping 失败！", domain), e);
        }
    }


}


