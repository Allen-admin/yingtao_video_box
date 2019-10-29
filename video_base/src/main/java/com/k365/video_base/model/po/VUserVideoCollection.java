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
 * @since 2019-08-29
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class VUserVideoCollection {

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
     * id
     */
    private String uId;

    /**
     * 收藏时间
     */
    private Date uvcCreateDate;

    /**
     * 是否为vip
     */
    private Boolean VIsVip;

}
