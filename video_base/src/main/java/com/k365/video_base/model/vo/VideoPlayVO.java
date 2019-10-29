package com.k365.video_base.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author Gavin
 * @date 2019/8/29 13:07
 * @description：
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VideoPlayVO {

    private VideoLabelListVO videoInfo;

    private Map<String,AdVO> adInfo;

}
