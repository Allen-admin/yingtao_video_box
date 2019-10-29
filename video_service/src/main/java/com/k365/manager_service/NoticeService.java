package com.k365.manager_service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.k365.video_base.common.AppDisplayTypeEnum;
import com.k365.video_base.model.dto.NoticeDTO;
import com.k365.video_base.model.po.Notice;
import com.k365.video_base.model.vo.NoticeVO;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author Gavin
 * @since 2019-07-17
 */
public interface NoticeService extends IService<Notice> {

    /**
     * 新增系统公告
     */
    void add(NoticeDTO noticeDTO);

    /**
     * 修改系统公告
     */
    void update(NoticeDTO noticeDTO);

    /**
     * 删除系统公告
     */
    void remove(Integer id);

    /**
     * 根据类型查询系统公告
     */
    List<NoticeVO> findListByType(AppDisplayTypeEnum adtEnum,Integer appType);

    /**
     * 查询所有公告
     */
    List<Notice> findAll(Integer appType);

}
