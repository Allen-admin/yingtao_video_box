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
public class UserVideoLabrlDTO extends SplitPageDTO implements BaseDTO {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 视频标签id
     */
    private Integer videoLabelId;

    /**
     * 标签名称
     */
    private String videoLabelName;

    /**
     * 用户视频标签排序
     */
    private Integer sort;


}
