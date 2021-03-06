package com.k365.video_base.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author Gavin
 * @date 2019/8/11 21:11
 * @description：
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VideoVO {

    private String id;

    /**
     * 视频封面图片
     */
    private String cover;

    /**
     * 视频标题
     */
    private String title;

    /**
     * 视频播放时长
     */
    private Integer timeLen;

    /**
     * 导演
     */
    private String director;

    /**
     * 发行时间
     */
    private Date createDate;

    /**
     * 当天观看次数
     */
    private Long playCountForDay;

    /**
     * 当周观看次数
     */
    private Long playCountForWeek;

    /**
     * 当月观看次数
     */
    private Long playCountForMonth;

    /**
     * 视频简介
     */
    private String synopsis;

    /**
     * 车牌号
     */
    private String code;

    /**
     * 视频累计下载量
     */
    private Long saveSum;

    /**
     * 视频状态
     */
    private Integer status;

    /**
     * 视频累计播放次数
     */
    private Long playSum;

    /**
     * 视频分辨率
     */
    private String resolution;

    /**
     *视频下载地址
     */
    private String  saveUrl;

    /**
     * 视频观看路径
     */
    private String playUrl;

    /**
     * 是否为vip
     */
    private Boolean isVip;

}
