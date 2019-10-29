package com.k365.video_base.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.k365.video_base.model.po.Video;
import com.k365.video_base.model.so.VideoSO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author Gavin
 * @since 2019-07-17
 */
@Mapper
public interface VideoMapper extends BaseMapper<Video> {

    /**
     * 全文检索 视频标题和车牌号
     */
    List<Video> searchMatchVideoPage(VideoSO videoSO);

    /**
     * 全文检索 视频标题和车牌号 匹配到的结果数
     */
    Long searchMatchVideoPageCount(VideoSO videoSO);

}
