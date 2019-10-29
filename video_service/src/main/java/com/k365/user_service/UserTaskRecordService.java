package com.k365.user_service;

import com.k365.video_base.common.TaskCodeEnum;
import com.k365.video_base.model.po.User;
import com.k365.video_base.model.po.UserTaskRecord;
import com.baomidou.mybatisplus.extension.service.IService;
import com.k365.video_base.model.vo.UserTaskRecordVO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Gavin
 * @since 2019-07-17
 */
public interface UserTaskRecordService extends IService<UserTaskRecord> {

    /**
     * 查询特别推荐任务(三個未完成的任務)
     */
    List<UserTaskRecordVO> findRecommend();

    /**
     * 查询所有任务
     */
    Map<String,List<UserTaskRecordVO>> findAll();

    /**
     * 新增或修改用户任务记录
     */
    boolean addOrUpdate(UserTaskRecord userTaskRecord);

    /**
     * 完成福利任务首次上产头像
     */
    boolean toDoFristUpPortrait(String uId);

    /**
     * 完成福利任务首次绑定手机号
     */
    boolean toDoFristBindPhone(String uId);

    /**
     *  完成福利任务
     */
    boolean doWelfareTasks(TaskCodeEnum taskCodeEnum, String uId);

    /**
     *  完成每日任务
     */
    boolean doDailyTasks(TaskCodeEnum taskCodeEnum, String uId);

    /**
     * 完成推广任务
     */
    boolean doPromotionTasks(User user);

    /**
     * 仅第一次生效任务
     */
    boolean onlyFirstEffectiveTask(TaskCodeEnum taskCodeEnum);

    /**
     * 福利任务保存二维码
     */
    boolean toDoSaveQrImg();

    /**
     * 完成每日任务，点击广告
     */
    boolean toDoClickAd(String uid);

    /**
     * 完成任务领取相应奖励
     */
    boolean getReward(Integer taskId,String uId);
}
