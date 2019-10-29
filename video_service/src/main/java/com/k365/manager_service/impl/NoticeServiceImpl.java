package com.k365.manager_service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.k365.manager_service.NoticeService;
import com.k365.video_base.common.AppDisplayTypeEnum;
import com.k365.video_base.mapper.NoticeMapper;
import com.k365.video_base.model.dto.NoticeDTO;
import com.k365.video_base.model.po.Notice;
import com.k365.video_base.model.vo.NoticeVO;
import com.k365.video_common.constant.StatusEnum;
import com.k365.video_common.exception.ResponsiveException;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Gavin
 * @since 2019-07-17
 */
@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice> implements NoticeService {

    @Override
    public void add(NoticeDTO noticeDTO) {
        Notice notice = Notice.builder().content(noticeDTO.getContent())
                .title(noticeDTO.getTitle()).status(noticeDTO.getStatus())
                .type(noticeDTO.getType()).sort(noticeDTO.getSort()).createDate(new Date()).
                        appType(noticeDTO.getAppType()).build();

        this.save(notice);
    }

    @Override
    public void update(NoticeDTO noticeDTO) {

        Notice notice = this.getById(noticeDTO.getId());
        if (notice == null)
            throw new ResponsiveException("公告不存在或已被删除");

        BeanUtils.copyProperties(noticeDTO, notice);

        this.updateById(notice);
    }

    @Override
    public void remove(Integer id) {
        this.removeById(id);
    }

    @Override
    public List<NoticeVO> findListByType(AppDisplayTypeEnum adtEnum,Integer appType) {
        List<Notice> list = this.list(new QueryWrapper<Notice>().eq("type", adtEnum.key())
                .eq("app_type",appType).eq("status", StatusEnum.ENABLE.key()).orderByAsc("sort"));

        List<NoticeVO> voList = new ArrayList<>();
        list.forEach(notice ->
                voList.add(NoticeVO.builder().title(notice.getTitle())
                        .content(notice.getContent())
                        .type(notice.getType()).build())
        );

        return voList;
    }

    @Override
    public List<Notice> findAll(Integer appType) {

        return this.list(new QueryWrapper<Notice>().eq("app_type",appType).orderByAsc("sort"));
    }
}
