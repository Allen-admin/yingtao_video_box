package com.k365.video_base.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.k365.video_base.model.po.VideoComment;
import com.k365.video_base.model.so.VideoCommentSO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author Gavin
 * @since 2019-07-17
 */
@Mapper
public interface VideoCommentMapper extends BaseMapper<VideoComment> {

    /**
     * 根据视频标题，用户昵称或评论内容全文检索
     */
    List<VideoComment> searchVideoComment(VideoCommentSO videoCommentSO);


}
