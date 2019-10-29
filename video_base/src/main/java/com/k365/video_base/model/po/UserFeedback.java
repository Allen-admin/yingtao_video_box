package com.k365.video_base.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
public class UserFeedback {

    @TableId(value = "id", type = IdType.UUID)
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
