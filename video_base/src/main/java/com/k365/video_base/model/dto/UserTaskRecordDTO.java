package com.k365.video_base.model.dto;

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
public class UserTaskRecordDTO extends SplitPageDTO implements BaseDTO {

    private String id;

    /**
     * 任务id
     */
    private String taskId;

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
