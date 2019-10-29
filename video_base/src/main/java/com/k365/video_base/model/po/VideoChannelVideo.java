package com.k365.video_base.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * <p>
 * </p>
 *
 * @author Gavin
 * @since 2019-08-12
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VideoChannelVideo {

    @TableId(value = "id", type = IdType.UUID)
    private String id;

    /**
     * 视频id
     */
    private String videoId;

    /**
     * 视频频道ID
     */
    private Integer videoChannelId;


}
