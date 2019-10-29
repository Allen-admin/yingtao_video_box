package com.k365.video_base.model.so;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author Gavin
 * @date 2019/8/31 16:50
 * @description：
 */
@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class VideoCommentSO extends BaseSO {

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
     * 搜索关键字
     */
    private String searchValue;
}
