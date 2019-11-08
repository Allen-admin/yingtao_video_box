package com.k365.video_base.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.k365.video_base.model.dto.ChannelDTO;
import com.k365.video_base.model.po.Channel;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;


/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Allen
 * @since 2019-11-05
 */
@Mapper
public interface ChannelMapper extends BaseMapper<Channel> {


    /**
     * 全文检索 渠道名称和id
     */
    List<Channel> searchMatchChannelPage(ChannelDTO channelDTO);

    /**
     * 查询渠道和数量
     */
  /*  List<ChannelRO> findPage(ChannelDTO channelDTO);*/

}
