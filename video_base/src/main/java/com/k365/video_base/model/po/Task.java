package com.k365.video_base.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * <p>
 * </p>
 *
 * @author Gavin
 * @since 2019-07-17
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Task {

    @TableId(type = IdType.AUTO)
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
     * 福利任务类型
     */
    private Integer taskCode;

}
