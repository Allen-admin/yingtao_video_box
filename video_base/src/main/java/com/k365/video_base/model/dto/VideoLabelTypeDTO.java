package com.k365.video_base.model.dto;

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
 * @since 2019-07-17
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VideoLabelTypeDTO extends SplitPageDTO implements BaseDTO {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 视频标签类型名称
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

}
