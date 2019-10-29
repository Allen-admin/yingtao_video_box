package com.k365.video_base.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;


/**
 * @author Gavin
 * @date 2019/8/23 9:16
 * @description：
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VideoLabelTypeVO {

    /**
     * 标签类型id
     */
    private Integer vltId;

    /**
     * 标签类型名称
     */
    private String vltName;

    private Collection<VideoLabelVO> vlList;

}
