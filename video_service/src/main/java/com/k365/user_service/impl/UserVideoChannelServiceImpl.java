package com.k365.user_service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.k365.video_base.common.UserContext;
import com.k365.user_service.UserVideoChannelService;
import com.k365.video_base.common.AppVideoTopLabelEnum;
import com.k365.video_base.mapper.UserVideoChannelMapper;
import com.k365.video_base.model.po.User;
import com.k365.video_base.model.po.UserVideoChannel;
import com.k365.video_base.model.vo.VideoChannelVO;
import com.k365.video_common.constant.StatusEnum;
import org.springframework.stereotype.Service;
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
public class UserVideoChannelServiceImpl
        extends ServiceImpl<UserVideoChannelMapper, UserVideoChannel> implements UserVideoChannelService {

    @Override
    public List<VideoChannelVO> getUserVideoChannel() {
        List<UserVideoChannel> userVideoChannels = this.getAll();
        List<VideoChannelVO> result = new ArrayList<>();

        //获取固定的频道信息
        for(AppVideoTopLabelEnum avrl : AppVideoTopLabelEnum.values()){
            result.add(VideoChannelVO.builder().name(avrl.getName()).sort(avrl.getSort()).build());
        }
        //获取动态的用户频道信息
        if(!ListUtils.isEmpty(userVideoChannels)){
            userVideoChannels.forEach(uvc -> result.add(VideoChannelVO.builder().name(uvc.getChannelName()).sort(uvc.getSort()).build()));
        }

        return result;
    }



    @Override
    public void removeByChannelId(Integer id) {
        this.remove(new QueryWrapper<UserVideoChannel>().eq("channel_id",id));
    }

    @Override
    public void removeByUId(String uId) {

        this.remove(new UpdateWrapper<UserVideoChannel>().eq("user_id",uId));
    }

    @Override
    public List<UserVideoChannel> getAll() {
        User currentUser = UserContext.getCurrentUser();
        List<UserVideoChannel> list = this.list(new QueryWrapper<UserVideoChannel>()
                .eq("user_id", currentUser.getId()).eq("status", StatusEnum.ENABLE.key()).orderByAsc("sort"));

        return list;
    }
}
