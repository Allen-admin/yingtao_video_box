package com.k365.config.shiro;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.k365.config.jwt.JwtToken;
import com.k365.video_base.common.SysUserContext;
import com.k365.manager_service.SysUserService;
import com.k365.video_common.constant.Constants;
import com.k365.video_common.constant.UserStatusEnum;
import com.k365.video_common.exception.CustomAuthException;
import com.k365.video_common.util.JwtUtil;
import com.k365.video_common.util.MD5Util;
import com.k365.video_common.util.RedisUtil;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Gavin
 * @date 2019/6/21 21:42
 * @description：匹配用户登录使用的令牌和数据库中保存的用户信息是否匹配。
 */
public class CredentialsMatcher extends SimpleCredentialsMatcher {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private RedisUtil cache;

    /**
     * 进行密码的比对,验证密码是否正确
     */
    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        JwtToken jwtToken = (JwtToken) token;
        String sysUserInfo = Constants.CACHE_CURRENT_SYS_USER_INFO + jwtToken.getUsername();
        Object accountCredentials = getCredentials(info);
        if (jwtToken.getPassword() != null) {
            Object tokenCredentials = MD5Util.mixKeyMD5(jwtToken.getPassword());

            String pwdWrongCountKey = Constants.CACHE_SHIRO_SYS_USER_PWD_WRONG_COUNT + jwtToken.getUsername();
            if (!accountCredentials.equals(tokenCredentials)) {
                //密码比对错误
                int wrongCount = 6;
                if(cache.hasKey(pwdWrongCountKey)){
                    //从缓存中获取错误次数，一天之内错误6次就锁定当前账号
                    wrongCount = Integer.parseInt(cache.get(pwdWrongCountKey).toString());
                    cache.del(pwdWrongCountKey);
                    if(wrongCount > 1){
                        cache.set(pwdWrongCountKey,--wrongCount,(System.currentTimeMillis()+(1000*60*60*24)) / 1000);

                    }else{
                        //第五次错误锁定账号并清除缓存
                        sysUserService.statusChangeByUName(jwtToken.getUsername(), UserStatusEnum.LOCKED);
                        throw new ExcessiveAttemptsException();
                    }
                }else{
                    cache.set(pwdWrongCountKey,--wrongCount,(System.currentTimeMillis()+(1000*60*60*24)) / 1000);
                }

                throw new CustomAuthException(String.format("密码错误，您剩余%s次尝试机会",wrongCount));
            }else if(cache.hasKey(pwdWrongCountKey)){
                cache.del(pwdWrongCountKey);
            }
        } else {
            boolean verify = JwtUtil.verify(jwtToken.getToken(), jwtToken.getUsername());
            if (!verify) {

                if(SysUserContext.getCurrentSysUser() != null &&
                        SysUserContext.getCurrentSysUser().getTryLoginCount() > 1)
                    throw new ExpiredCredentialsException("Token已过期");//如果尝试refreshToken次数大于1，返回401，避免进入死循环


                throw new TokenExpiredException("Token已过期");

            }
        }
        return true;
    }
}
