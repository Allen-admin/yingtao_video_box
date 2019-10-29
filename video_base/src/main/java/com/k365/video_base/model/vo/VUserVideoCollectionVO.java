package com.k365.video_base.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author Gavin
 * @date 2019/8/29 16:33
 * @description：
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VUserVideoCollectionVO {

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
     * 是否为vip
     */
    private Boolean VIsVip;

}
