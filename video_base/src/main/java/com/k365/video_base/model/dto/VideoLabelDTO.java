package com.k365.video_base.model.dto;

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
 * @since 2019-07-17
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VideoLabelDTO extends SplitPageDTO implements BaseDTO {


    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 视频标签类型id
     */
    private Integer videoLabelTypeId;

    /**
     * 标签名称
     */
    private String name;

    /**
     * 标签排序
     */
    private Integer sort;

}
