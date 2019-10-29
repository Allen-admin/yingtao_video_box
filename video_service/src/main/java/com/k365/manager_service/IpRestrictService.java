package com.k365.manager_service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.k365.video_base.model.dto.IpRestrictDTO;
import com.k365.video_base.model.po.IpRestrict;
import com.k365.video_base.model.vo.BaseListVO;

import javax.servlet.ServletRequest;
import java.util.List;

/**
 * <p>
 * ip黑白名单限制表 服务类
 * </p>
 *
 * @author Gavin
 * @since 2019-09-11
 */
public interface  IpRestrictService extends IService<IpRestrict> {

    /**
     * 查询所有有效IP限制
     */
    List<IpRestrict>  findAll();

    /**
     * 分页查询IP限制
     */
    BaseListVO<IpRestrict> getPage(IpRestrictDTO ipRestrictDTO);

    /**
     * 添加IP限制
     */
    void add(IpRestrict ipRestrict);

    /**
     * 修改IP限制
     */
    void update(IpRestrict ipRestrict);

    /**
     * 移出IP限制
     */
    void remove(IpRestrict ipRestrict);

    /**
     * IP拦截检测
     */
    Boolean ipCheck(ServletRequest request);

}
