package com.k365.user_service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.k365.user_service.VUserViewingRecordService;
import com.k365.video_base.mapper.VUserViewingRecordMapper;
import com.k365.video_base.model.dto.UserViewingRecordDTO;
import com.k365.video_base.model.po.VUserViewingRecord;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * VIEW 服务实现类
 * </p>
 *
 * @author Gavin
 * @since 2019-09-02
 */
@Service
public class VUserViewingRecordServiceImpl extends ServiceImpl<VUserViewingRecordMapper, VUserViewingRecord> implements VUserViewingRecordService {

    @Override
    public List<VUserViewingRecord> findAll(UserViewingRecordDTO userViewingRecordDTO) {

        return this.page(new Page<VUserViewingRecord>().setCurrent(userViewingRecordDTO.getPage())
                .setSize(userViewingRecordDTO.getPageSize()),new QueryWrapper<VUserViewingRecord>()
                .eq("u_id",userViewingRecordDTO.getUserId()).orderByDesc("uvr_record_time")).getRecords();
    }
}
