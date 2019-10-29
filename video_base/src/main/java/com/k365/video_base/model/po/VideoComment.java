package com.k365.video_base.model.po;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.*;
import lombok.experimental.Accessors;

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
public class VideoComment {

    @TableId(value = "id", type = IdType.UUID)
    private String id;

    /**
     * 评论人id，对应user表id
     */
    private String userId;

    /**
     * 评论人头像
     */
    private String userIcon;

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
     * 总条数
     */
    @JSONField(serialize = false)
    @TableField(exist = false)
    private Long total;


}
