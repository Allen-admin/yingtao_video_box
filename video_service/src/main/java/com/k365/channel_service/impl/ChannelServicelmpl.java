package com.k365.channel_service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.k365.channel_service.ChannelService;
import com.k365.video_base.mapper.ChannelMapper;
import com.k365.video_base.model.dto.ChannelDTO;
import com.k365.video_base.model.po.Channel;
import com.k365.video_base.model.po.SysLog;
import com.k365.video_base.model.so.SysLogSO;
import com.k365.video_base.model.vo.BaseListVO;
import com.k365.video_common.exception.ResponsiveException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * 用户渠道 服务实现类
 * </p>
 *
 * @author Allen
 * @since 2019-11--05
 */
@Service
public class ChannelServicelmpl extends ServiceImpl<ChannelMapper, Channel> implements ChannelService {


    @Override
   public boolean add(ChannelDTO channelDTO){
        Channel channel = this.getOne(new QueryWrapper<Channel>().eq("name", channelDTO.getName()));
            if (channel != null)
                throw new ResponsiveException("同名渠道已存在！");

        Channel channel1 = Channel.builder().name(channelDTO.getName()).
                CreateDate(new Date()).link(channelDTO.getLink()).build();

            return this.save(channel1);
        }


    @Override
    public BaseListVO<Channel> find(ChannelDTO channelDTO) {

        IPage<Channel> page=this.page(new Page<Channel>().setSize(channelDTO.getPageSize()).setCurrent(channelDTO.getPage()),
                new QueryWrapper<Channel>().orderByAsc("create_date"));

        return  new BaseListVO<Channel>().setTotal(page.getTotal()).setList(page.getRecords());

    }


    @Override
    public void update(ChannelDTO channelDTO) {

        Channel channel = this.getById(channelDTO.getId());
        if (channel == null)
            throw new RuntimeException("渠道不存在或已被删除");

        Channel channel1 = Channel.builder().id(channelDTO.getId()).name(channelDTO.getName()).link(channelDTO.getLink())
                .build();
        this.updateById(channel1);
    }


    @Override
    public BaseListVO<Channel> search(ChannelDTO channelDTO) {
        QueryWrapper<Channel> queryWrapper = new QueryWrapper<>();
        if (channelDTO.getName() != null) {
            //名称查询 成功 失败
            queryWrapper.eq("name", channelDTO.getName());
        }

        if (channelDTO.getId() != null) {
            //编号查询 成功 失败
            queryWrapper.eq("id", channelDTO.getId());
        }

        IPage<Channel> page = this.page(new Page<Channel>().setCurrent(channelDTO.getPage()).setSize(channelDTO.getPageSize()),
                queryWrapper.orderByDesc("create_date"));

        return new BaseListVO<Channel>().setTotal(page.getTotal()).setList(page.getRecords());

    }







}
