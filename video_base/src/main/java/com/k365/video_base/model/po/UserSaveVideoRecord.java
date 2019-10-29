package com.k365.video_base.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * <p>
 * </p>
 *
 * @author Gavin
 * @since 2019-07-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class UserSaveVideoRecord{

    @TableId(type = IdType.UUID)
    private String id;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 视频id
     */
    private String videoId;

    /**
     * 视频下载记录时间
     */
    private Date recordTime;

    /**
     * 视频大小
     */
    private Double videoSize;

    /**
     * 已下载大小
     */
    private Double saveVideoSize;


    /**
     * 视频下载状态
     */
    private Integer saveStatus;


}
