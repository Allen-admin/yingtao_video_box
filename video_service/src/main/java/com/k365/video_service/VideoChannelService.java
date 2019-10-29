package com.k365.video_service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.k365.video_base.model.po.VideoChannel;
import com.k365.video_base.model.vo.VideoChannelVO;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author Gavin
 * @since 2019-08-12
 */
public interface VideoChannelService extends IService<VideoChannel> {

    /**
     * 获取APP头部所有频道
     */
    List<VideoChannel> getAllChannels();

    /**
     * 获取所有视频频道
     */
    List<VideoChannel> gatList();

    /**
     * 添加频道
     */
    void add(VideoChannel videoChannel);

    /**
     * 修改频道
     */
    void update(VideoChannel videoChannel);

    /**
     * 根据id删除频道
     */
    void remove(Integer id);

}
