package com.k365.channel_service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.k365.video_base.model.po.UserLevel;
import com.k365.video_base.model.po.VUserChannel;
import com.k365.video_base.model.so.VUserChannelSO;
import com.k365.video_base.model.vo.VUserChannelVO;

import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 用户渠道
 * </p>
 *
 * @author Allen
 * @since 2019-11-05
 */
public interface VUserChannelService extends IService<VUserChannel> {

    /**
     * 查询渠道信息,注册量和总数
     * @param
     * @return
     */
     HashMap<String,Object> findList(VUserChannelSO vUserChannelSO);

    /**
     * 檢索渠道信息
     * @param
     * @return
     */
    List<VUserChannelVO> search(VUserChannelSO vUserChannelSO);

    /**
     * 根据时间段查询
     * @param vUserChannelSO
     * @return
     */
    List<VUserChannelVO> searchPage(VUserChannelSO vUserChannelSO);

    /**
     * 获取所有用户等级
     * @return
     */
    List<UserLevel> findAll();


    /**
     * 根据编号，渠道名，时间搜索
     * @param vUserChannelSO
     * @return
     */
    List<VUserChannelVO> searchList(VUserChannelSO vUserChannelSO);


}
