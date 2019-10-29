package com.k365.video_service;

import com.k365.video_base.model.po.VideoActor;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Gavin
 * @since 2019-08-06
 */
public interface VideoActorService extends IService<VideoActor> {

    /**
     * 根据女优id删除
     */
    void removeByActorId(Integer id);


    /**
     * 根据视频id删除
     */
    void removeByVideoId(String id);

    /**
     * 根据视频id修改
     */
    void updateByVideoId(String videoId,Integer[] actorId);

}
