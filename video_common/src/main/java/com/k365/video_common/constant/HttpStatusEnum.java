package com.k365.video_common.constant;


/**
 * @author Gavin
 * @date 2019/6/24 17:32
 * @description：
 */
public enum  HttpStatusEnum {
    SUCCESS(1000,"请求成功"),
    MISSING_PARAM(1001,"缺失参数"),
    WRONG_PARAM_TYPE(1002,"参数类型错误"),
    ILLEGAL_PARAM(1003,"非法参数"),
    HANDLE_REQUEST_FAIL(1004,"处理请求失败"),
    SHOW_TIPS(1005,"显示message提示用户"),
    ILLEGAL_REQUEST(1006,"非法请求"),
    TOKEN_INVALID(1007,"Token失效，请重新登录"),
    LACK_AUTH_SIGN(1008,"缺少Token身份标识"),
    NO_LOGIN(1009,"该资源需要登录访问"),
    NO_ACCESS(1010,"对不起，您无权访问该资源"),
    INTERNAL_SERVER_ERROR(1011,"服务器内部错误"),
    NOT_DATA(1012,"数据暂无"),
    ACCESS_DENIED(1013,"拒绝访问");


    private int value;
    private String reasonPhrase;

    HttpStatusEnum(int value, String reasonPhrase) {
        this.value = value;
        this.reasonPhrase = reasonPhrase;
    }

    public int value() {
        return this.value;
    }

    public String getReasonPhrase() {
        return this.reasonPhrase;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setReasonPhrase(String reasonPhrase) {
        this.reasonPhrase = reasonPhrase;
    }
}
