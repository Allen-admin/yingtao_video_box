package com.k365.config.jwt;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.k365.config.shiro.AuthAbnormalDecomposer;
import com.k365.manager_service.IpRestrictService;
import com.k365.video_common.constant.ExCodeMsgEnum;
import com.k365.video_common.constant.HttpStatusEnum;
import com.k365.video_common.exception.ResponsiveException;
import com.k365.video_common.util.IPUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * @author Gavin
 * @date 2019/7/3 15:23
 * @description：
 */
@Slf4j
public class AllowJwtFilter extends VmUrlFilter {

    private static final String servletContextPath = "/videomanager";

    @Autowired
    private IpRestrictService ipRestrictService;

    /**
     * 是否允许访问
     *
     * @param request
     * @param response
     * @param mappedValue
     * @return
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {

        //黑白名单访问限制
        if (!ipRestrictService.ipCheck(request)) {
            FilterUtil.response502(request, response, HttpStatusEnum.ACCESS_DENIED.getReasonPhrase(),
                    HttpStatusEnum.ACCESS_DENIED.value());
            return false;
        }

        HttpServletRequest req = WebUtils.toHttp(request);

        //判断是否为视频APP请求
        if (!servletContextPath.equalsIgnoreCase(req.getContextPath().trim())) {
            return true;
        }

        HttpStatusEnum httpStatus = HttpStatusEnum.ILLEGAL_REQUEST;
        if (isLoginAttempt(request, response)) {

            Subject subject = getSubject(request, response);
            try {
                this.executeLogin(request, response);
                if (!subject.isAuthenticated()) {
                    throw new ResponsiveException(ExCodeMsgEnum.IDENTITY_HAS_EXPIRED);
                }

                return true;

            } catch (AuthenticationException e) {
                Throwable throwable = e.getCause();
                if (throwable instanceof TokenExpiredException) {
                    if (super.refreshToken(request, response))//token过期，无痛刷新token
                        return true;

                    httpStatus = HttpStatusEnum.TOKEN_INVALID;
                } else {
                    ExCodeMsgEnum exCodeMsg = AuthAbnormalDecomposer.decoAuthenticationException(throwable);
                    log.info("用户IP[{}]登陆失败,原因[{}]", IPUtil.getClientIp(req), exCodeMsg.msg() + " : " + e.getMessage());
                    httpStatus.setValue(exCodeMsg.code());
                    httpStatus.setReasonPhrase(exCodeMsg.msg());
                }

            } catch (Exception e) {
                log.info("【401】token认证失败，msg：{},{}:{}", String.format("用户IP[%s]登陆失败",
                        IPUtil.getClientIp(req)), e.getClass().getName(), e.getMessage());
            }

        } else {

            httpStatus = HttpStatusEnum.LACK_AUTH_SIGN;
            log.info("【401】{}，缺少Token标识", String.format("用户IP[%s]权限认证失败",
                    IPUtil.getClientIp(req)));

        }

        FilterUtil.response401(request, response, httpStatus.getReasonPhrase(),
                httpStatus.value());

        return false;
    }

}
