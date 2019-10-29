package com.k365.video_service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.k365.video_base.model.dto.VideoCommentDTO;
import com.k365.video_base.model.po.VideoComment;
import com.k365.video_base.model.vo.BaseListVO;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author Gavin
 * @since 2019-07-17
 */
public interface VideoCommentService extends IService<VideoComment> {

    /**
     * 视频评论查询
     *
     * @param videoCommentDTO
     * @return
     */
    BaseListVO<VideoComment> findAll(VideoCommentDTO videoCommentDTO);

    /**
     * 根据电影id和用户id筛选评论
     */
    BaseListVO<VideoComment> findByVIdOrUId(VideoCommentDTO videoCommentDTO);

    /**
     * 根据电影id和用户id筛选评论
     */
    BaseListVO<VideoComment> search(VideoCommentDTO videoCommentDTO);

    /**
     * 新增视频评论
     *
     * @param
     * @return
     */
    void addComment(VideoComment videoComment);

    /**
     * 通过id删除视频评论
     *
     * @param id
     */
    void removeByVideoId(String id);

    /**
     * 根据视频id查询视频评论
     */
    List<VideoComment> getPageByVId(VideoCommentDTO videoCommentDTO);

}
