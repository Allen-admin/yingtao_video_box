package com.k365.video_service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.k365.video_base.model.dto.VideoSubjectDTO;
import com.k365.video_base.model.po.VideoSubject;
import com.k365.video_base.model.ro.VVideoSubjectRO;
import com.k365.video_base.model.vo.BaseListVO;

import javax.servlet.ServletRequest;
import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author Gavin
 * @since 2019-08-14
 */
public interface VideoSubjectService extends IService<VideoSubject> {

    /**
     * 添加视频专题
     */
    void add(VideoSubject videoSubject);

    /**
     * 修改视频专题
     */
    void update(VideoSubject videoSubject);

    /**
     * 删除视频专题
     */
    void remove(Integer id);

    /**
     * 查询所有视频专题
     */
    List<VideoSubject> findAll(VideoSubjectDTO videoSubjectDTO);

    /**
     * 分页查询视频专题
     * @param videoSubjectDTO
     * @return
     */
    BaseListVO<VideoSubject> findPage(VideoSubjectDTO videoSubjectDTO);

    /**
     * 分页查询专题列表
     */
    List<VVideoSubjectRO> pageList(VideoSubjectDTO videoSubjectDTO,ServletRequest request);


}
