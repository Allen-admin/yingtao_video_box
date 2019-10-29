package com.k365.video_service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.k365.video_base.model.po.VVideoChannelLabel;
import com.k365.video_base.model.po.VideoLabel;
import com.k365.video_base.model.ro.VVideoChannelLabelRO;
import com.k365.video_base.model.so.VideoSO;
import com.k365.video_base.model.vo.BaseListVO;
import com.k365.video_base.model.vo.VVideoChannelLabelVO;
import com.k365.video_base.model.vo.VideoLabelListVO;
import com.k365.video_base.model.vo.VideoLabelVO;

import java.util.List;

/**
 * <p>
 * VIEW 服务类
 * </p>
 *
 * @author Gavin
 * @since 2019-08-15
 */
public interface VVideoChannelLabelService extends IService<VVideoChannelLabel> {

    /**
     * 根据类型筛选
     */
    BaseListVO<VVideoChannelLabelVO> searchByKeyWordAndType(VideoSO videoSO);

    /**
     * 根据视频标签和状态查询视频
     */
    List<VVideoChannelLabel> findVideosByLabelIds(VideoSO videoSO);

    /**
     * 根据频道id查询视频
     */
    List<VVideoChannelLabel> findVideoByChannel(VideoSO videoSO);

    /**
     * 查询热门视频标签
     */
    List<VideoLabelVO> findHotLabelByHotVideo(VideoSO videoSO);

    /**
     * 根据视频id查询所有视频标签和视频信息
     */
    VideoLabelListVO findLabelsByVId(String id);

}
