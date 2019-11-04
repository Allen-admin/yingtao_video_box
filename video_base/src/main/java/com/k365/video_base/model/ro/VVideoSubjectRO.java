package com.k365.video_base.model.ro;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author Gavin
 * @date 2019/8/24 16:15
 * @description：
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class VVideoSubjectRO {

    /**
     * id
     */
    private Object vsId;

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
     * 影片数量
     */
    private Integer videoTotal;

    /**
     * 是否为广告
     */
    private Boolean isAd = false;

    /**
     * 广告跳转路径
     */
    private String adUrl;

    /**
     * 图标
     */
    private String vsIcon;


}
