package com.k365.video_base.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author Allen
 * @date 2019/11/05
 * @description：
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChannelVO {

    private String id;

    /**
     * 渠道名称
     */
    private String name;

    /**
     * 渠道链接
     */
    private String link;

    /**
     * 创建时间
     */
    private Date CreateDate;

}
