package com.k365.video_common.constant;

/**
 * @author Gavin
 * @date 2019/6/22 20:38
 * @description：
 */
public enum ExCodeMsgEnum {

    NOT_INPUT_AUTH(1001,"请输入身份验证码"),
    AUTH_WRONG(1002,"身份验证码错误"),
    LOGIN_FAIL(1003,"用户名或密码错误，登陆失败"),
    ACCOUNT_EXISTS(1004,"当前用户名已存在"),
    PROVOLEGE_FAILED(1005,"权限认证失败"),
    NOT_SING_IN(1006,"用户未登录或身份异常"),
    LOGOUT_FAIL(1007,"注销登录失败"),
    NO_ACCESS_TO_RESOURCE(1008,"无法访问资源"),
    NO_AUTH_SIGN(1009,"未含授权标示，禁止访问"),
    IDENTITY_HAS_EXPIRED(1010,"身份已过期或无效，请重新认证"),

    AUTH_ACCOUNT_LOCK(1011,"账号被锁定"),
    AUTH_UNKNOWN_ACCOUNT(1012,"账号异常，请重新输入"),
    AUTH_EXCESSIVE_ATTEMPTS(1013,"认证次数超过限制,账号已被锁定,请联系管理员解锁"),
    AUTH_CONCURRENT_ACCESS(1014,"当前账号存在多端登录风险，请联系管理员重置密码"),
    AUTH_EXPIRED_CREDENTIALS(1015,"凭证过期，请重新登录"),
    SYS_PARAM_EXISTS(1016,"当前系统参数已存在，请到编辑页面进行修改"),
    AUTH_DISABLED_ACCOUNT(1017,"账号被禁用"),
    ACCOUNT_DELETED(1018,"账号已被删除"),
    ACCOUNT_NOT_EXISTS(1019,"当前账号不存在或已被删除");




    private int code;

    private String msg;

    ExCodeMsgEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int code() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String msg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
