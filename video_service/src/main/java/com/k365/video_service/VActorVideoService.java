package com.k365.video_service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.k365.video_base.model.dto.VActorVideoDTO;
import com.k365.video_base.model.dto.VideoDTO;
import com.k365.video_base.model.po.VActorVideo;
import com.k365.video_base.model.vo.VideoBasicInfoVO;

import java.util.List;

/**
 * <p>
 * VIEW 服务类
 * </p>
 *
 * @author Gavin
 * @since 2019-08-30
 */
public interface VActorVideoService extends IService<VActorVideo> {

    /**
     * 根据女优id分页插视频
     */
    List<VideoBasicInfoVO> findByActorId(VActorVideoDTO vActorVideoDTO);

}
