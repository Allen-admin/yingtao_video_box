package com.k365.user_service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.k365.video_base.model.dto.UserViewingRecordDTO;
import com.k365.video_base.model.po.VUserViewingRecord;

import java.util.List;

/**
 * <p>
 * VIEW 服务类
 * </p>
 *
 * @author Gavin
 * @since 2019-09-02
 */
public interface VUserViewingRecordService extends IService<VUserViewingRecord> {

    /**
     * 查询用户观影记录
     */
    List<VUserViewingRecord> findAll(UserViewingRecordDTO userViewingRecordDTO);

}
