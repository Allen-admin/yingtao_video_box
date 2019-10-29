package com.k365.video_service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.k365.user_service.UserVideoCollectionService;
import com.k365.video_base.mapper.VVideoChannelLabelMapper;
import com.k365.video_base.model.po.VVideoChannelLabel;
import com.k365.video_base.model.po.VideoLabel;
import com.k365.video_base.model.ro.VVideoChannelLabelRO;
import com.k365.video_base.model.so.VideoSO;
import com.k365.video_base.model.vo.*;
import com.k365.video_common.constant.StatusEnum;
import com.k365.video_service.VVideoChannelLabelService;
import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.util.ArrayListWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.ListUtils;

import java.util.*;
import java.util.zip.Inflater;

/**
 * <p>
 * VIEW 服务实现类
 * </p>
 *
 * @author Gavin
 * @since 2019-08-15
 */
@Service
public class VVideoChannelLabelServiceImpl extends ServiceImpl<VVideoChannelLabelMapper, VVideoChannelLabel>
        implements VVideoChannelLabelService {

    @Autowired
    private UserVideoCollectionService userVideoCollectionService;

    @Override
    public BaseListVO<VVideoChannelLabelVO> searchByKeyWordAndType(VideoSO videoSO) {
        videoSO.setPage((videoSO.getPage() - 1) * videoSO.getPageSize());

        videoSO.setSearchValue(StringUtils.join(videoSO.getSearchValue(),
                StringUtils.isNotBlank(videoSO.getSearchValue()) ? "*" : ""));

        List<VVideoChannelLabelRO> roList = this.baseMapper.searchMatchPage(videoSO);

        List<VVideoChannelLabelVO> voList = new ArrayList<>();
        if(!ListUtils.isEmpty(roList)){
            roList.forEach(ro -> {
                //视频频道数组
                String[] videoChannels = StringUtils.isNotBlank(ro.getVideoChannels()) ? ro.getVideoChannels().split(",") : new String[0];

                //视频标签数组
                String[] videoLabels = StringUtils.isNotBlank(ro.getVideoLabels()) ? ro.getVideoLabels().split(",") : new String[0];

                voList.add(VVideoChannelLabelVO.builder().vId(ro.getVId())
                    .vStatus(ro.getVStatus()).vTitle(ro.getVTitle()).vCode(ro.getVCode()).vCover(ro.getVCover())
                    .vCreateDate(ro.getVCreateDate()).vcId(ro.getVcId()).vcName(ro.getVcName()).vcSort(ro.getVcSort())
                    .vlId(ro.getVlId()).vlName(ro.getVlName()).vcSort(ro.getVcSort())
                    .videoChannels(videoChannels).videoLabels(videoLabels).build());
            });
        }

        Long total = this.baseMapper.searchMatchPageCount(videoSO);
        return new BaseListVO<VVideoChannelLabelVO>().setTotal(total).setList(voList);
    }

    @Override
    public List<VVideoChannelLabel> findVideosByLabelIds(VideoSO videoSO) {
        return this.page(new Page<VVideoChannelLabel>().setCurrent(videoSO.getPage()).setSize(videoSO.getPageSize()),
                new QueryWrapper<VVideoChannelLabel>().in("vl_id", videoSO.getVideoLabelIds())
                        .eq("v_status", videoSO.getStatus()).groupBy("v_id")
                        .select("v_id,v_code,v_title,v_play_sum,v_time_len,v_create_date,v_cover,v_is_vip").
                        orderBy(true,videoSO.getIsAsc(),videoSO.getSortName())).getRecords();

        //        return this.baseMapper.findVideosByLabelIds(videoSO);

    }

    @Override
    public List<VVideoChannelLabel> findVideoByChannel(VideoSO videoSO) {
        return  this.page(new Page<VVideoChannelLabel>().setCurrent(videoSO.getPage()).setSize(videoSO.getPageSize()),
                new QueryWrapper<VVideoChannelLabel>().eq("vc_id", videoSO.getVideoChannelId()).eq("v_status",StatusEnum.ENABLE.key())
                        .groupBy("v_id").select("v_id,v_cover,v_title,v_play_sum,v_time_len,v_create_date,v_is_vip")
                        .orderBy(true,videoSO.getIsAsc(),videoSO.getSortName())).getRecords();
    }

    @Override
    public List<VideoLabelVO> findHotLabelByHotVideo(VideoSO videoSO) {
        //获取播放量前30的视频
        List<VVideoChannelLabel> records = this.page(new Page<VVideoChannelLabel>().setCurrent(1).setSize(30),
                new QueryWrapper<VVideoChannelLabel>().eq("v_status", StatusEnum.ENABLE.key())
                        .orderByDesc("v_play_sum").select("vl_id,vl_name")).getRecords();

        List<VideoLabelVO> voList = new ArrayList<>(20);
        if(!ListUtils.isEmpty(records)){
            Map<String,Integer> map = new HashMap<>();
            records.forEach(vvcl -> {
                if(vvcl != null) {
                    String key = StringUtils.join(vvcl.getVlId(), "#", vvcl.getVlName());
                    map.put(key, map.get(key) == null ? 1 : map.get(key) + 1);
                }
            });

            //按照热度值降序排
            List<Map.Entry<String,Integer>> sortList = new ArrayList<>(map.entrySet());
            sortList.sort(Comparator.comparing(Map.Entry<String,Integer>::getValue).reversed());
            Iterator<Map.Entry<String, Integer>> iterator = sortList.iterator();
            while(iterator.hasNext() && voList.size() < 20 ){
                Map.Entry<String, Integer> next = iterator.next();
                String[] label = next.getKey().split("#");
                voList.add(VideoLabelVO.builder().vlId(Integer.valueOf(label[0])).vlName(label[1]).build());
            }
        }

        return voList;
    }

    @Override
    public VideoLabelListVO findLabelsByVId(String id) {
        VideoLabelListVO result = new VideoLabelListVO();
        List<VVideoChannelLabel> list = this.list(new QueryWrapper<VVideoChannelLabel>().eq("v_id", id)
                .eq("v_status", StatusEnum.ENABLE.key()).orderByAsc("vl_sort")
                .select("v_id,v_code,v_title,v_play_sum,v_create_date,v_play_url,v_save_url,vl_id,vl_name"));


        Set<Integer> vlIds = new HashSet<>();
        List<VideoLabelVO> labelVOList = new ArrayList<>();
        if(!ListUtils.isEmpty(list)){
            list.forEach(vvcl -> {
                if(!vlIds.contains(vvcl.getVlId())){
                    labelVOList.add(VideoLabelVO.builder().vlName(vvcl.getVlName()).vlId(vvcl.getVlId()).build());
                    vlIds.add(vvcl.getVlId());
                }

                if(result.getVId() == null){
                    result.setVId(vvcl.getVId()).setVCode(vvcl.getVCode()).setVCreateDate(vvcl.getVCreateDate().getTime())
                            .setVPlaySum(vvcl.getVPlaySum()).setVTitle(vvcl.getVTitle()).setSaveUrl(vvcl.getVSaveUrl())
                            .setPlayUrl(vvcl.getVPlayUrl());
                }
            });
            result.setVideoLabelVOList(labelVOList);
            result.setHasCollected(userVideoCollectionService.hasCollection(result.getVId()));
        }

        return result;
    }
}
