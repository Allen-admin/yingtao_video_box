package com.k365.user_service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.k365.video_base.common.UserContext;
import com.k365.video_base.model.dto.UserSaveVideoRecordDTO;
import com.k365.video_base.model.po.User;
import com.k365.video_base.model.po.UserSaveVideoRecord;
import com.k365.video_base.mapper.UserSaveVideoRecordMapper;
import com.k365.user_service.UserSaveVideoRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Gavin
 * @since 2019-07-17
 */
@Service
public class UserSaveVideoRecordServiceImpl extends ServiceImpl<UserSaveVideoRecordMapper, UserSaveVideoRecord> implements UserSaveVideoRecordService {

    @Override
    public  List<UserSaveVideoRecord> find(UserSaveVideoRecordDTO userSaveVideoRecordDTO){
        User currentUser = UserContext.getCurrentUser();

        List<UserSaveVideoRecord> list=this.page(new Page<UserSaveVideoRecord>().
                setSize(userSaveVideoRecordDTO.getPageSize()).setCurrent(userSaveVideoRecordDTO.getPage()),
                new QueryWrapper<UserSaveVideoRecord>().eq("user_id", currentUser.getId()).
                        orderByAsc("record_time")).getRecords();
        return list;
    }

    @Override
    public void removeByUserId(String uId) {

        this.remove(new UpdateWrapper<UserSaveVideoRecord>().eq("user_id",uId));
    }


}
