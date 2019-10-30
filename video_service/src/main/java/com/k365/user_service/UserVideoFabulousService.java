package com.k365.user_service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.k365.video_base.model.po.UserVideoFabulous;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author Gavin
 * @since 2019-08-29
 */
public interface UserVideoFabulousService extends IService<UserVideoFabulous> {


    /**
     * 视频点赞
     */
    void addFabulous(String vid);


    /**
     * 根据视频id查询是否点赞过
     */
    boolean hasFabulous(String vid);


    /**
     * 根据用户id或根据视频id删除
     */
    void removeByVidOrUId(UserVideoFabulous userVideoFabulous);

}
