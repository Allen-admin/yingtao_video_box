package com.k365.video_base.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author Gavin
 * @date 2019/8/29 10:44
 * @description：
 */
@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class VideoViewingInfoVO {

    private String id;
    /**
     * 手机号
     */
    private String phone;

    /**
     * 视频下载次数
     */
    private Integer saveCount;

    /**
     * 推荐用户数
     */
    private Integer recommendCount;

    /**
     * 已用下载次数
     */
    private Integer usedSaveCount;

    /**
     * 已用观影次数
     */
    private Integer usedViewingCount;

    /**
     * 总观影次数
     */
    private Integer viewingCount;

    /**
     * 下一个等级推广人数
     */
    private Integer nextLevelCount;

    /**
     *VIP到期时间
     */
    private Date vipEndTime;

    /**
     *VIP类型
     */
    private Integer vipType;
}
