package com.k365.user_service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.k365.video_base.model.po.UserVideoChannel;
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
public interface UserVideoChannelService extends IService<UserVideoChannel> {

    /**
     * 获取APP头部视频频道
     */
    List<VideoChannelVO> getUserVideoChannel();

    /**
     * 根据频道id删除
     */
    void removeByChannelId(Integer id);

    /**
     * 根据用户id删除
     */
    void removeByUId(String uId);

    /**
     * 获取用户所有频道
     */
    List<UserVideoChannel> getAll();

}
