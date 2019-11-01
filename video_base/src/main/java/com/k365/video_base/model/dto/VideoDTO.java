package com.k365.video_base.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

/**
 * <p>
 * <p>
 * </p>
 *
 * @author Gavin
 * @since 2019-07-17
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VideoDTO extends SplitPageDTO implements BaseDTO {

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
     * 视频简介
     */
    private String synopsis;

    /**
     * 车牌号
     */
    private String code;

    /**
     * 视频状态
     */
    private Integer status;

    /**
     *视频下载地址
     */
    private String  saveUrl;

    /**
     * 视频观看路径
     */
    private String playUrl;

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
     * 视频累计播放次数
     */
    private Long playSum;

    /**
     * 是否为vip
     */
    private Boolean isVip;

    /**
     * 视频标签id
     */
    @NotEmpty(groups = {Update.class,Add.class},message = "所属标签不能为空")
    private Integer[] videoLabelIds;

    /**
     * 视频频道id
     */
    /*@NotEmpty(groups = {Update.class,Add.class},message = "所属频道不能为空")*/
    private Integer[] videoChannelIds;

    /**
     * 视频女优id
     */
    //@NotEmpty(groups = {Update.class,Add.class},message = "参演女优不能为空")
    private Integer[] videoActorIds;

    /**
     * 视频主题id
     */
    //@NotEmpty(groups = {Update.class,Add.class},message = "所属主题不能为空")
    private Integer[] videoSubjectIds;

}
