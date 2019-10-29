package com.k365.video_common.handler.sms;

import java.util.Random;

/**
 * @author Gavin
 * @date 2019/7/24 11:03
 * @description：
 */
public interface AbstractSMSProvider {

    /**
     * http请求 内容类型
     */
    String CONTENT_TYPE = "application/x-www-form-urlencoded;charset=utf-8";

    /**
     * 编解码字符集
     */
    String ENCODING = "utf-8";

    /**
     * 成功状态码
     */
    int SUCCESS_CODE = 200;

    /**
     * 失败状态码
     */
    int FAIL_CODE = -1;

    /**
     * 验证码有效时间 单位：秒
     */
    long ACTIVE_TIME = 60 * 3;

    /**
     * 验证码长度，范围4～10，默认为4
     */
    String CODELEN = "6";


    /**
     * 发送短信验证码
     */
    int sendCode(String mobile, String code) throws Exception;

    /**
     * 呼叫语音验证码
     */
    int getVoiceCode(String mobile, String code) throws Exception;

    /**
     * 检验验证码
     */
    int checkCode(String mobile, String code) throws Exception;


    default String getVerifyCode() {
        return String.valueOf(new Random().nextInt(899999) + 100000);
    }


}
