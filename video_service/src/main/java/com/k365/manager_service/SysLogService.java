package com.k365.manager_service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.k365.video_base.model.dto.SysLogDTO;
import com.k365.video_base.model.po.SysLog;
import com.k365.video_base.model.so.SysLogSO;
import com.k365.video_base.model.vo.BaseListVO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Gavin
 * @since 2019-09-11
 */
public interface SysLogService extends IService<SysLog> {

    /**
     * 筛选系统日志
     */
    BaseListVO<SysLog> search(SysLogSO sysLogSO);

    /**
     * 新增系统日志
     */
    void add(SysLog sysLog);

}
