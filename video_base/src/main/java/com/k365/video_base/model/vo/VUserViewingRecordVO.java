package com.k365.video_base.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author Gavin
 * @date 2019/9/2 15:52
 * @description：
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VUserViewingRecordVO {

    /**
     * 车牌号
     */
    private String vCode;

    /**
     * 视频封面图片
     */
    private String vCover;

    /**
     * 创建时间
     */
    private LocalDateTime vCreateDate;

    /**
     * id
     */
    private String vId;

    /**
     * 视频累计播放次数
     */
    private Long vPlaySum;

    /**
     * 视频状态
     */
    private Integer vStatus;

    /**
     * 视频播放时长（单位：秒）
     */
    private Integer vTimeLen;

    /**
     * 视频标题
     */
    private String vTitle;

    /**
     * 用户id
     */
    private String uvrId;

    /**
     * 视频id
     */
    private Date uvrPlayTimeLen;

    /**
     * 播放时间
     */
    private Date uvrRecordTime;

}
