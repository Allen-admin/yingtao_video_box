package com.k365.video_common.handler.sms;

/**
 * @author Gavin
 * @date 2019/7/24 13:13
 * @description：
 */
public interface SMSFactory {

    AbstractSMSProvider getSMSProvider();
}
