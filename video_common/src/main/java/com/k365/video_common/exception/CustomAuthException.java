package com.k365.video_common.exception;

import org.apache.shiro.authc.AuthenticationException;

/**
 * @author Gavin
 * @date 2019/7/5 17:38
 * @description：
 */
public class CustomAuthException extends AuthenticationException{

    private static final long serialVersionUID = -262073841283188506L;

    public CustomAuthException(String message) {
        super(message);
    }
}
