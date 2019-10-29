package com.k365.video_base.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * <p>
 * </p>
 *
 * @author Gavin
 * @since 2019-08-08
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class VideoSubjectVideo {


    @TableId(value = "id", type = IdType.UUID)
    private String id;

    /**
     * 视频id
     */
    private String videoId;

    /**
     * 视频主题id
     */
    private Integer videoSubjectId;


}
