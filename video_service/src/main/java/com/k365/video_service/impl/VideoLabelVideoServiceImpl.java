package com.k365.video_service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.k365.video_base.mapper.VideoLabelVideoMapper;
import com.k365.video_base.model.po.Video;
import com.k365.video_base.model.po.VideoLabelVideo;
import com.k365.video_common.constant.StatusEnum;
import com.k365.video_common.util.MapSortUtil;
import com.k365.video_service.VideoLabelVideoService;
import com.k365.video_service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
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

    @Autowired
    @Lazy
    private VideoService videoService;


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
                this.remove(new UpdateWrapper<VideoLabelVideo>().in("video_label_id", oldVlIds).eq("video_id",videoId));
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

    @Override
    public List<Video> getVideoVoByLabels(List<Integer> labels) {

        //创建一个list装视频id
        List<String> videoIds=new ArrayList<>();
        //创建一个map用于去重
        Map<String,Integer> map=new HashMap<>();
        for (Integer label:labels){
            List<VideoLabelVideo> videoLabelVideos = this.list(new QueryWrapper<VideoLabelVideo>().eq("video_label_id", label));
            for(VideoLabelVideo videoLabelVideo:videoLabelVideos){
                if(map.get(videoLabelVideo.getVideoId())!=null){
                    map.put(videoLabelVideo.getVideoId(),map.get(videoLabelVideo.getVideoId())+1);
                }else{
                    map.put(videoLabelVideo.getVideoId(),1);
                }
            }
        }
        //对map进行排序
        map=MapSortUtil.sortDescend(map);
        System.out.println(map);
        //遍历map获取前100个视频id
        if(map.size()<=100){
            for (String key : map.keySet()) {
                videoIds.add(key);
            }
        }else{
            int i=0;
            for (String key : map.keySet()) {
                if(i<100){
                    videoIds.add(key);
                    i=i+1;
                }else{
                    break;
                }
            }
        }
        List<Video> videos=new ArrayList<>();
        //通过最后得到的视频id查询视频相关信息
        for(String vid:videoIds){

            Video video = videoService.getById(vid);
            if(video!=null){
                if(video.getStatus()==StatusEnum.ENABLE.key()){
                    videos.add(video);
                }
            }

        }

        return videos;
    }

    @Override
    public List<VideoLabelVideo> getVideoLableVideosByVideoId(String videoId) {
        return this.list(
                new QueryWrapper<VideoLabelVideo>()
                        .eq("video_id", videoId));
    }

    @Override
    public List<VideoLabelVideo> getVideoLableVideosByLableId(String videoLabelId) {
        return this.list(
                new QueryWrapper<VideoLabelVideo>()
                        .eq("video_label_id", videoLabelId));
    }
}
