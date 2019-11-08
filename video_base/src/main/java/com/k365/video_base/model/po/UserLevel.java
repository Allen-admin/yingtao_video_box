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
 * @since 2019-07-17
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class UserLevel {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 等级标识
     */
    private Integer level;

    /**
     * 等级名称
     */
    private String name;

    /**
     * 等级下载视频次数
     */
    private Integer saveCount;

    /**
     * 等级观影次数
     */
    private Integer viewingCount;

    /**
     * 推荐人数
     */
    private Integer recommendCount;

    /**
     * 状态
     */
    private Integer status;

}
