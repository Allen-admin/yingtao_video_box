package com.k365.video_base.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author Gavin
 * @since 2019-08-13
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VideoLabelType{

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 标签类型名称
     */
    private String name;

    /**
     * 排序
     */
    private Integer sort;

}
