package com.k365.video_base.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.k365.video_base.model.po.VVideoChannelLabel;
import com.k365.video_base.model.ro.VVideoChannelLabelRO;
import com.k365.video_base.model.so.VideoSO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * VIEW Mapper 接口
 * </p>
 *
 * @author Gavin
 * @since 2019-08-15
 */
@Mapper
public interface VVideoChannelLabelMapper extends BaseMapper<VVideoChannelLabel> {

    /**
     * 全文检索 视频标题和车牌号
     */
    List<VVideoChannelLabelRO> searchMatchPage(VideoSO videoSO);

    /**
     * 全文检索 视频标题和车牌号 匹配到的结果数
     */
    Long searchMatchPageCount(VideoSO videoSO);

    /**
     * 根据视频标签查询视频
     */
    List<VVideoChannelLabelRO> findVideosByLabelIds(VideoSO videoSO);

}
