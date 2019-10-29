
package com.k365.manager_service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.k365.manager_service.TaskService;
import com.k365.video_base.common.TaskTypeEnum;
import com.k365.video_base.mapper.TaskMapper;
import com.k365.video_base.model.dto.TaskDTO;
import com.k365.video_base.model.po.Task;
import com.k365.video_common.constant.StatusEnum;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.ListUtils;
import org.thymeleaf.util.SetUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Gavin
 * @date 2019/6/29 21:14
 * @description：
 */

@Service
public class TaskServiceImpl extends ServiceImpl<TaskMapper, Task> implements TaskService {

    @Override
    public List<Task> findTasksByType(TaskTypeEnum type) {
        return this.list(new QueryWrapper<Task>().eq("task_type", type.key()).orderByAsc("sort"));
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public void add(List<TaskDTO> taskDTOList) {
        if (!ListUtils.isEmpty(taskDTOList)) {
            //修改旧任务 新增新任务
            List<Task> newTask = new ArrayList<>();
            List<Task> updateTask = new ArrayList<>();

            List<Integer> oldIdList = JSONArray.parseArray(JSON.toJSONString(this.listObjs(
                    new QueryWrapper<Task>().eq("task_type", TaskTypeEnum.PROMOTION_TASK.key()).select("id"))), Integer.class);

            Set<Integer> oldIdSet = new HashSet<>(oldIdList);
            Set<Integer> updateIdSet = new HashSet<>();
            for (TaskDTO taskDTO : taskDTOList) {
                if (taskDTO.getId() != null && oldIdList.contains(taskDTO.getId())) {
                    //修改
                    updateTask.add(Task.builder().id(taskDTO.getId()).recommendAmount(taskDTO.getRecommendAmount())
                            .viewingCount(taskDTO.getViewingCount()).downloadCount(taskDTO.getDownloadCount()).build());

                    updateIdSet.add(taskDTO.getId());

                } else {
                    //新增
                    newTask.add(Task.builder().recommendAmount(taskDTO.getRecommendAmount())
                            .taskType(TaskTypeEnum.PROMOTION_TASK.key()).viewingCount(taskDTO.getViewingCount())
                            .downloadCount(taskDTO.getDownloadCount()).build());
                }
            }

            if (!ListUtils.isEmpty(newTask)) {
                this.saveBatch(newTask);
            }

            if (!ListUtils.isEmpty(updateTask)) {
                this.updateBatchById(updateTask);
            }

            //1、旧id 与 更新id 取 交集 2、旧id 与 '交集' 取差集 3、删除 差集
            updateIdSet.retainAll(oldIdSet);
            oldIdSet.removeAll(updateIdSet);

            if (!SetUtils.isEmpty(oldIdSet)) {
                this.removeByIds(oldIdSet);
            }
        }

    }

    @Override
    public void add(Task task) {
        this.save(task);
    }

    @Override
    public void update(Task task) {
        Task task2 = this.getById(task.getId());
        if (task2 == null) {
            throw new RuntimeException("任务不存在或已被删除");
        }

        this.updateById(task);
    }

    @Override
    public void remove(Integer id) {
        this.removeById(id);
    }

    @Override
    public Task findTaskBySpread(Integer spreadCount) {
        List<Task> tasks = this.list(new QueryWrapper<Task>().le("recommend_amount", spreadCount)
                .eq("status", StatusEnum.ENABLE.key()).eq("task_type", TaskTypeEnum.PROMOTION_TASK.key())
                .orderByDesc("recommend_amount"));

        if (!ListUtils.isEmpty(tasks)) {
            return tasks.get(0);
        }
        return null;
    }


}

