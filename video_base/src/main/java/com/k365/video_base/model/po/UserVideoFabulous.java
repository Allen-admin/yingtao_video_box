package com.k365.video_base.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.*;
import lombok.experimental.Accessors;

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
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class UserVideoFabulous {

    @TableId(value = "id",type = IdType.AUTO)
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
     * 点赞时间
     */
    private Date createDate;


}
