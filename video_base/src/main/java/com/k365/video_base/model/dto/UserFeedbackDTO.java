package com.k365.video_base.model.dto;

import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 用户反馈意见表
 * </p>
 *
 * @author Gavin
 * @since 2019-07-17
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserFeedbackDTO extends SplitPageDTO implements BaseDTO {


    private String id;

    /**
     * 用户ID
     */
    private String feedbackUserId;

    /**
     * 反馈内容
     */
    private String feedbackContent;

    /**
     * 联系方式
     */
    private String contact;

    /**
     * 反馈时间
     */
    private Date feedbackTime;

    /**
     * 状态
     */
    private Integer status;


}
