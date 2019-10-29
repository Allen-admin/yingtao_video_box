package com.k365.video_service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.k365.user_service.UserVideoChannelService;
import com.k365.video_base.common.AppVideoTopLabelEnum;
import com.k365.video_base.mapper.VideoChannelMapper;
import com.k365.video_base.model.po.VideoChannel;
import com.k365.video_common.exception.ResponsiveException;
import com.k365.video_service.VideoChannelService;
import com.k365.video_service.VideoChannelVideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.ListUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Gavin
 * @since 2019-08-12
 */
@Service
public class VideoChannelServiceImpl extends ServiceImpl<VideoChannelMapper, VideoChannel> implements VideoChannelService {

    @Autowired
    private UserVideoChannelService userVideoChannelService;

    @Autowired
    private VideoChannelVideoService videoChannelVideoService;

    @Override
    public List<VideoChannel> getAllChannels() {
        List<VideoChannel> result = new ArrayList<>();

        //获取固定的频道信息
        int i = Integer.MIN_VALUE;
        for (AppVideoTopLabelEnum avrl : AppVideoTopLabelEnum.values()) {
            result.add(VideoChannel.builder().id(++i).name(avrl.getName()).sort(avrl.getSort()).build());
        }

        List<VideoChannel> videoChannels = this.list(new QueryWrapper<VideoChannel>().orderByAsc("sort"));
        if (!ListUtils.isEmpty(videoChannels)) {
            result.addAll(videoChannels);
        }

        return result;
    }


    @Override
    public List<VideoChannel> gatList() {
        return this.list(new QueryWrapper<VideoChannel>().orderByAsc("sort"));

    }

    @Override
    public void add(VideoChannel videoChannel) {
        VideoChannel videoChannel2 = this.getOne(new QueryWrapper<VideoChannel>().eq("name", videoChannel.getName()));
        if (videoChannel2 != null)
            throw new ResponsiveException("同名视频频道已存在！");

        this.save(videoChannel);
    }

    @Override
    public void update(VideoChannel videoChannel) {
        VideoChannel videoChannel2 = this.getById(videoChannel.getId());
        if (videoChannel2 == null)
            throw new ResponsiveException("视频频道不存在或已被删除");

        this.updateById(videoChannel);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public void remove(Integer id) {
        boolean removeSuccess = this.removeById(id);
        if (removeSuccess) {
            videoChannelVideoService.removeByVideoChannelId(id);
            userVideoChannelService.removeByChannelId(id);
        }
    }
}
