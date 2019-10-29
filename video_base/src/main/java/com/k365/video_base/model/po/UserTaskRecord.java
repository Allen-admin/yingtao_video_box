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
 * @since 2019-07-17
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class UserTaskRecord {

    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 任务id
     */
    private Integer taskId;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 任务状态
     */
    private Integer status;

    /**
     * 完成时间
     */
    private Date completeTime;

}
