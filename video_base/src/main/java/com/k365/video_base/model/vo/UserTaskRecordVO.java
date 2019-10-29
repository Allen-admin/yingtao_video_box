package com.k365.video_base.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Gavin
 * @date 2019/8/26 16:06
 * @description：
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserTaskRecordVO {

    /**
     * 任务状态
     */
    private Integer status;

    /**
     * 任务名称
     */
    private String taskName;

}
