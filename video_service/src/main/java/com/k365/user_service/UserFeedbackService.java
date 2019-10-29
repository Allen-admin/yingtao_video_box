package com.k365.user_service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.k365.video_base.model.dto.UserFeedbackDTO;
import com.k365.video_base.model.po.UserFeedback;
import com.k365.video_base.model.vo.BaseListVO;

/**
 * <p>
 * 用户反馈意见表 服务类
 * </p>
 *
 * @author Gavin
 * @since 2019-07-17
 */
public interface UserFeedbackService extends IService<UserFeedback> {

    /**
     * 新增用户反馈
     */
    void add(UserFeedback userFeedback);

    /**
     * 删除用户反馈
     */
    void remove(String id);

    /**
     * 根据修改用户反馈状态
     */
    void updateStatusById(UserFeedback userFeedback);

    /**
     * 查询用户反馈
     * @return
     */
    BaseListVO<UserFeedback> find(UserFeedbackDTO userFeedbackDTO);
}
