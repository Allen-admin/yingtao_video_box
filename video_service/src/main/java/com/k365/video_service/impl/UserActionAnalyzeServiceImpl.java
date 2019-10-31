package com.k365.video_service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.k365.video_base.mapper.UserActionAanlyzeMapper;
import com.k365.video_base.model.dto.UserActionAnaylzeDTO;
import com.k365.video_base.model.po.UserActionAnalyze;
import com.k365.video_base.model.po.VideoLabelVideo;
import com.k365.video_service.UserActionAnalyzeService;
import com.k365.video_service.VideoLabelVideoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * <p>
 * 用户行为分析服务实现类
 * </p>
 *
 * @author Gavin
 * @since 2019-07-17
 */
@Service
@Slf4j
public class UserActionAnalyzeServiceImpl extends ServiceImpl<UserActionAanlyzeMapper, UserActionAnalyze> implements UserActionAnalyzeService {


    @Autowired
    private VideoLabelVideoService videoLabelVideoService;


    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public void add(UserActionAnaylzeDTO userActionAnaylzeDTO, int actionType) {

        System.out.println("add user action anaylze:" + actionType);

        //1.根据videoId获取videlLableList
        String videoID = userActionAnaylzeDTO.getVideoId();
        List<VideoLabelVideo> videoLabelVideoList = videoLabelVideoService.getVideoLableVideosByVideoId(videoID);

        //2.组装待新增数据userActionAnaylzeList
        List<UserActionAnalyze> userActionAnalyzeList = null;
        if (!videoLabelVideoList.isEmpty() && videoLabelVideoList.size() > 0) {

            userActionAnalyzeList = new ArrayList<>();

            for (int i = 0; i < videoLabelVideoList.size(); i++) {

                //组装单个元素userActionAnalyze
                UserActionAnalyze userActionAnalyze = new UserActionAnalyze();

                userActionAnalyze.setMacAddr(userActionAnaylzeDTO.getMacAddr());
                userActionAnalyze.setVideoLabelId(videoLabelVideoList.get(i).getVideoLabelId());
                userActionAnalyze.setCrttime(new Date());

                //添加进待新增数据库的userActionAnaylzeList
                userActionAnalyzeList.add(userActionAnalyze);
            }
        }

        //3.根据macAddr查询user_action_anaylze获取list
        List<UserActionAnalyze> userActionAnalyzeListOld = this.findUserActionAnaylzeListByMacAddr(userActionAnaylzeDTO.getMacAddr());

        if (!userActionAnalyzeListOld.isEmpty() && userActionAnalyzeListOld.size() > 0) {
            if (userActionAnalyzeListOld.size() > 50 && userActionAnalyzeListOld.size() > userActionAnalyzeList.size()) {
                //删除集合里（userActionAnalyzeList.size()）条最旧的数据
                for (int i = 0; i <= userActionAnalyzeList.size(); i++) {
                    userActionAnalyzeListOld.remove(i);
                }
                //根据macAddr删除数据库表里的所有数据
                if (this.remove(new UpdateWrapper<UserActionAnalyze>().in("mac_addr", userActionAnaylzeDTO.getMacAddr()))) {
                    //插入
                    this.saveBatch(userActionAnalyzeListOld);
                }
            }
        }

        //4.批量新增
        if (actionType > 0) {
            for (int i = 0; i <= actionType; i++) {
                System.out.println("user action anaylze data :"+userActionAnalyzeList);
                this.saveOrUpdateBatch(userActionAnalyzeList);
            }
        }
    }

    /**
     * 根据mac_addr查询并按照crttime降序排序
     *
     * @param macAddr
     * @return
     */
    @Override
    public List<UserActionAnalyze> findUserActionAnaylzeListByMacAddr(String macAddr) {
        return this.list(
                new QueryWrapper<UserActionAnalyze>()
                        .eq("mac_addr", macAddr).orderByDesc("crttime"));
    }
}
