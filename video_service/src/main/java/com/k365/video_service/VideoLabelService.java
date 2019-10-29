package com.k365.video_service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.k365.video_base.model.dto.VideoLabelDTO;
import com.k365.video_base.model.po.VideoLabel;
import com.k365.video_base.model.vo.BaseListVO;
import com.k365.video_base.model.vo.VideoLabelTypeVO;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author Gavin
 * @since 2019-07-17
 */
public interface VideoLabelService extends IService<VideoLabel> {

    /**
     * 添加视频标签
     */
    void add(VideoLabelDTO videoLabelDTO);

    /**
     * 修改视频标签
     */
    void update(VideoLabelDTO videoLabelDTO);

    /**
     * 删除标签
     */
    void remove(Integer id);

    /**
     * 查询所有标签
     */
    List<VideoLabel> findAll();

    /**
     * 查询所有的标签和标签类型
     */
    Collection<VideoLabelTypeVO> findLabelAndType();


    /**
     * 分页查询所有的标签
     */
    BaseListVO<VideoLabel> findPage(VideoLabelDTO videoLabelDTO);

    /**
     * 模糊查询视频标签
     */
    BaseListVO<VideoLabel> search(VideoLabelDTO videoLabelDTO);

}
