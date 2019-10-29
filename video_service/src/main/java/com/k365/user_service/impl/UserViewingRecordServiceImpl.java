package com.k365.user_service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.k365.video_base.common.UserContext;
import com.k365.user_service.UserViewingRecordService;
import com.k365.user_service.VUserViewingRecordService;
import com.k365.video_base.mapper.UserViewingRecordMapper;
import com.k365.video_base.model.dto.UserViewingRecordDTO;
import com.k365.video_base.model.po.User;
import com.k365.video_base.model.po.UserViewingRecord;
import com.k365.video_base.model.po.VUserViewingRecord;
import com.k365.video_base.model.vo.VideoBasicInfoVO;
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
 * @author All
 * @since 2019-07-17
 */
@Service
public class UserViewingRecordServiceImpl extends ServiceImpl<UserViewingRecordMapper, UserViewingRecord> implements UserViewingRecordService {

    @Autowired
    private VUserViewingRecordService vUserViewingRecordService;

    @Override
    public List<VideoBasicInfoVO> findPage(UserViewingRecordDTO userViewingRecordDTO) {
        User currentUser = UserContext.getCurrentUser();
        userViewingRecordDTO.setUserId(currentUser.getId());

        List<VideoBasicInfoVO> result = new ArrayList<>();
        List<VUserViewingRecord> records = vUserViewingRecordService.findAll(userViewingRecordDTO);
        if (!ListUtils.isEmpty(records)) {
            records.forEach(vuvr -> result.add(new VideoBasicInfoVO().setId(vuvr.getVId()).setCover(vuvr.getVCover())
                    .setTitle(vuvr.getVTitle()).setPlaySum(vuvr.getVPlaySum()).setIsVip(vuvr.getVIsVip()).setTimeLen(vuvr.getVTimeLen())
                    .setCreateDate(vuvr.getVCreateDate())));
        }
        return result;
    }

    @Override
    public void removeByUId(String uId) {
        this.remove(new UpdateWrapper<UserViewingRecord>().eq("user_id", uId));
    }

    @Override
    public List<UserViewingRecord> findAll() {
        User currentUser = UserContext.getCurrentUser();

        return this.list(new QueryWrapper<UserViewingRecord>()
                .eq("user_id", currentUser.getId()).orderByDesc("record_time"));

    }

    @Override
    public void add(String videoId) {
        User currentUser = UserContext.getCurrentUser();
        List<UserViewingRecord> list = this.list(new QueryWrapper<UserViewingRecord>()
                .eq("user_id", currentUser.getId()).eq("video_id", videoId).orderByDesc("record_time"));

        if (!ListUtils.isEmpty(list)) {
            UserViewingRecord userViewingRecord = list.get(0);
            userViewingRecord.setRecordTime(new Date());
            this.updateById(userViewingRecord);
            return;
        }

        UserViewingRecord userViewingRecord = new UserViewingRecord().setRecordTime(new Date())
                .setVideoId(videoId).setUserId(currentUser.getId());

        this.save(userViewingRecord);
    }


}
