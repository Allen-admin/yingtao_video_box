package com.k365.video_common.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Gavin
 * @date 2019/8/4 21:04
 * @description：
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HttpClientResult implements Serializable {

    private static final long serialVersionUID = 5916394281851027725L;
    /**
     * 响应状态码
     */
    private int code;

    /**
     * 响应数据
     */
    private Object content;
}
