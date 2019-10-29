package com.k365.video_manager_api.controller;

import com.k365.config.jwt.FilterUtil;
import com.k365.config.jwt.JwtToken;
import com.k365.manager_service.IpRestrictService;
import com.k365.manager_service.SysConfParamService;
import com.k365.manager_service.SysUserService;
import com.k365.video_base.model.dto.SysUserDTO;
import com.k365.video_common.annotation.SysLogs;
import com.k365.video_common.constant.HttpStatusEnum;
import com.k365.video_common.constant.SysParamValueNameEnum;
import com.k365.video_base.common.ResultFactory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Gavin
 * @date 2019/6/30 18:04
 * @description：
 */
@RestController
@RequestMapping(value = {"/account"})
@Api(tags = {"账户相关"})
public class AccountController {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysConfParamService sysConfParamService;

    @Autowired
    private IpRestrictService ipRestrictService;

    @PostMapping(value = "/sign-in")
    @ApiOperation(value = "登录")
    @SysLogs("登录")
    public String signIn(@RequestBody @Validated @ApiParam(value = "登录数据", required = true)
                                 SysUserDTO signInDTO, ServletRequest request, ServletResponse response) {

        //黑白名单访问限制
        if(!ipRestrictService.ipCheck(request)){
            FilterUtil.response502(request, response, HttpStatusEnum.ACCESS_DENIED.getReasonPhrase(),
                    HttpStatusEnum.ACCESS_DENIED.value());
            return null;
        }

        sysUserService.signIn(signInDTO);
        String token = ((JwtToken) SecurityUtils.getSubject().getPrincipal()).getToken();
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader("Authorization",token);
        return ResultFactory.buildSuccessResult(token);
    }


    @GetMapping("/get/auth-param")
    @ApiOperation(value = "获取身份验证码参数")
    public String getAuthParam(ServletRequest request, ServletResponse response) {
        //黑白名单访问限制
        if(!ipRestrictService.ipCheck(request)){
            FilterUtil.response502(request, response, HttpStatusEnum.ACCESS_DENIED.getReasonPhrase(),
                    HttpStatusEnum.ACCESS_DENIED.value());
            return null;
        }

        Object val = sysConfParamService.getValByValName(SysParamValueNameEnum.IS_NEED_AUTH);
        boolean result = val == null ? false : Boolean.valueOf(val.toString());
        return ResultFactory.buildSuccessResult(result);
    }


}
