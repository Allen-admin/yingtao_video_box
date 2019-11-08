package com.k365.channel_service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.k365.video_base.model.dto.ChannelDTO;
import com.k365.video_base.model.po.Channel;
import com.k365.video_base.model.po.User;
import com.k365.video_base.model.vo.BaseListVO;
import com.k365.video_base.model.vo.ChannelVO;

import java.util.List;

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
     * 通过id和渠道名称检索
     * @param channelDTO
     * @return
     */
    BaseListVO<Channel> searchPage(ChannelDTO channelDTO);

    /**
     * 查询user的渠道总数
     * @param channelDTO
     * @return
     */
    Integer count(ChannelDTO channelDTO);


    /**
     *查询用户信息
     * @param channelDTO
     * @return
     */
    List<User> findAll(ChannelDTO channelDTO);

    /**
     *通过时间段检索
     * @param channelDTO
     * @return
     */
    BaseListVO<Channel> searchDate(ChannelDTO channelDTO);

    /**
     * 查询渠道和数量
     * @param channelDTO
     * @return
     */
    /*List<Channel> findPage(ChannelDTO channelDTO);*/

    /**
     * 查询所有的渠道
     * @return
     */
    List<ChannelVO> findAll();

}
