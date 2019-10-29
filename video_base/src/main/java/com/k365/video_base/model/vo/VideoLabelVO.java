package com.k365.video_base.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Gavin
 * @date 2019/8/23 9:19
 * @description：
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VideoLabelVO {

    /**
     * 标签id
     */
    private Integer vlId;

    /**
     * 标签名称
     */
    private String vlName;


}
