package com.k365.channel_service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.k365.channel_service.ChannelService;
import com.k365.user_service.UserService;
import com.k365.video_base.mapper.ChannelMapper;
import com.k365.video_base.model.dto.ChannelDTO;
import com.k365.video_base.model.po.Channel;
import com.k365.video_base.model.po.User;
import com.k365.video_base.model.vo.BaseListVO;
import com.k365.video_base.model.vo.ChannelVO;
import com.k365.video_common.exception.ResponsiveException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    @Autowired
    private UserService userService;

    @Override
   public boolean add(ChannelDTO channelDTO){

       Channel channel =this.getOne(new QueryWrapper<Channel>()
               .eq("channel_code",channelDTO.getChannelCode()));

        if (channel != null)
                throw new ResponsiveException("同名渠道已存在！");

        Channel channel1 = Channel.builder().name(channelDTO.getName()).
                createDate(new Date()).link(channelDTO.getLink()).
                channelCode(channelDTO.getChannelCode()).build();

            return this.save(channel1);
        }



    @Override
    public BaseListVO<Channel> find(ChannelDTO channelDTO) {

        IPage<Channel> page=this.page(new Page<Channel>().setSize(channelDTO.getPageSize())
                        .setCurrent(channelDTO.getPage()),
                new QueryWrapper<Channel>().orderByDesc("create_date"));

        if(channelDTO.getSearchValue() != null && !channelDTO.getSearchValue().equals("")){
            IPage<Channel> page1=this.page(new Page<Channel>().setSize(channelDTO.getPageSize())
                            .setCurrent(channelDTO.getPage()),
                    new QueryWrapper<Channel>().like("name",channelDTO.getSearchValue()).or()
                            .like("channel_code",channelDTO.getSearchValue()).orderByDesc("create_date"));

            return  new BaseListVO<Channel>().setTotal(page1.getTotal()).setList(page1.getRecords());
        }

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
    public BaseListVO<Channel> searchPage(ChannelDTO channelDTO) {

        IPage<Channel> page=this.page(new Page<Channel>().setSize(channelDTO.getPageSize())
                        .setCurrent(channelDTO.getPage()),
                new QueryWrapper<Channel>().like("name",channelDTO.getSearchValue()).or()
                        .like("channel_code",channelDTO.getSearchValue()).orderByDesc("create_date"));

        return  new BaseListVO<Channel>().setTotal(page.getTotal()).setList(page.getRecords());

    }

    @Override
    public Integer count(ChannelDTO channelDTO) {
        return userService.count(new QueryWrapper<User>().
                eq("register_channel",channelDTO.getChannelCode()));
    }

    @Override
    public List<ChannelVO> findAll() {
        List<Channel> list =this.list(new QueryWrapper<Channel>().orderByDesc("create_date"));

        List<ChannelVO> voList = new ArrayList<>();
        list.forEach(channel ->
                voList.add(ChannelVO.builder().id(channel.getId())
                        .name(channel.getName()).channelCode(channel.getChannelCode())
                        .build())
        );

        return voList;

    }
}
