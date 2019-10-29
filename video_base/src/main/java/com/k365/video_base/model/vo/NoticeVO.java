package com.k365.video_base.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Gavin
 * @date 2019/8/1 15:10
 * @description：
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NoticeVO {

    /**
     * 公告标题
     */
    private String title;

    /**
     * 公告内容
     */
    private String content;

    /**
     * 公告类型
     */
    private Integer type;

}
