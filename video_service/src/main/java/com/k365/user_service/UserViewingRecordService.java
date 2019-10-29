package com.k365.user_service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.k365.video_base.model.dto.UserViewingRecordDTO;
import com.k365.video_base.model.po.UserViewingRecord;
import com.k365.video_base.model.vo.VideoBasicInfoVO;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author Allen
 * @since 2019-08-09
 */
public interface UserViewingRecordService extends IService<UserViewingRecord> {

    /**
     * 查询历史记录
     */
    List<VideoBasicInfoVO> findPage(UserViewingRecordDTO userViewingRecordDTO);

    /**
     * 根据用户id删除记录
     */
    void removeByUId(String uId);

    /**
     * 查询所有观看记录
     */
    List<UserViewingRecord> findAll();

    /**
     * 添加用户观影记录
     */
    void add(String videoId);


}
