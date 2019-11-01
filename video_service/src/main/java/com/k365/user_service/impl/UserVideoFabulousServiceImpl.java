package com.k365.user_service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.k365.user_service.UserVideoFabulousService;
import com.k365.video_base.common.UserContext;
import com.k365.video_base.mapper.UserVideoFabulousMapper;
import com.k365.video_base.model.po.User;
import com.k365.video_base.model.po.UserVideoFabulous;
import com.k365.video_common.exception.ResponsiveException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.ListUtils;

import java.util.Date;
import java.util.List;


/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Gavin
 * @since 2019-08-29
 */
@Service
public class UserVideoFabulousServiceImpl extends ServiceImpl<UserVideoFabulousMapper, UserVideoFabulous> implements UserVideoFabulousService {

    @Override
    public void addFabulous(String vid) {
        User currentUser = UserContext.getCurrentUser();

        if (hasFabulous(vid)) {
            throw new ResponsiveException("视频已点赞");

        }
            this.save(UserVideoFabulous.builder().videoId(vid).userId(currentUser.getId()).
                    createDate(new Date()).build());
    }

    @Override
    public boolean hasFabulous(String vid) {
        User currentUser = UserContext.getCurrentUser();

        List<UserVideoFabulous> list = this.list(new QueryWrapper<UserVideoFabulous>().
                eq("video_id", vid).eq("user_id", currentUser.getId()));

        if (ListUtils.isEmpty(list)) {
            return false;
        }

        return true;
    }

    @Override
    public void removeByVidOrUId(UserVideoFabulous userVideoFabulous) {
        UpdateWrapper<UserVideoFabulous> updateWrapper = new UpdateWrapper<>();

        if (userVideoFabulous.getVideoId() != null) {
            updateWrapper.eq("video_id", userVideoFabulous.getVideoId());
        }

        if (StringUtils.isNotBlank(userVideoFabulous.getUserId())) {
            updateWrapper.eq("user_id", userVideoFabulous.getUserId());
        }

        this.remove(updateWrapper);
    }


    @Override
    public Integer countFabulous(String vid) {
        return this.count(new QueryWrapper<UserVideoFabulous>().eq("video_id",vid));
    }

}

