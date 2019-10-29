package com.k365.video_base.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Gavin
 * @date 2019/8/24 13:40
 * @description：
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VideoSubjectVO {

    private Object id;

    /**
     * 专题名称
     */
    private String name;

    /**
     * 专题封面
     */
    private String cover;

    /**
     * 专题排序
     */
    private Integer sort;

    /**
     * 专题描述
     */
    private String content;

    /**
     * 影片数量
     */
    private Integer videoTotal;

}
