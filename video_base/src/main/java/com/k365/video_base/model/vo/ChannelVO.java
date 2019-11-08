package com.k365.video_base.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

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

    private Integer id;

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
    private Date createDate;

    /**
     *渠道编号
     */
    private String channelCode;

    /**
     * 数量
     */
    private Integer channelTotal;

}
