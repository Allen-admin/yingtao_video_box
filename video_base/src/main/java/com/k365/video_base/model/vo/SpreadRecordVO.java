package com.k365.video_base.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @author Gavin
 * @date 2019/8/28 18:05
 * @description：
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SpreadRecordVO {

    /**
     * 被邀请人
     */
    private String inviterNickname;

    /**
     * 注册时间
     */
    private Long registerTime;
}