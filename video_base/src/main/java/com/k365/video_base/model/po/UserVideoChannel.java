package com.k365.video_base.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * <p>
 * </p>
 *
 * @author Gavin
 * @since 2019-08-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class UserVideoChannel {

    @TableId(value = "id", type = IdType.UUID)
    private String id;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 视频频道id
     */
    private Integer channelId;

    /**
     * 视频频道名称
     */
    private String channelName;

    /**
     * 视频频道code
     */
    private String channelCode;

    /**
     * 用户视频频道排序
     */
    private Integer sort;

    /**
     * channel状态
     */
    private Short status;


}
