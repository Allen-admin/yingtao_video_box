package com.k365.video_base.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author Gavin
 * @since 2019-08-29
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class UserVideoCollection {

    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 用户Id
     */
    private String userId;

    /**
     * 视频id
     */
    private String videoId;

    /**
     * 创建时间
     */
    private Date createDate;


}
