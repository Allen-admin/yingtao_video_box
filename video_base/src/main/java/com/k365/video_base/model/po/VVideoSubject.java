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
 * @since 2019-08-24
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VVideoSubject {

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
     * 视频排序
     */
    private Integer vSort;

    /**
     * 视频状态
     */
    private Integer vStatus;

    /**
     * 视频标题
     */
    private String vTitle;

    /**
     * 视频时长
     */
    private Integer vTimeLen;

    /**
     * 视频累计播放次数
     */
    private Long vPlaySum;

    /**
     * 创建时间
     */
    private Date vCreateDate;

    /**
     * id
     */
    private Integer vsId;

    /**
     * 封面图片
     */
    private String vsCover;

    /**
     * 名称
     */
    private String vsName;

    /**
     * 状态
     */
    private Integer vsStatus;

    /**
     * 排序
     */
    private Integer vsSort;

    /**
     * 描述
     */
    private String vsContent;

    /**
     * 是否为VIP视频
     */
    private Boolean vIsVip;




}
