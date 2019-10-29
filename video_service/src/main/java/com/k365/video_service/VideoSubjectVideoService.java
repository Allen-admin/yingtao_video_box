package com.k365.video_service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.k365.video_base.model.po.VideoSubjectVideo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Gavin
 * @since 2019-08-08
 */
public interface VideoSubjectVideoService extends IService<VideoSubjectVideo> {

    /**
     * 根据视频id删除
     */
    void removeByVideoId(String id);

    /**
     * 根据视频主题删除
     */
    void removeByVideoSubjectId(Integer videoSubjectId);

    /**
     * 根据视频id修改
     */
    void updateByVideoId(String videoId,Integer[] videoSubjectId);


}
