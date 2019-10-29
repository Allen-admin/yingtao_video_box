package com.k365.video_service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.k365.video_base.model.po.VideoActor;
import com.k365.video_base.mapper.VideoActorMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.k365.video_service.VideoActorService;
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
public class VideoActorServiceImpl extends ServiceImpl<VideoActorMapper, VideoActor> implements VideoActorService {

    @Override
    public void removeByActorId(Integer id) {
        this.remove(new UpdateWrapper<VideoActor>().eq("actor_id",id));
    }

    @Override
    public void removeByVideoId(String videoId) {
        this.remove(new UpdateWrapper<VideoActor>().eq("video_id",videoId));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT)
    public void updateByVideoId(String videoId, Integer[] actorId) {
        List<Integer> oldVaIdList = JSONArray.parseArray(JSON.toJSONString(
                this.listObjs(new QueryWrapper<VideoActor>().eq("video_id",videoId).select("actor_id"))),Integer.class);

        Set<Integer> newActorIds = new HashSet<>(Arrays.asList(actorId));
        List<VideoActor> newVaList = new ArrayList<>();
        if(!ListUtils.isEmpty(oldVaIdList)){
            Set<Integer> oldActorIds = new HashSet<>();
            Set<Integer> intersection = new HashSet<>(oldActorIds);
            //取交集
            intersection.retainAll(newActorIds);

            //取 old 与 交集 的差集 【删除】
            oldActorIds.removeAll(intersection);
            if(!SetUtils.isEmpty(oldActorIds)) {
                this.remove(new UpdateWrapper<VideoActor>().in("actor_id", oldActorIds));
            }

            //取 new 与 交集 的差集 【新增】
            newActorIds.removeAll(intersection);

        }

        newActorIds.forEach(newActorId -> newVaList.add(VideoActor.builder().actorId(newActorId).videoId(videoId).build()));

        this.saveBatch(newVaList);
    }
}
