package com.k365.video_base.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Gavin
 * @date 2019/8/4 14:15
 * @description：
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VideoInfoDTO extends SplitPageDTO implements BaseDTO {

    /**
     * 视频标题
     */
    private String title;

    /**
     * 车牌号
     */
    private String code;

    /**
     * 视频标签信息
     */
    private String[] label;

    /**
     * 视频简介
     */
    private String synopsis;

    /**
     * 视频播放时长
     */
    private Integer timeLen;

    /**
     * 导演
     */
    private String director;

    /**
     * 女优信息
     */
    private Object[] actorInfo;

    /**
     * 视频图标
     */
    private String icon;

    /**
     * 视频封面图片
     */
    private String cover;

    /**
     * 视频分辨率
     */
    private String resolution;

    /**
     * 视频路径
     */
    private String url;

    /**
     * 视频下载路径
     */
    private String saveUrl;

    /**
     * 开始时间
     */
    private String beginTime;

    /**
     * 结束时间
     */
    private String endTime;

}
