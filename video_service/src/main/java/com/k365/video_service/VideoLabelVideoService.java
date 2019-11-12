package com.k365.video_service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.k365.video_base.model.po.Video;
import com.k365.video_base.model.po.VideoLabelVideo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Gavin
 * @since 2019-08-06
 */
public interface VideoLabelVideoService extends IService<VideoLabelVideo> {

    /**
     * 根据视频标签id删除
     */
    void removeByVideoLabelId(Integer id);

    /**
     * 根据视频id删除
     */
    void removeByVideoId(String id);

    /**
     * 根据视频id修改
     */
    void updateByVideoId(String videoId,Integer[] videoLabelId);

    /**
     * 根据视频ids批量查询标签ids
     */
    List<Integer> getVLIdsByVIds(List<String> videoIds);

    /**
     * 根据视频标签批量查询视频
     */
    List<Video> getVideoVoByLabels(List<Integer> labels);

    /**
     * 根据视频id批量查询
     */
    List<VideoLabelVideo> getVideoLableVideosByVideoId(String videoId);

    /**
     * 根据videlLableId批量查询
     */
    List<VideoLabelVideo> getVideoLableVideosByLableId(String labelId);

}
