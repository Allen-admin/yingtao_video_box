package com.k365.video_base.model.vo;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @author Allen
 * @date 2019/8/8 11:22
 * @description：
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskVO {

    private Integer id;

    private String taskName;

    private Integer viewingCount;

    private Integer downloadCount;

    private Integer recommendAmount;

    /**
     * 每日可完成次数上限
     */
    private Integer dailyCeiling;

    /**
     * 任务编码
     */
    private Integer taskCode;

}
