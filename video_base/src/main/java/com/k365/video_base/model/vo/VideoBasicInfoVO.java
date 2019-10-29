package com.k365.video_base.model.vo;

import lombok.*;
import lombok.experimental.Accessors;
import org.aspectj.lang.annotation.Aspect;

import java.util.*;

/**
 * @author Gavin
 * @date 2019/8/20 14:48
 * @description：
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class VideoBasicInfoVO {

    private String id;

    /**
     * 视频封面图片
     */
    private String cover;

    /**
     * 视频标题
     */
    private String title;

    /**
     * 视频累计播放次数
     */
    private Long playSum;

    /**
     * 视频播放时长
     */
    private Integer timeLen;

    /**
     * 发行时间
     */
    private Date createDate;

    /**
     * 广告跳转路径
     */
    private String adUrl;

    /**
     * 是否为视频
     */
    private Boolean isAd = false;

    /**
     * 是否为VIP视频
     */
    private Boolean isVip;

}
