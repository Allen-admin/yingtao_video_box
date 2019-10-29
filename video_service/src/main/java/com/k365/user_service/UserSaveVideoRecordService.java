package com.k365.user_service;

import com.k365.video_base.model.dto.UserSaveVideoRecordDTO;
import com.k365.video_base.model.po.UserSaveVideoRecord;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Gavin
 * @since 2019-07-17
 */
public interface UserSaveVideoRecordService extends IService<UserSaveVideoRecord> {

    /**
     * 查看下载记录
     */
    List<UserSaveVideoRecord> find(UserSaveVideoRecordDTO userSaveVideoRecordDTO);

    /**
     * 根据用户id删除记录
     */
    void removeByUserId(String id);


}
