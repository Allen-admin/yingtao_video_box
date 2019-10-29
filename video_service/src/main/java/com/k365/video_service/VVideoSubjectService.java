package com.k365.video_service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.k365.video_base.model.dto.VideoSubjectDTO;
import com.k365.video_base.model.po.VVideoSubject;
import com.k365.video_base.model.ro.VVideoSubjectRO;
import com.k365.video_base.model.vo.VideoSubjectVO;

import java.util.List;

/**
 * <p>
 * VIEW 服务类
 * </p>
 *
 * @author Gavin
 * @since 2019-08-24
 */
public interface VVideoSubjectService extends IService<VVideoSubject> {

    /**
     * 分页查询视频专题和视频数量
     */
    List<VVideoSubjectRO> findVideoSubjects(VideoSubjectDTO videoSubjectDTO);

    /**
     * 分页查询专题视频
     */
    List<VVideoSubject> findVideoBySubject(VideoSubjectDTO videoSubjectDTO);

}
