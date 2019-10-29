package com.k365.video_common.constant;

import java.io.Serializable;

/**
 * @author Gavin
 * @date 2019/6/24 10:23
 * @description：系统参数code类型
 */
public enum SysParamCodeEnum {
    SYS_LOGIN_PARAM("sys_login_param","后台系统登录参数"),
    FRONT_CONTROL_PARAM("front_control_param","前端控制参数");

    private Serializable code;

    private Serializable name;

    SysParamCodeEnum(Serializable code, Serializable name) {
        this.code = code;
        this.name = name;
    }

    public Serializable getCode() {
        return code;
    }

    public void setCode(Serializable code) {
        this.code = code;
    }

    public Serializable getName() {
        return name;
    }

    public void setName(Serializable name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "{\\\"code\\\":" + code +
                ",\\\"name\\\":\\\"" + name+ "\\\"}" ;
    }
}
