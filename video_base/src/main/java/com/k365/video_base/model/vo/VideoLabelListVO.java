package com.k365.video_base.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Collection;

/**
 * @author Gavin
 * @date 2019/8/29 13:39
 * @description：
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class VideoLabelListVO {

    private String vId;

    private String vCode;

    private String vTitle;

    private String playUrl;

    private String saveUrl;

    private Long vPlaySum;

    private Long vCreateDate;

    private Boolean hasCollected;

    private Boolean hasFabulous;

    private Collection<VideoLabelVO> videoLabelVOList;
}
