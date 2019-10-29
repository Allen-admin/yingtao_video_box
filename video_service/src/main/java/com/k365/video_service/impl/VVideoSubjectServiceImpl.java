package com.k365.video_service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.k365.video_base.mapper.VVideoSubjectMapper;
import com.k365.video_base.model.dto.VideoSubjectDTO;
import com.k365.video_base.model.po.VVideoSubject;
import com.k365.video_base.model.ro.VVideoSubjectRO;
import com.k365.video_common.constant.StatusEnum;
import com.k365.video_service.VVideoSubjectService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * VIEW 服务实现类
 * </p>
 *
 * @author Gavin
 * @since 2019-08-24
 */
@Service
public class VVideoSubjectServiceImpl extends ServiceImpl<VVideoSubjectMapper, VVideoSubject> implements VVideoSubjectService {

    @Override
    public List<VVideoSubjectRO> findVideoSubjects(VideoSubjectDTO videoSubjectDTO) {
        videoSubjectDTO.setPage((videoSubjectDTO.getPage() - 1) * videoSubjectDTO.getPageSize());
        return this.baseMapper.findVideoSubject(videoSubjectDTO);
    }

    @Override
    public List<VVideoSubject> findVideoBySubject(VideoSubjectDTO videoSubjectDTO) {
        List<VVideoSubject> records = this.page(new Page<VVideoSubject>().setSize(videoSubjectDTO.getPageSize())
                .setCurrent(videoSubjectDTO.getPage()), new QueryWrapper<VVideoSubject>()
                .eq("vs_id", videoSubjectDTO.getId()).eq("vs_status", StatusEnum.ENABLE.key())
                .eq("v_status", StatusEnum.ENABLE.key()).orderByAsc("v_sort")
                .select("v_id,v_code,v_cover,v_status,v_title,v_play_sum,v_create_date,v_time_len,v_is_vip")).getRecords();

        return records;
    }
}
