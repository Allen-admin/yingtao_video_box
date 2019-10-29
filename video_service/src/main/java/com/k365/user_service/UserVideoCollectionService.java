package com.k365.user_service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.k365.video_base.model.dto.VUserVideoCollectionDTO;
import com.k365.video_base.model.po.UserVideoCollection;
import com.k365.video_base.model.vo.VUserVideoCollectionVO;
import com.k365.video_base.model.vo.VideoBasicInfoVO;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author Gavin
 * @since 2019-08-29
 */
public interface UserVideoCollectionService extends IService<UserVideoCollection> {

    /**
     * 分页查询用户收藏列表
     */
    List<VideoBasicInfoVO> getPage(VUserVideoCollectionDTO dto);

    /**
     * 收藏视频
     */
    void addCollection(String id);

    /**
     * 根据视频id查询是否收藏过
     */
    boolean hasCollection(String vId);

    /**
     * 根据用户id或根据视频id删除
     */
    void removeByVidOrUId(UserVideoCollection userVideoCollection);
}
