package com.k365.video_base.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author Gavin
 * @date 2019/8/30 18:06
 * @description：
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VActorVideoDTO extends SplitPageDTO implements BaseDTO{

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
