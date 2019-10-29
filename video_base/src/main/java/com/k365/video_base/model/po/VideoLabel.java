package com.k365.video_base.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author Gavin
 * @since 2019-07-17
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VideoLabel {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 标签名称
     */
    private String name;

    /**
     * 标签排序
     */
    private Integer sort;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 视频标签类型id
     */
    private Integer typeId;

    /**
     * 视频标签类型名称
     */
    private String typeName;

    /**
     * 视频标签类型状态
     */
    private Integer typeSort;

}
