package com.k365.video_base.model.so;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Collection;
import java.util.List;

/**
 * @author Gavin
 * @date 2019/8/14 14:30
 * @description：
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VideoSO extends BaseSO{

    /**
     * 检索关键字 番号+标题
     */
    private String searchValue;

    /**
     * 频道筛选
     */
    private Integer videoChannelId;

    /**
     * 状态筛选
     */
    private Integer status;

    /**
     * 标签筛选
     */
    private Integer videoLabelId;

    /**
     * 主题id
     */
    private Integer videoSubjectId;

    /**
     * 标签筛选
     */
    private Collection<Integer> videoLabelIds;

    /**
     * 视频ID
     */
    private String videoId;
}
