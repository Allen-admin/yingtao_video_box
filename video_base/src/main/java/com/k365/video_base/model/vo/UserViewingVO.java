package com.k365.video_base.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Gavin
 * @date 2019/8/27 10:29
 * @description：
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserViewingVO {

    /**
     * 视频下载次数
     */
    private Integer saveCount;

    /**
     * 已用下载次数
     */
    private Integer usedSaveCount;

    /**
     * 已用观影次数
     */
    private Integer usedViewingCount;

    /**
     * 总观影次数
     */
    private Integer viewingCount;
}
