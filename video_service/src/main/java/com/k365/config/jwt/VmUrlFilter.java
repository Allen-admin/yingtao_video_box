package com.k365.config.jwt;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.k365.config.shiro.AuthAbnormalDecomposer;
import com.k365.manager_service.IpRestrictService;
import com.k365.video_common.constant.Constants;
import com.k365.video_common.constant.ExCodeMsgEnum;
import com.k365.video_common.constant.HttpStatusEnum;
import com.k365.video_common.exception.ResponsiveException;
import com.k365.video_common.util.IPUtil;
import com.k365.video_common.util.JwtUtil;
import com.k365.video_common.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * @author Gavin
 * @date 2019/6/20 18:29
 * @description：
 */
@Slf4j
public class VmUrlFilter extends BasicHttpAuthenticationFilter {

    @Autowired
    private RedisUtil cache;

    @Autowired
    private IpRestrictService ipRestrictService;

    private static final String servletContextPath = "/videomanager";

    /*private static List<String> anonurls = Arrays.asList(
            "/videomanager/sign-in",
            "/videomanager/sign-out",
            "/videomanager/sysparam/get/auth-param",
            "/videomanager/druid/",
            "/videomanager/swagger-ui",
            "/videomanager/webjars/springfox-swagger-ui",
            "/videomanager/swagger-resources",
            "/videomanager/v2/api-docs",
            "/videomanager/csrf"
    );
    */


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
      /*  if(!ipRestrictService.ipCheck(request)){
            FilterUtil.response502(request, response, HttpStatusEnum.ACCESS_DENIED.getReasonPhrase(),
                    HttpStatusEnum.ACCESS_DENIED.value());
            return false;
        }*/

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

                if (null != mappedValue) {
                    String[] value = (String[]) mappedValue;
                    for (String permission : value) {
                        if (StringUtils.isBlank(permission)) {
                            continue;
                        }
                        if (subject.isPermitted(permission)) {
                            return true;
                        }
                    }
                }
                if (null == subject.getPrincipal()) {//表示没有登录，返回登录提示
                    httpStatus = HttpStatusEnum.NO_LOGIN;
                } else {
                    httpStatus = HttpStatusEnum.NO_ACCESS;
                }

            } catch (AuthenticationException e) {
                Throwable throwable = e.getCause();
                if (throwable instanceof TokenExpiredException) {
                    if (refreshToken(request, response))//token过期，无痛刷新token
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


    /**
     * 刷新AccessToken，进行判断RefreshToken是否过期，未过期就返回新的AccessToken且继续正常访问
     */
    protected boolean refreshToken(ServletRequest request, ServletResponse response) {
        // 获取AccessToken(Shiro中getAuthzHeader方法已经实现)
        String token = this.getAuthzHeader(request);
        // 获取当前Token的帐号信息
        String account = JwtUtil.getAccount(token);
        String refreshTokenCacheKey = Constants.CACHE_SHIRO_SYS_USER_TOKEN_REFRESH + account;
        // 判断Redis中RefreshToken是否存在
        if (cache.hasKey(refreshTokenCacheKey)) {
            // 获取RefreshToken时间戳,及AccessToken中的时间戳
            // 相比如果一致，进行AccessToken刷新
            String currentTimeMillisRedis = cache.get(refreshTokenCacheKey).toString();
            String tokenMillis = JwtUtil.getClaim(token, Constants.CURRENT_TIME_MILLIS);

            if (Objects.equals(tokenMillis,currentTimeMillisRedis)) {

                // 设置RefreshToken中的时间戳为当前最新时间戳
                String currentTimeMillis = String.valueOf(System.currentTimeMillis());

                cache.set(refreshTokenCacheKey, currentTimeMillis, JwtUtil.DEFAULT_EXPIRE_TIME);

                // 刷新AccessToken，为当前最新时间戳
                token = JwtUtil.sign(account, currentTimeMillis);

                // 使用AccessToken 再次提交给ShiroRealm进行认证，如果没有抛出异常则登入成功，返回true
                JwtToken jwtToken = JwtToken.builder().token(token).username(account).build();
                this.getSubject(request, response).login(jwtToken);

                // 设置响应的Header头新Token
                HttpServletResponse httpServletResponse = (HttpServletResponse) response;
                httpServletResponse.setHeader(Constants.REQUEST_AUTH_HEADER, token);
                httpServletResponse.setHeader("Access-Control-Expose-Headers", Constants.REQUEST_AUTH_HEADER);
                return true;
            }
        }
        return false;
    }


    /**
     * 判断用户是否想要登入。
     * 检测header里面是否包含Authorization字段即可
     */
    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        HttpServletRequest req = (HttpServletRequest) request;
        String authorization = req.getHeader("Authorization");
        return authorization != null;
    }

    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String authorization = httpServletRequest.getHeader(Constants.REQUEST_AUTH_HEADER);
        if (StringUtils.isBlank(authorization)) {
            return false;
        }
        JwtToken token = JwtToken.builder().token(authorization).build();
        // 提交给realm进行登入，如果错误他会抛出异常并被捕获
        Subject subject = getSubject(request, response);
        subject.login(token);

        // 如果没有抛出异常则代表登入成功，返回true
        return true;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {
        return false;
    }

    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpRequest = WebUtils.toHttp(request);
        HttpServletResponse httpResponse = WebUtils.toHttp(response);
        if (httpRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpResponse.setHeader("Access-control-Allow-Origin", httpRequest.getHeader("Origin"));
            httpResponse.setHeader("Access-Control-Allow-Methods", httpRequest.getMethod());
            httpResponse.setHeader("Access-Control-Allow-Headers", httpRequest.getHeader("Access-Control-Request-Headers"));
            httpResponse.setStatus(HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(request, response);
    }
}
