package com.k365.user_service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.k365.video_base.model.dto.VUserVideoCollectionDTO;
import com.k365.video_base.model.po.VUserVideoCollection;
import com.k365.video_base.model.vo.VUserVideoCollectionVO;

import java.util.List;

/**
 * <p>
 * VIEW 服务类
 * </p>
 *
 * @author Gavin
 * @since 2019-08-29
 */
public interface VUserVideoCollectionService extends IService<VUserVideoCollection> {

    /**
     * 根据用户id分页查询
     */
    List<VUserVideoCollectionVO> findByUidOrVid(VUserVideoCollectionDTO vUserVideoCollectionDTO);

}
