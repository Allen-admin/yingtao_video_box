package com.k365.user_service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.k365.user_service.VUserVideoCollectionService;
import com.k365.video_base.mapper.VUserVideoCollectionMapper;
import com.k365.video_base.model.dto.VUserVideoCollectionDTO;
import com.k365.video_base.model.po.VUserVideoCollection;
import com.k365.video_base.model.vo.VUserVideoCollectionVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.ListUtils;

import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;

/**
 * <p>
 * VIEW 服务实现类
 * </p>
 *
 * @author Gavin
 * @since 2019-08-29
 */
@Service
public class VUserVideoCollectionServiceImpl extends ServiceImpl<VUserVideoCollectionMapper, VUserVideoCollection>
        implements VUserVideoCollectionService {

    @Override
    public List<VUserVideoCollectionVO> findByUidOrVid(VUserVideoCollectionDTO vUserVideoCollectionDTO) {
        QueryWrapper<VUserVideoCollection> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(vUserVideoCollectionDTO.getUId())) {
            queryWrapper.eq("u_id", vUserVideoCollectionDTO.getUId());
        }

        if (vUserVideoCollectionDTO.getVId() != null) {
            queryWrapper.eq("v_id", vUserVideoCollectionDTO.getVId());
        }

        List<VUserVideoCollection> records = this.page(new Page<VUserVideoCollection>().setCurrent(vUserVideoCollectionDTO.getPage())
                .setSize(vUserVideoCollectionDTO.getPageSize()), queryWrapper.orderByDesc("uvc_create_date")).getRecords();

        List<VUserVideoCollectionVO> voList = new ArrayList<>();
        if(!ListUtils.isEmpty(records)){
            Set<String> vIdSet = new HashSet<>();
            records.forEach( vuvc -> {
                if(!vIdSet.contains(vuvc.getVId())) {
                    voList.add(VUserVideoCollectionVO.builder().vCode(vuvc.getVCode()).vCover(vuvc.getVCover()).vId(vuvc.getVId())
                            .vCreateDate(vuvc.getVCreateDate()).vPlaySum(vuvc.getVPlaySum()).VIsVip(vuvc.getVIsVip()).vTimeLen(vuvc.getVTimeLen())
                            .vTitle(vuvc.getVTitle()).build());
                }
            });
        }

        return voList;

    }
}
