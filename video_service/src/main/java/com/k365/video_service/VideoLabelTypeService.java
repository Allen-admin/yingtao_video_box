package com.k365.video_service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.k365.video_base.model.po.VideoLabelType;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author Gavin
 * @since 2019-08-13
 */
public interface VideoLabelTypeService extends IService<VideoLabelType> {

    /**
     * 添加视频标签类型
     */
    void add(VideoLabelType videoLabelType);

    /**
     * 修改视频标签类型
     */
    void update(VideoLabelType videoLabelType);

    /**
     * 删除标签类型
     */
    void remove(Integer id);

    /**
     * 查询所有标签类型
     */
    List<VideoLabelType> findAll();

}
