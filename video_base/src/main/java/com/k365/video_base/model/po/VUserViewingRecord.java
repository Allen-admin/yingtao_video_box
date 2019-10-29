package com.k365.video_base.model.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * VIEW
 * </p>
 *
 * @author Gavin
 * @since 2019-09-02
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
public class VUserViewingRecord {

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
    private Date vCreateDate;

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
    private String uId;

    /**
     * 播放时长
     */
    private Date uvrPlayTimeLen;

    /**
     * 记录时间
     */
    private Date uvrRecordTime;

    /**
     * 是否为vip
     */
    private Boolean VIsVip;
}
