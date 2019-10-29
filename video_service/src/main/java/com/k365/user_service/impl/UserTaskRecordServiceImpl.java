package com.k365.user_service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.k365.manager_service.TaskService;
import com.k365.user_service.UserService;
import com.k365.user_service.UserTaskRecordService;
import com.k365.video_base.common.TaskCodeEnum;
import com.k365.video_base.common.TaskTypeEnum;
import com.k365.video_base.common.UserContext;
import com.k365.video_base.common.VideoContants;
import com.k365.video_base.mapper.UserTaskRecordMapper;
import com.k365.video_base.model.po.Task;
import com.k365.video_base.model.po.User;
import com.k365.video_base.model.po.UserTaskRecord;
import com.k365.video_base.model.vo.UserTaskRecordVO;
import com.k365.video_common.constant.StatusEnum;
import com.k365.video_common.util.DateUtil;
import com.k365.video_common.util.RedisUtil;
import com.k365.video_common.util.TranslateUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.ListUtils;

import java.util.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Gavin
 * @since 2019-07-17
 */
@Service
public class UserTaskRecordServiceImpl extends ServiceImpl<UserTaskRecordMapper, UserTaskRecord> implements UserTaskRecordService {

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserService userService;

    @Autowired
    private RedisUtil cache;

    @Override
    public List<UserTaskRecordVO> findRecommend() {
        List<UserTaskRecordVO> result = new ArrayList<>();
        Map<String, List<UserTaskRecordVO>> all = this.findAll();
        List<Map.Entry<String, List<UserTaskRecordVO>>> entries = new ArrayList<>(all.entrySet());

        //排序 （优先获取福利任务，然后获取每日任务，最后获取推广任务）
        //entries.sort(Comparator.comparing(Map.Entry<String, List<UserTaskRecordVO>>::getKey));
        Iterator<Map.Entry<String, List<UserTaskRecordVO>>> it = entries.iterator();

        while (it.hasNext() && result.size() < 3) {
            Map.Entry<String, List<UserTaskRecordVO>> next = it.next();
            List<UserTaskRecordVO> value = next.getValue();
            if (!ListUtils.isEmpty(value)) {
                Iterator<UserTaskRecordVO> it2 = value.iterator();
                while (it2.hasNext() && result.size() < 3) {
                    UserTaskRecordVO userTaskRecordVO = it2.next();
                    if (Objects.equals(userTaskRecordVO.getStatus(), StatusEnum.UNFINISHED.key())) {
                        result.add(userTaskRecordVO);
                    }
                }
            }
        }

        return result;
    }

    @Override
    public Map<String, List<UserTaskRecordVO>> findAll() {
        User currentUser = UserContext.getCurrentUser();
        List<Integer> userTaskId = JSONArray.parseArray(JSON.toJSONString(this.listObjs(new QueryWrapper<UserTaskRecord>()
                .eq("user_id", currentUser.getId()).eq("status", StatusEnum.FINISHED.key())
                .select("task_id"))), Integer.class);


        List<Task> taskList = taskService.list(new QueryWrapper<Task>().eq("status", StatusEnum.ENABLE.key())
                .orderByAsc("task_type").orderByAsc("recommend_amount"));

        Map<String, List<UserTaskRecordVO>> result = new LinkedHashMap<>();

        //保证返回数据结构统一
        for (TaskTypeEnum taskType : TaskTypeEnum.values()) {
            result.put(taskType.name(), new ArrayList<>());
        }

        if (!ListUtils.isEmpty(taskList)) {
            taskList.forEach(task -> {
                StatusEnum finishedStatus = userTaskId.contains(task.getId()) ? StatusEnum.FINISHED : StatusEnum.UNFINISHED;
                TaskTypeEnum taskTypeEnum = TaskTypeEnum.getByKey(task.getTaskType());
                String taskName = "";
                if (taskTypeEnum != null) {
                    /*if (result.get(taskTypeEnum.name()) == null) {
                        result.put(taskTypeEnum.name(), new ArrayList<>());
                    }*/
                    switch (taskTypeEnum) {
                        case DAILY_TASKS: //每日任务
                            taskName = StringUtils.join(task.getTaskName(), "观影 +",
                                    task.getViewingCount(), " 每日", TranslateUtil.arabicNumberToZh_cn(task.getDailyCeiling()), "次 当日有效");
                            break;
                        case WELFARE_TASKS: //福利任务
                            taskName = StringUtils.join(task.getTaskName(), " 观影+", task.getViewingCount());
                            break;
                        case PROMOTION_TASK: //推广任务
                            taskName = StringUtils.join("推广", task.getRecommendAmount(), "人 观影+",
                                    task.getViewingCount(), " 下载+", task.getDownloadCount());
                            break;
                    }
                    result.get(taskTypeEnum.name()).add(UserTaskRecordVO.builder().taskName(taskName)
                            .status(finishedStatus.key()).build());
                }
            });
        }

        return result;
    }

    @Override
    public boolean addOrUpdate(UserTaskRecord userTaskRecord) {
        UserTaskRecord taskRecord = this.getOne(new QueryWrapper<UserTaskRecord>()
                .eq("task_id", userTaskRecord.getTaskId()).eq("user_id", userTaskRecord.getUserId()));

        taskRecord = taskRecord == null ? userTaskRecord : taskRecord;

        taskRecord.setStatus(userTaskRecord.getStatus());
        taskRecord.setCompleteTime(new Date());
        return this.saveOrUpdate(taskRecord);

    }

    @Override
    public boolean toDoFristUpPortrait(String uId) {
        return this.doWelfareTasks(TaskCodeEnum.FRIST_UP_PORTRAIT, uId);
    }

    @Override
    public boolean toDoFristBindPhone(String uId) {
        return this.doWelfareTasks(TaskCodeEnum.FRIST_BIND_PHONE, uId);
    }

    @Override
    public boolean toDoSaveQrImg() {
        return onlyFirstEffectiveTask(TaskCodeEnum.SAVE_QR);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public boolean onlyFirstEffectiveTask(TaskCodeEnum taskCodeEnum) {

        Task task = taskService.getOne(new QueryWrapper<Task>().eq("task_type",
                taskCodeEnum.getTaskType().key()).eq("task_code",
                taskCodeEnum.key()).eq("status", StatusEnum.ENABLE.key()));

        User currentUser = UserContext.getCurrentUser();
        if (task != null) {
            UserTaskRecord taskRecord = this.getOne(new QueryWrapper<UserTaskRecord>()
                    .eq("task_id", task.getId()).eq("user_id", currentUser.getId()));

            if (taskRecord == null || !Objects.equals(taskRecord.getStatus(), StatusEnum.FINISHED.key())) {
                taskRecord = taskRecord != null ? taskRecord : new UserTaskRecord().setTaskId(task.getId())
                        .setUserId(currentUser.getId());

                taskRecord.setStatus(StatusEnum.FINISHED.key());
                taskRecord.setCompleteTime(new Date());

                if (this.saveOrUpdate(taskRecord)) {
                    return this.getReward(task.getId(), currentUser.getId());
                }
            }

        }
        return false;
    }

    @Override
    public boolean doWelfareTasks(TaskCodeEnum taskCodeEnum, String uId) {
        Task task = taskService.getOne(new QueryWrapper<Task>().eq("task_type",
                taskCodeEnum.getTaskType().key()).eq("task_code",
                taskCodeEnum.key()).eq("status", StatusEnum.ENABLE.key()));

        if (task != null) {
            boolean updated = this.addOrUpdate(new UserTaskRecord().setTaskId(task.getId())
                    .setStatus(StatusEnum.FINISHED.key()).setUserId(uId));

            if (updated) {
                return this.getReward(task.getId(), uId);
            }
        }
        return false;
    }


    @Override
    public boolean doDailyTasks(TaskCodeEnum taskCodeEnum, String uId) {
        Task task = taskService.getOne(new QueryWrapper<Task>().eq("task_type",
                taskCodeEnum.getTaskType().key()).eq("task_code",
                taskCodeEnum.key()).eq("status", StatusEnum.ENABLE.key()));

        if (task != null) {
            //每日任务完成次数上限
            Integer clicksMax = task.getDailyCeiling();
            clicksMax = clicksMax == null ? 3 : clicksMax;

            String cacheKey = VideoContants.CACHE_TODAY_AD_CLICK_COUNT + uId;
            int count = 0;
            if (cache.hasKey(cacheKey)) {
                count = (Integer) cache.get(cacheKey);
            }
            if (count < clicksMax) {
                //有效任务 增加相应观影次数
                this.getReward(task.getId(), uId);

                if (++count == clicksMax) {
                    //当日完成次数达到上限 变更用户任务状态
                    this.addOrUpdate(new UserTaskRecord().setTaskId(task.getId())
                            .setStatus(StatusEnum.FINISHED.key()).setUserId(uId));
                }
                //设置每日任务 已完成次数缓存时间为当天 23:59:59.99999
                cache.set(cacheKey, count, DateUtil.getSurplusSecondOfToday());
            }

            return true;

        }

        return false;
    }

    @Override
    public boolean doPromotionTasks(User user) {
        //查询当前推广人数对应的推广任务
        Task taskBySpread = taskService.findTaskBySpread(user.getRecommendCount());
        if (taskBySpread != null) {
            UserTaskRecord taskRecord = this.getOne(new QueryWrapper<UserTaskRecord>().eq("user_id", user.getId())
                    .eq("task_id", taskBySpread.getId()));

            //判断是否领取过该推广任务的任务奖励
            if (taskRecord == null || Objects.equals(taskRecord.getStatus(), StatusEnum.UNFINISHED.key())) {

                taskRecord = taskRecord != null ? taskRecord : new UserTaskRecord().setTaskId(taskBySpread.getId())
                        .setUserId(user.getId());

                taskRecord.setStatus(StatusEnum.FINISHED.key());
                taskRecord.setCompleteTime(new Date());

                if (this.saveOrUpdate(taskRecord)) {
                    return this.getReward(taskBySpread.getId(), user.getId());
                }
            }
        }
        return false;
    }


    @Override
    public boolean toDoClickAd(String uId) {
        return this.doDailyTasks(TaskCodeEnum.CLICK_AD, uId);
    }

    @Override
    public boolean getReward(Integer taskId, String uId) {
        Task task = taskService.getById(taskId);
        User user = userService.getById(uId);
        if (task == null || user == null) {
            return false;
        }

        if (!Objects.equals(task.getTaskType(), TaskTypeEnum.DAILY_TASKS.key())) {
            user.setAwardSaveCount(user.getAwardSaveCount() + task.getDownloadCount());
            user.setAwardViewingCount(user.getAwardViewingCount() + task.getViewingCount());
        }

       /* user.setViewingCount(user.getViewingCount() < 0 ? -1 : (user.getViewingCount() + task.getViewingCount()));
        user.setSaveCount(user.getSaveCount() < 0 ? -1 : (user.getSaveCount() + task.getDownloadCount()));*/
        user.setViewingCount(user.getViewingCount() + task.getViewingCount());
        user.setSaveCount(user.getSaveCount() + task.getDownloadCount());
        return userService.doUpdateUser(user);
    }

}
