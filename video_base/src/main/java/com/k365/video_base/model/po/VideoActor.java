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
 * @since 2019-08-06
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VideoActor {

    @TableId(type = IdType.UUID)
    private String id;

    /**
     * 演员id
     */
    private Integer actorId;

    /**
     * 视频id
     */
    private String videoId;


}
