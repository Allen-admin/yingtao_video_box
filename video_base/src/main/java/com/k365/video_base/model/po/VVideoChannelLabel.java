package com.k365.video_base.model.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * <p>
 * VIEW
 * </p>
 *
 * @author Gavin
 * @since 2019-08-15
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VVideoChannelLabel {

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
     * 视频标题
     */
    private String vTitle;

    /**
     * 视频总播放次数
     */
    private Long vPlaySum;

    /**
     * 播放时长
     */
    private Integer vTimeLen;

    /**
     * 视频上架时间
     */
    private Date vCreateDate;

    /**
     * 视频播放地址
     */
    private String vPlayUrl;

    /**
     * 视频下载地址
     */
    private String vSaveUrl;


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
     * 是否为VIP视频
     */
    private Boolean vIsVip;

}
