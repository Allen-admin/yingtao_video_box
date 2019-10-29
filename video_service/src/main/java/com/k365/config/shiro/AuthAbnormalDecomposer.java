package com.k365.config.shiro;

import com.k365.video_common.constant.ExCodeMsgEnum;
import com.k365.video_common.exception.CustomAuthException;
import org.apache.shiro.authc.*;

/**
 * @author Gavin
 * @date 2019/7/5 15:28
 * @description：shiro 登录、认证异常分解器
 */
public class AuthAbnormalDecomposer {
    /**
     * @param ex
     * @return登录异常
     */
    public static synchronized ExCodeMsgEnum decoAuthenticationException(Throwable ex) {
        ExCodeMsgEnum exCodeMsg ;
        if (ex instanceof LockedAccountException) {
            //账号被锁定
            exCodeMsg = ExCodeMsgEnum.AUTH_ACCOUNT_LOCK;

        }
        else if (ex instanceof DisabledAccountException) {
            //账号被禁用
            exCodeMsg = ExCodeMsgEnum.AUTH_DISABLED_ACCOUNT;

        }
        else if (ex instanceof ExcessiveAttemptsException) {
            //多次登录密码错误
            exCodeMsg = ExCodeMsgEnum.AUTH_EXCESSIVE_ATTEMPTS;
        }
        else if (ex instanceof UnknownAccountException) {
            //账号异常，请重新输入
            exCodeMsg = ExCodeMsgEnum.AUTH_UNKNOWN_ACCOUNT;

        }
        else if (ex instanceof ConcurrentAccessException) {
            //当前账号存在多端登录风险，请联系管理员重置密码
            exCodeMsg = ExCodeMsgEnum.AUTH_CONCURRENT_ACCESS;

        }
        else if (ex instanceof AccountException) {
            //账号异常，请重新输入
            exCodeMsg = ExCodeMsgEnum.AUTH_UNKNOWN_ACCOUNT;

        }
        else if (ex instanceof ExpiredCredentialsException) {
            //凭证过期，请重新登录
            exCodeMsg = ExCodeMsgEnum.AUTH_EXPIRED_CREDENTIALS;

        }
        else if(ex instanceof CustomAuthException) {
            //自定义登录认证异常
            exCodeMsg = ExCodeMsgEnum.LOGIN_FAIL;
            exCodeMsg.setMsg(ex.getMessage());
        }else {
            //用户名或密码错误，登陆失败
            exCodeMsg = ExCodeMsgEnum.LOGIN_FAIL;
        }
        return exCodeMsg;
    }
}
