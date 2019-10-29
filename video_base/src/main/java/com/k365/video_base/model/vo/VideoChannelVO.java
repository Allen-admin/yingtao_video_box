package com.k365.video_base.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Gavin
 * @date 2019/8/12 13:54
 * @description：
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VideoChannelVO {

    /**
     * 频道名称
     */
    private String name;

    /**
     * 频道编码
     */
//    private String code;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * id
     */
    private Integer id;

}
