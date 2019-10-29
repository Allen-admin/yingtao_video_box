package com.k365.video_service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.k365.video_base.model.po.VideoChannelVideo;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author Gavin
 * @since 2019-08-12
 */
public interface VideoChannelVideoService extends IService<VideoChannelVideo> {

    /**
     * 根据频道id删除
     */
    void removeByVideoChannelId(Integer vcid);

    /**
     * 根据视频id删除
     */
    void removeByVideoId(String videoId);

    /**
     * 根据视频id修改
     */
    void updateByVideoId(String videoId,Integer[] videoChannelId);

}
