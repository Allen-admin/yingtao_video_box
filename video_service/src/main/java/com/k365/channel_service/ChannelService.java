package com.k365.channel_service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.k365.video_base.model.dto.ChannelDTO;
import com.k365.video_base.model.po.Channel;
import com.k365.video_base.model.vo.BaseListVO;

/**
 * <p>
 * 用户渠道
 * </p>
 *
 * @author Allen
 * @since 2019-11-05
 */
public interface ChannelService extends IService<Channel> {

    /**
     * 新增渠道
     */
    boolean add(ChannelDTO channelDTO);

    /**
     * 分页查询用户渠道
     * @param channelDTO
     * @return
     */
    BaseListVO<Channel> find(ChannelDTO channelDTO);

    /**
     * 修改用户渠道
     * @param channelDTO
     */
    void update(ChannelDTO channelDTO);

    /**
     * 用id和用户名模糊查询
     * @param channelDTO
     * @return
     */
    BaseListVO<Channel> search(ChannelDTO channelDTO);

}
