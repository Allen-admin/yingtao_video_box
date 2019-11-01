package com.k365.video_service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.k365.video_base.model.dto.UserActionAnaylzeDTO;
import com.k365.video_base.model.po.UserActionAnalyze;

import java.util.List;

/**
 * @author Gavin
 * @date 2019/10/31 9:42
 * @description：
 */
public interface UserActionAnalyzeService extends IService<UserActionAnalyze> {
    /**
     * 新增
     */
    void add(UserActionAnaylzeDTO userActionAanylzeDTO,int ationType);

    /**
     * 根据mac_addr查询批量数据
     */
    List<UserActionAnalyze> findUserActionAnaylzeListByMacAddr(String macAddr);

}
