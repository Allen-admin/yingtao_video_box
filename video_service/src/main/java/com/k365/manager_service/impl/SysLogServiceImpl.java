package com.k365.manager_service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.k365.manager_service.SysLogService;
import com.k365.video_base.mapper.SysLogMapper;
import com.k365.video_base.model.po.SysLog;
import com.k365.video_base.model.so.SysLogSO;
import com.k365.video_base.model.vo.BaseListVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Gavin
 * @since 2019-09-11
 */
@Service
public class SysLogServiceImpl extends ServiceImpl<SysLogMapper, SysLog> implements SysLogService {


    @Override
    public BaseListVO<SysLog> search(SysLogSO sysLogSO) {
        QueryWrapper<SysLog> queryWrapper = new QueryWrapper<>();
        if (sysLogSO.getStatus() != null) {
            //状态查询 成功 失败
            queryWrapper.eq("status", sysLogSO.getStatus());
        }
        if (StringUtils.isNotBlank(sysLogSO.getOperate())) {
            //操作功能查询
            queryWrapper.like("operate", sysLogSO.getOperate());
        }
        if (StringUtils.isNotBlank(sysLogSO.getUserIp())) {
            //用户IP查询
            queryWrapper.like("user_ip", sysLogSO.getUserIp());
        }
        if (StringUtils.isNotBlank(sysLogSO.getUsername())) {
            //用户名查询
            queryWrapper.like("username", sysLogSO.getUsername());
        }
        if (StringUtils.isNotBlank(sysLogSO.getAccessPath())) {
            //访问路径查询
            queryWrapper.like("access_path", sysLogSO.getAccessPath());
        }
        if (sysLogSO.getBeginTime() != null && sysLogSO.getEndTime() != null) {
            //按时间筛选
            queryWrapper.between("time", sysLogSO.getBeginTime(), sysLogSO.getEndTime());
        }

        IPage<SysLog> page = this.page(new Page<SysLog>().setCurrent(sysLogSO.getPage()).setSize(sysLogSO.getPageSize()),
                queryWrapper.orderByDesc("time"));

        return new BaseListVO<SysLog>().setTotal(page.getTotal()).setList(page.getRecords());

    }

    @Override
    public void add(SysLog sysLog) {
        this.save(sysLog);
    }
}
