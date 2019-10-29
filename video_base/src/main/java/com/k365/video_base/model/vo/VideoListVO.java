package com.k365.video_base.model.vo;

import com.k365.video_base.model.po.Video;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Collection;

/**
 * @author Gavin
 * @date 2019/9/3 19:14
 * @description：
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class VideoListVO extends Video {

    /**
     * 视频标签id
     */
    private Collection<VideoLabelVO> videoLabels;

    /**
     * 视频频道id
     */
    private Collection<VideoChannelVO> videoChannels;

    /**
     * 视频女优id
     */
    private Collection<ActorVO> actors;


    /**
     * 视频主题id
     */
    private Collection<VideoSubjectVO> videoSubjects;
}
