package com.k365.video_base.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

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
public class VideoCommentDTO extends SplitPageDTO implements BaseDTO {

    private String id;

    /**
     * 评论人id，对应user表id
     */
    private String userId;

    /**
     * 评论时间
     */
    private Date time;

    /**
     * 评论视频id
     */
    private String videoId;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 评论状态
     */
    private Integer status;

    /**
     * 用户昵称
     */
    private String userNickname;

    /**
     * 视频标题
     */
    private String videoTitle;

    /**
     *
     */
    private String searchValue;


}
