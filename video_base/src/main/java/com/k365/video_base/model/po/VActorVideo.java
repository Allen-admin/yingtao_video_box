package com.k365.video_base.model.po;

import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * <p>
 * VIEW
 * </p>
 *
 * @author Gavin
 * @since 2019-08-30
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class VActorVideo  {

    /**
     * 女优id
     */
    private Integer aId;

    /**
     * 名字首字母
     */
    private String aAcronym;

    /**
     * 女优姓名
     */
    private String aName;

    /**
     * id
     */
    private String vId;

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
     * 视频累计播放次数
     */
    private Long vPlaySum;

    /**
     * 视频播放时长（单位：秒）
     */
    private Integer vTimeLen;

    /**
     * 视频标题
     */
    private String vTitle;

    /**
     * 视频状态
     */
    private Integer vStatus;

    /**
     * 是否为VIP视频
     */
    private Boolean vIsVip;
}
