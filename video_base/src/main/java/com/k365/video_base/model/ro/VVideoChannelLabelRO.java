package com.k365.video_base.model.ro;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author Gavin
 * @date 2019/8/16 13:12
 * @description：
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VVideoChannelLabelRO {

    private String vId;

    /**
     * 车牌号
     */
    private String vCode;

    /**
     * 视频状态
     */
    private Integer vStatus;

    /**
     * 视频封面图片
     */
    private String vCover;

    /**
     * 视频时长
     */
    private Integer vTimeLen;

    /**
     * 视频标题
     */
    private String vTitle;

    /**
     * 视频总播放次数
     */
    private Long vPlaySum;

    /**
     * 视频上架时间
     */
    private Date vCreateDate;


    private Integer vcId;

    /**
     * 频道名称
     */
    private String vcName;

    /**
     * 排序
     */
    private Integer vcSort;


    private Integer vlId;

    /**
     * 标签名称
     */
    private String vlName;

    /**
     * 状态
     */
    private Integer vlStatus;

    /**
     * 标签排序
     */
    private Integer vlSort;

    /**
     * 视频频道
     */
    private String videoChannels;

    /**
     * 视频标签
     */
    private String videoLabels;


}
