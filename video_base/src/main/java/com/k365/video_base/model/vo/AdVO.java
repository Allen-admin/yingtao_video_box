package com.k365.video_base.model.vo;

import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author Gavin
 * @date 2019/7/30 19:51
 * @description：
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdVO implements Serializable{

    private static final long serialVersionUID = -8174452840090873929L;

    private String id;

    /**
     * 广告标题
     */
    private String title;

    /**
     * 广告封面图片
     */
    private String cover;

    /**
     * 广告排序
     */
    private Integer sort;

    /**
     * 广告详情url
     */
    private String detailsUrl;

}
