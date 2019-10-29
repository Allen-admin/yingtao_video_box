package com.k365.video_common.handler.sms;

/**
 * @author Gavin
 * @date 2019/7/24 13:14
 * @description：
 */
public class NeteaseSMS implements SMSFactory {

    @Override
    public AbstractSMSProvider getSMSProvider() {
        return new NeteaseProvider();
    }
}
