package com.k365.video_service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.k365.video_base.mapper.VActorVideoMapper;
import com.k365.video_base.model.dto.VActorVideoDTO;
import com.k365.video_base.model.po.VActorVideo;
import com.k365.video_base.model.vo.VideoBasicInfoVO;
import com.k365.video_common.constant.StatusEnum;
import com.k365.video_service.VActorVideoService;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.ListUtils;

import java.util.List;
import java.util.ArrayList;

/**
 * <p>
 * VIEW 服务实现类
 * </p>
 *
 * @author Gavin
 * @since 2019-08-30
 */
@Service
public class VActorVideoServiceImpl extends ServiceImpl<VActorVideoMapper, VActorVideo> implements VActorVideoService {

    @Override
    public List<VideoBasicInfoVO> findByActorId(VActorVideoDTO vActorVideoDTO) {
        List<VActorVideo> videoList = this.page(new Page<VActorVideo>().setSize(vActorVideoDTO.getPageSize()).setCurrent(vActorVideoDTO.getPage()),
                new QueryWrapper<VActorVideo>().eq("a_id", vActorVideoDTO.getAId()).eq("v_status", StatusEnum.ENABLE.key()).groupBy("v_id")
                        .select("v_id, v_code,v_cover,v_create_date,v_play_sum,v_time_len,v_title,v_is_vip")).getRecords();

        List<VideoBasicInfoVO> voList = new ArrayList<>();
        if(!ListUtils.isEmpty(videoList)){
            videoList.forEach(vav -> voList.add(new VideoBasicInfoVO().setId(vav.getVId()).setCover(vav.getVCover())
                    .setTitle(vav.getVTitle()).setPlaySum(vav.getVPlaySum()).setTimeLen(vav.getVTimeLen())
                    .setCreateDate(vav.getVCreateDate()).setIsVip(vav.getVIsVip())));
        }

        return voList;
    }
}
