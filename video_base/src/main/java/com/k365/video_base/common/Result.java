package com.k365.video_base.common;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Gavin
 * @date 2019/6/19 13:08
 * @description：响应客户端数据模型
 */
@Data
@Builder
public class Result implements Serializable {

    private static final long serialVersionUID = -4757604760606575331L;
    /**
     * 响应状态码
     */
    private int code;
    /**
     * 响应提示信息
     */
    private String message;
    /**
     * 响应结果对象
     */
    private Object data;

    public Result(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

}
