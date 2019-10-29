package com.k365.video_base.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Gavin
 * @date 2019/8/24 13:50
 * @description：
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VideoSubjectDTO extends SplitPageDTO implements BaseDTO {

    private Integer id;

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

    /**
     * 视频状态
     */
    private Integer vStatus;

    /**
     * 视频主题状态
     */
    private Integer vsStatus;
}
