package com.k365.user_service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.k365.video_base.model.po.UserLevel;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author Gavin
 * @since 2019-07-17
 */
public interface UserLevelService extends IService<UserLevel> {

    /**
     * 新增用户层级
     */
    void add(UserLevel userLevel);

    /**
     * 更新用户层级
     */
    void update(UserLevel userLevel);

    /**
     * 删除用户层级
     */
    void remove(Integer id);

    /**
     * 查询所有玩家等级
     */
    List<UserLevel> findAll();

    /**
     * 获取当前玩家等级及下一个等级
     */
    List<UserLevel> findCurrentLevel();

    /**
     * 查询推广人数所对应的层级
     */
    UserLevel findLevelBySpread(Integer spreadCount);

}
