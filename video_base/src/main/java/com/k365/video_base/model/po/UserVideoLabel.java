package com.k365.video_base.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
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
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class UserVideoLabel {

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
