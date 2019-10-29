package com.k365.video_base.model.dto;

import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author Gavin
 * @since 2019-07-17
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskDTO extends SplitPageDTO implements BaseDTO {

    private Integer id;

    /**
     * 任务类型
     */
    private Integer taskType;

    /**
     * 任务名称
     */
    private String taskName;

    /**
     * 任务奖励观影次数
     */
    private Integer viewingCount;

    /**
     * 任务奖励下载次数
     */
    private Integer downloadCount;

    /**
     * 任务推荐人数
     */
    private Integer recommendAmount;

    /**
     * 任务排序
     */
    private Integer sort;

    /**
     * 任务状态
     */
    private Integer status;

    /**
     * 每日可完成次数上限
     */
    private Integer dailyCeiling;

    /**
     * 任务编码
     */
    private Integer taskCode;

}
