package com.k365.manager_service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.k365.video_base.common.TaskTypeEnum;
import com.k365.video_base.model.dto.TaskDTO;
import com.k365.video_base.model.po.Task;
import com.k365.video_base.model.po.UserLevel;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author Gavin
 * @since 2019-07-17
 */
public interface TaskService extends IService<Task> {

    /**
     * 根据任务类型查询任务
     */
    List<Task> findTasksByType(TaskTypeEnum type);

    /**
     * 批量新增任务
     */
    void add(List<TaskDTO> taskDTOList);

    /**
     * 新增任务
     */
    void add(Task task);

    /**
     * 修改任务
     */
    void update(Task task);

    /**
     * 删除任务
     */
    void remove(Integer id);

    /**
     * 查询推广人数所对应的推广任务
     */
    Task findTaskBySpread(Integer spreadCount);

}
