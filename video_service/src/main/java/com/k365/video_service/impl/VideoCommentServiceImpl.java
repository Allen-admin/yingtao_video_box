package com.k365.video_service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.k365.user_service.UserService;
import com.k365.video_base.common.UserContext;
import com.k365.video_base.mapper.VideoCommentMapper;
import com.k365.video_base.model.dto.VideoCommentDTO;
import com.k365.video_base.model.po.User;
import com.k365.video_base.model.po.VideoComment;
import com.k365.video_base.model.so.VideoCommentSO;
import com.k365.video_base.model.vo.BaseListVO;
import com.k365.video_common.constant.StatusEnum;
import com.k365.video_common.exception.ResponsiveException;
import com.k365.video_service.VideoCommentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.ListUtils;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Gavin
 * @since 2019-07-17
 */
@Service
public class VideoCommentServiceImpl extends ServiceImpl<VideoCommentMapper, VideoComment> implements VideoCommentService {

    @Autowired
    private UserService userService;

    @Override
    public BaseListVO<VideoComment> findAll(VideoCommentDTO videoCommentDTO) {
        IPage<VideoComment> page = this.page(new Page<VideoComment>().setCurrent(videoCommentDTO.getPage()).setSize(videoCommentDTO.getPageSize()),
                new QueryWrapper<VideoComment>().orderByDesc("time"));
        return new BaseListVO<VideoComment>().setTotal(page.getTotal()).setList(page.getRecords());
    }

    @Override
    public BaseListVO<VideoComment> findByVIdOrUId(VideoCommentDTO videoCommentDTO) {
        QueryWrapper<VideoComment> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(videoCommentDTO.getUserId())) {
            queryWrapper.eq("user_id", videoCommentDTO.getUserId());
        }
        if (videoCommentDTO.getVideoId() != null) {
            queryWrapper.eq("video_id", videoCommentDTO.getVideoId());
        }

        IPage<VideoComment> page = this.page(new Page<VideoComment>().setCurrent(videoCommentDTO.getPage())
                .setSize(videoCommentDTO.getPageSize()), queryWrapper);

        return new BaseListVO<VideoComment>().setTotal(page.getTotal()).setList(page.getRecords());
    }

    @Override
    public BaseListVO<VideoComment> search(VideoCommentDTO videoCommentDTO) {
        if (StringUtils.isNotBlank(videoCommentDTO.getUserId()) || videoCommentDTO.getVideoId() != null) {
            return findByVIdOrUId(videoCommentDTO);
        }

        String searchWords = "";
        if(StringUtils.isNotBlank(videoCommentDTO.getUserNickname())){
            searchWords = StringUtils.join(videoCommentDTO.getUserNickname(),"*");

        }else if(StringUtils.isNotBlank(videoCommentDTO.getVideoTitle())){
            searchWords = StringUtils.join(videoCommentDTO.getVideoTitle(),"*");

        }else if(StringUtils.isNotBlank(videoCommentDTO.getContent())){
            searchWords = StringUtils.join(videoCommentDTO.getContent(),"*");

        }

        VideoCommentSO videoCommentSO = VideoCommentSO.builder().searchValue(searchWords).build();
        videoCommentSO.setPage((videoCommentDTO.getPage() - 1) * videoCommentDTO.getPageSize()).setPageSize(videoCommentDTO.getPageSize());
        List<VideoComment> videoCommentList = this.baseMapper.searchVideoComment(videoCommentSO);

        BaseListVO<VideoComment> result = new BaseListVO<>();
        if(!ListUtils.isEmpty(videoCommentList)){
            result.setTotal(videoCommentList.get(0).getTotal());
        }

        return result.setList(videoCommentList);
    }

    @Override
    public void addComment(VideoComment videoComment) {
        User currentUser = UserContext.getCurrentUser();
        if (StringUtils.isBlank(currentUser.getPhone())) {
            throw new ResponsiveException("绑定手机号后才能发表评论哦！");
        }

        videoComment.setStatus(StatusEnum.ENABLE.key());
        videoComment.setTime(new Date());
        videoComment.setUserId(currentUser.getId());
        videoComment.setUserIcon(currentUser.getUserIcon());
        videoComment.setUserNickname(currentUser.getNickname());
        videoComment.setVideoTitle(videoComment.getVideoTitle());
        this.save(videoComment);
    }

    @Override
    public void removeByVideoId(String id) {
        this.remove(new UpdateWrapper<VideoComment>().eq("video_id", id));
    }

    @Override
    public List<VideoComment> getPageByVId(VideoCommentDTO videoCommentDTO) {
        List<VideoComment> records = this.page(new Page<VideoComment>().setCurrent(videoCommentDTO.getPage())
                .setSize(videoCommentDTO.getPageSize()), new QueryWrapper<VideoComment>().eq("status", StatusEnum.ENABLE.key())
                .eq("video_id", videoCommentDTO.getVideoId()).orderByDesc("time")).getRecords();
        //通过userid查询用户   查询用户头像和姓名
        for(VideoComment videoComment:records){
            User user = userService.getById(videoComment.getUserId());
            if(!StringUtils.isEmpty(user.getNickname())){
                videoComment.setUserNickname(user.getNickname());
            }
            if(!StringUtils.isEmpty(user.getUserIcon())){
                videoComment.setUserIcon(user.getUserIcon());
            }
        }



        return records;
    }

}
