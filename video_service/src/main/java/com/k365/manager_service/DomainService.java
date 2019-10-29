package com.k365.manager_service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.k365.video_base.model.dto.DomainDTO;
import com.k365.video_base.model.po.Domain;
import com.k365.video_base.model.vo.BaseListVO;
import com.k365.video_base.model.vo.DomainVO;
import com.k365.video_common.constant.AppTypeEnum;

import javax.servlet.ServletRequest;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Gavin
 * @since 2019-07-17
 */
public interface DomainService extends IService<Domain> {

    /**
     * 查询所有可用域名·
     */
    List<DomainVO> findUsable(AppTypeEnum appType);

    /**
     * 新增域名
     */
    void add(DomainDTO domainDTO);

    /**
     * 修改域名
     */
    void update(DomainDTO domainDTO);

    /**
     * 分页查询域名列表
     */
    BaseListVO<Domain> getByPage(DomainDTO domainDTO,ServletRequest request);

    /**
     * 删除域名
     */
    void remove(Integer id);

    /**
     * 获取推广域名信息
     */
    Map<String,Object> getSpreadDomain();

    /**
     *  获取域名
     * @return
     */
    String getDomain(ServletRequest request);

    /**
     *  获取app图片域名
     * @return
     */
    String getAppPicDomain();
}
