package com.k365.video_service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.k365.video_base.mapper.VideoSubjectVideoMapper;
import com.k365.video_base.model.po.VideoSubjectVideo;
import com.k365.video_service.VideoSubjectVideoService;
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
 * @since 2019-08-08
 */
@Service
public class VideoSubjectVideoServiceImpl extends
        ServiceImpl<VideoSubjectVideoMapper, VideoSubjectVideo> implements VideoSubjectVideoService {

    @Override
    public void removeByVideoId(String id) {
        this.remove(new UpdateWrapper<VideoSubjectVideo>().eq("video_id", id));
    }

    @Override
    public void removeByVideoSubjectId(Integer videoSubjectId) {
        this.remove(new UpdateWrapper<VideoSubjectVideo>().eq("video_subject_id", videoSubjectId));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public void updateByVideoId(String videoId, Integer[] videoSubjectId) {
        List<Integer> vsIds = JSONArray.parseArray(JSON.toJSONString(this.listObjs(
                new QueryWrapper<VideoSubjectVideo>().eq("video_id", videoId).select("video_subject_id"))), Integer.class);

        Set<Integer> newVsIds = new HashSet<>(Arrays.asList(videoSubjectId));
        List<VideoSubjectVideo> newVsv = new ArrayList<>();
        if (!ListUtils.isEmpty(vsIds)) {
            Set<Integer> oldVsIds = new HashSet<>(vsIds);
            Set<Integer> intersection = new HashSet<>(oldVsIds);

            //获取 旧专题与新专题的id 交集（intersection）
            intersection.retainAll(newVsIds);

            //获取 旧专题id 与 交集 的差集 【删除】
            oldVsIds.removeAll(intersection);
            if(!SetUtils.isEmpty(oldVsIds)) {
                this.remove(new UpdateWrapper<VideoSubjectVideo>().in("video_subject_id", oldVsIds).eq("video_id",videoId));
            }

            //获取 新专题id 与 交集 的差集 【新增】
            newVsIds.removeAll(intersection);

        }

        newVsIds.forEach(newVcId ->
                newVsv.add(VideoSubjectVideo.builder().videoId(videoId).videoSubjectId(newVcId).build())
        );

        this.saveBatch(newVsv);
    }
}

