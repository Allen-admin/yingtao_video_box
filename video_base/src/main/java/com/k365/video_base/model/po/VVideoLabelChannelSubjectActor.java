package com.k365.video_base.model.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * VIEW
 * </p>
 *
 * @author Gavin
 * @since 2019-09-03
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class VVideoLabelChannelSubjectActor implements Serializable {

    private static final long serialVersionUID = 1L;

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
     * 视频状态
     */
    private Integer vStatus;

    /**
     * 视频标题
     */
    private String vTitle;

    /**
     * 视频累计播放次数
     */
    private Long vPlaySum;

    /**
     * 视频播放时长（单位：秒）
     */
    private Integer vTimeLen;

    /**
     * 创建时间
     */
    private Date vCreateDate;

    /**
     * 视频观看路径
     */
    private String vPlayUrl;

    /**
     * 视频下载地址
     */
    private String vSaveUrl;

    /**
     * id
     */
    private Integer vcId;

    /**
     * 频道名称
     */
    private String vcName;

    /**
     * 排序
     */
    private Integer vcSort;

    /**
     * 标签id
     */
    private Integer vlId;

    /**
     * 标签名称
     */
    private String vlName;

    /**
     * 状态
     */
    private Integer vlStatus;

    /**
     * 标签排序
     */
    private Integer vlSort;

    /**
     * id
     */
    private Integer vsId;

    /**
     * 名称
     */
    private String vsName;

    /**
     * 封面图片
     */
    private String vsCover;

    /**
     * 状态
     */
    private Integer vsStatus;

    /**
     * 女优id
     */
    private Integer aId;

    /**
     * 女优姓名
     */
    private String aName;

    /**
     * 女优头像
     */
    private String aActorIcon;

    /**
     * 名字首字母
     */
    private String aAcronym;


}
