package com.k365.video_common.exception;

import com.k365.video_common.constant.ExCodeMsgEnum;

/**
 * @author Gavin
 * @date 2019/6/24 16:43
 * @description：
 */
public class ResponsiveException extends RuntimeException {

    private static final long serialVersionUID = 6429677491925087437L;

    private ExCodeMsgEnum msgEnum;

    public ResponsiveException(ExCodeMsgEnum msgEnum) {
        super(msgEnum.msg());
        this.msgEnum = msgEnum;
    }

    public ResponsiveException(String message) {
        super(message);
    }

    public ExCodeMsgEnum getMsgEnum() {
        return msgEnum;
    }
}
