package com.k365.video_service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.k365.video_base.mapper.VideoChannelVideoMapper;
import com.k365.video_base.model.po.VideoChannelVideo;
import com.k365.video_service.VideoChannelVideoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.ListUtils;
import org.thymeleaf.util.SetUtils;

import java.util.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Gavin
 * @since 2019-08-12
 */
@Service
public class VideoChannelVideoServiceImpl extends
        ServiceImpl<VideoChannelVideoMapper, VideoChannelVideo> implements VideoChannelVideoService {

    @Override
    public void removeByVideoChannelId(Integer vcid) {
        this.remove(new QueryWrapper<VideoChannelVideo>().eq("video_channel_id", vcid));
    }

    @Override
    public void removeByVideoId(String videoId) {
        this.remove(new UpdateWrapper<VideoChannelVideo>().eq("video_id", videoId));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT)
    public void updateByVideoId(String videoId, Integer[] videoChannelId) {
        List<Integer> vcIds = JSONArray.parseArray(JSON.toJSONString(this.listObjs(
                new QueryWrapper<VideoChannelVideo>().eq("video_id", videoId).select("video_channel_id"))), Integer.class);

        Set<Integer> newVcIds = new HashSet<>(Arrays.asList(videoChannelId));
        List<VideoChannelVideo> newVcv = new ArrayList<>();
        if (!ListUtils.isEmpty(vcIds)) {
            Set<Integer> oldVcIds = new HashSet<>(vcIds);
            Set<Integer> intersection = new HashSet<>(oldVcIds);

            //获取 旧频道与新频道的id 交集（intersection）
            intersection.retainAll(newVcIds);

            //获取 旧频道id 与 交集 的差集 【删除】
            oldVcIds.removeAll(intersection);
            if(!SetUtils.isEmpty(oldVcIds)) {
                this.remove(new UpdateWrapper<VideoChannelVideo>().in("video_channel_id", oldVcIds));
            }

            //获取 新频道id 与 交集 的差集 【新增】
            newVcIds.removeAll(intersection);

        }
        newVcIds.forEach(newVcId ->
                newVcv.add(VideoChannelVideo.builder().videoId(videoId).videoChannelId(newVcId).build())
        );

        this.saveBatch(newVcv);
    }

}
