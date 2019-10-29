package com.k365.config.jwt;

import com.k365.video_common.constant.Constants;
import com.k365.video_common.constant.HttpStatusEnum;
import com.k365.video_common.util.IPUtil;
import com.k365.video_common.util.JwtUtil;
import com.k365.video_common.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.LogoutFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * @author Gavin
 * @date 2019/7/7 19:08
 * @description：退出登录过滤器
 */
@Slf4j
public class VmSignOutFilter extends LogoutFilter {

    @Autowired
    private RedisUtil cache;

    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpRequest = WebUtils.toHttp(request);
        String token = this.getAuthzHeader(request);
        String redirectUrl = null;
        String account = null;
        if(StringUtils.isNotBlank(token)) {
            try {
                Subject subject = getSubject(request, response);
                redirectUrl = getRedirectUrl(request, response, subject);
                account = JwtUtil.getAccount(token);
                String refreshTokenCacheKey = Constants.CACHE_SHIRO_SYS_USER_TOKEN_REFRESH + account;
                //清空缓存
                if (cache.hasKey(refreshTokenCacheKey)) {
                    cache.del(refreshTokenCacheKey);
                }
                subject.logout();


            } catch (Exception e) {
                log.error("用户【{}】退出登录失败，msg:{}", account == null ?
                        IPUtil.getClientIp(httpRequest) : account, e.getMessage());
            }
            issueRedirect(request, response, redirectUrl);

        }else{
            FilterUtil.response401(request,response,
                    HttpStatusEnum.LACK_AUTH_SIGN.getReasonPhrase(),HttpStatusEnum.LACK_AUTH_SIGN.value());

        }
        return false;

    }

    public String getAuthzHeader(ServletRequest request) {
        HttpServletRequest httpRequest = WebUtils.toHttp(request);
        return httpRequest.getHeader("Authorization");
    }


}
