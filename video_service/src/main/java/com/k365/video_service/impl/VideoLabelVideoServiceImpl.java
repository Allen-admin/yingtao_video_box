package com.k365.video_service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.k365.video_base.model.po.VideoChannelVideo;
import com.k365.video_base.model.po.VideoLabelVideo;
import com.k365.video_base.mapper.VideoLabelVideoMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.k365.video_base.model.po.VideoSubjectVideo;
import com.k365.video_service.VideoLabelVideoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.ListUtils;
import org.thymeleaf.util.SetUtils;

import java.util.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Gavin
 * @since 2019-08-06
 */
@Service
public class VideoLabelVideoServiceImpl extends ServiceImpl<VideoLabelVideoMapper, VideoLabelVideo>
        implements VideoLabelVideoService {


    @Override
    public void removeByVideoLabelId(Integer id) {
        this.remove(new UpdateWrapper<VideoLabelVideo>().eq("video_label_id",id));
    }

    @Override
    public void removeByVideoId(String id) {
        this.remove(new UpdateWrapper<VideoLabelVideo>().eq("video_id",id));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT)
    public void updateByVideoId(String videoId, Integer[] videoLabelId) {
        List<Integer> vlIds = JSONArray.parseArray(JSON.toJSONString(this.listObjs(
                new QueryWrapper<VideoLabelVideo>().eq("video_id", videoId).select("video_label_id"))), Integer.class);

        Set<Integer> newVlIds = new HashSet<>(Arrays.asList(videoLabelId));
        List<VideoLabelVideo> newVcv = new ArrayList<>();
        if (!ListUtils.isEmpty(vlIds)) {
            Set<Integer> oldVlIds = new HashSet<>(vlIds);
            Set<Integer> intersection = new HashSet<>(oldVlIds);

            //获取 旧标签与新标签的id 交集（intersection）
            intersection.retainAll(newVlIds);

            //获取 旧标签id 与 交集 的差集 【删除】
            oldVlIds.removeAll(intersection);
            if(!SetUtils.isEmpty(oldVlIds)) {
                this.remove(new UpdateWrapper<VideoLabelVideo>().in("video_label_id", oldVlIds));
            }

            //获取 新标签id 与 交集 的差集 【新增】
            newVlIds.removeAll(intersection);

        }

        newVlIds.forEach(newVcId ->
                newVcv.add(VideoLabelVideo.builder().videoId(videoId).videoLabelId(newVcId).build())
        );

        this.saveBatch(newVcv);
    }

    @Override
    public List<Integer> getVLIdsByVIds(List<String> videoIds) {
        return JSONArray.parseArray(JSON.toJSONString(this.listObjs(new QueryWrapper<VideoLabelVideo>()
                .in("video_id",videoIds).select("video_label_id"))),Integer.class);

    }
}
