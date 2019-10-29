package com.k365.video_service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.k365.video_base.mapper.VideoLabelTypeMapper;
import com.k365.video_base.model.po.VideoLabelType;
import com.k365.video_common.exception.ResponsiveException;
import com.k365.video_service.VideoLabelTypeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Gavin
 * @since 2019-08-13
 */
@Service
public class VideoLabelTypeServiceImpl extends ServiceImpl<VideoLabelTypeMapper, VideoLabelType> implements VideoLabelTypeService {

    @Override
    public void add(VideoLabelType videoLabelType) {
        VideoLabelType labelType = this.getOne(new QueryWrapper<VideoLabelType>().eq("name", videoLabelType.getName()));
        if(labelType != null)
            throw new ResponsiveException("同名视频标签类型已存在！");

        this.save(videoLabelType);
    }

    @Override
    public void update(VideoLabelType videoLabelType) {
        VideoLabelType labelType = this.getById(videoLabelType.getId());
        if(labelType == null)
            throw new ResponsiveException("标签类型不存在或已被删除");

        this.updateById(videoLabelType);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT)
    public void remove(Integer id) {
        this.removeById(id);

    }

    @Override
    public List<VideoLabelType> findAll() {

        return this.list(new QueryWrapper<VideoLabelType>().orderByAsc("sort"));
    }


}
