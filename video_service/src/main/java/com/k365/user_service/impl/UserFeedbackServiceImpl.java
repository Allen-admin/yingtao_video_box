package com.k365.user_service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.k365.video_base.common.UserContext;
import com.k365.video_base.model.dto.UserFeedbackDTO;
import com.k365.video_base.model.po.User;
import com.k365.video_base.model.po.UserFeedback;
import com.k365.video_base.mapper.UserFeedbackMapper;
import com.k365.user_service.UserFeedbackService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.k365.video_base.model.po.VideoLabel;
import com.k365.video_base.model.vo.BaseListVO;
import com.k365.video_common.constant.StatusEnum;
import com.k365.video_common.exception.ResponsiveException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 用户反馈意见表 服务实现类
 * </p>
 *
 * @author Gavin
 * @since 2019-07-17
 */
@Service
public class UserFeedbackServiceImpl extends ServiceImpl<UserFeedbackMapper, UserFeedback> implements UserFeedbackService {

    @Override
    public void add(UserFeedback userFeedback) {
        User currentUser = UserContext.getCurrentUser();

  /*      UserFeedback userFeedback1=this.getOne(new QueryWrapper<UserFeedback>().eq("contact",userFeedback.getContact()));
          if (userFeedback1 != null ){
            throw new ResponsiveException("反馈的意见重复");
        }*/
        userFeedback.setFeedbackUserId(currentUser.getId());
        userFeedback.setStatus(StatusEnum.NOT_ACCEPTED.key());
        userFeedback.setFeedbackTime(new Date());
        this.save(userFeedback);
    }

    @Override
    public void remove(String id) {
        this.removeById(id);
    }

    @Override
    public void updateStatusById(UserFeedback userFeedback) {
        UserFeedback userFeedback2 = this.getById(userFeedback.getId());
        if(userFeedback2 == null)
            throw new ResponsiveException("反馈意见不存在或已被删除");

        this.update(new UpdateWrapper<UserFeedback>().set("status",userFeedback.getStatus()).eq("id",userFeedback.getId()));
    }

    @Override
    public BaseListVO<UserFeedback> find(UserFeedbackDTO userFeedbackDTO) {

        IPage<UserFeedback> page=this.page(new Page<UserFeedback>().setSize(userFeedbackDTO.getPageSize()).setCurrent(userFeedbackDTO.getPage()),
                new QueryWrapper<UserFeedback>().orderByAsc("feedback_time"));

        return  new BaseListVO<UserFeedback>().setTotal(page.getTotal()).setList(page.getRecords());

    }


}
