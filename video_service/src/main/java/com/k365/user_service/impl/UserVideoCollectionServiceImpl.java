package com.k365.user_service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.k365.user_service.UserVideoCollectionService;
import com.k365.user_service.VUserVideoCollectionService;
import com.k365.video_base.common.UserContext;
import com.k365.video_base.mapper.UserVideoCollectionMapper;
import com.k365.video_base.model.dto.UserActionAnaylzeDTO;
import com.k365.video_base.model.dto.VUserVideoCollectionDTO;
import com.k365.video_base.model.po.User;
import com.k365.video_base.model.po.UserVideoCollection;
import com.k365.video_base.model.vo.VUserVideoCollectionVO;
import com.k365.video_base.model.vo.VideoBasicInfoVO;
import com.k365.video_common.exception.ResponsiveException;
import com.k365.video_service.UserActionAnalyzeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.ListUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Gavin
 * @since 2019-08-29
 */
@Service
public class UserVideoCollectionServiceImpl extends ServiceImpl<UserVideoCollectionMapper, UserVideoCollection> implements UserVideoCollectionService {

    @Autowired
    private VUserVideoCollectionService vUserVideoCollectionService;
    @Autowired
    private UserActionAnalyzeService userActionAnalyzeService;

    @Override
    public List<VideoBasicInfoVO> getPage(VUserVideoCollectionDTO dto) {
        User currentUser = UserContext.getCurrentUser();
        dto.setUId(currentUser.getId());
        List<VUserVideoCollectionVO> vuvcList =
                vUserVideoCollectionService.findByUidOrVid(dto);

        List<VideoBasicInfoVO> result = new ArrayList<>();
        if (!ListUtils.isEmpty(vuvcList)) {
            vuvcList.forEach(vuvc -> result.add(new VideoBasicInfoVO().setCreateDate(vuvc.getVCreateDate())
                    .setTimeLen(vuvc.getVTimeLen()).setPlaySum(vuvc.getVPlaySum()).setIsVip(vuvc.getVIsVip()).setTitle(vuvc.getVTitle())
                    .setCover(vuvc.getVCover()).setId(vuvc.getVId())));
        }
        return result;
    }

    @Override
    public void addCollection(String vId) {
        if (hasCollection(vId)) {
            throw new ResponsiveException("视频已收藏");
        }

        User currentUser = UserContext.getCurrentUser();
        this.save(UserVideoCollection.builder().videoId(vId).userId(currentUser.getId()).createDate(new Date()).build());

        //调用用户行为分析接口
        UserActionAnaylzeDTO userActionAnaylzeDTO = new UserActionAnaylzeDTO();
        userActionAnaylzeDTO.setVideoId(vId);
        userActionAnaylzeDTO.setMacAddr(currentUser.getMacAddr());
        userActionAnalyzeService.add(userActionAnaylzeDTO, 3);
    }

    @Override
    public boolean hasCollection(String vId) {
        User currentUser = UserContext.getCurrentUser();
        List<UserVideoCollection> list = this.list(new QueryWrapper<UserVideoCollection>().eq("video_id", vId)
                .eq("user_id", currentUser.getId()));
        if (ListUtils.isEmpty(list)) {
            return false;
        }
        return true;
    }


    @Override
    public void removeByVidOrUId(UserVideoCollection userVideoCollection) {
        UpdateWrapper<UserVideoCollection> updateWrapper = new UpdateWrapper<>();

        if (userVideoCollection.getVideoId() != null) {
            updateWrapper.eq("video_id", userVideoCollection.getVideoId());
        }

        if (StringUtils.isNotBlank(userVideoCollection.getUserId())) {
            updateWrapper.eq("user_id", userVideoCollection.getUserId());
        }
        this.remove(updateWrapper);
    }
}
