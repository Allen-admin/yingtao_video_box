package com.k365.user_service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.k365.user_service.UserLevelService;
import com.k365.user_service.UserService;
import com.k365.video_base.common.UserContext;
import com.k365.video_base.mapper.UserLevelMapper;
import com.k365.video_base.model.po.User;
import com.k365.video_base.model.po.UserLevel;
import com.k365.video_common.constant.Constants;
import com.k365.video_common.constant.StatusEnum;
import com.k365.video_common.exception.ResponsiveException;
import com.k365.video_common.util.BasicUtil;
import com.k365.video_common.util.DateUtil;
import com.k365.video_common.util.RedisUtil;
import com.k365.video_common.util.RegsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.ListUtils;

import java.util.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Gavin
 * @since 2019-07-17
 */
@Service
public class UserLevelServiceImpl extends ServiceImpl<UserLevelMapper, UserLevel> implements UserLevelService {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisUtil cache;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public void add(UserLevel userLevel) {
        UserLevel level = this.getOne(new QueryWrapper<UserLevel>().eq("level", userLevel.getLevel()));

        if (level != null)
            throw new ResponsiveException("当前层级已存在");

        userLevel.setStatus(StatusEnum.ENABLE.key());

        //查询现有最高等级 currentlyTallestLevel as ctl
        UserLevel ctl = this.getOne(new QueryWrapper<UserLevel>().eq("level", userLevel.getLevel() - 1));

        //根据新等级推广人数动态调整玩家等级
        List<User> userList = userService.list(new QueryWrapper<User>().ge("recommend_count", userLevel.getRecommendCount()));
        if (!ListUtils.isEmpty(userList)) {
            //开启redis事务控制
            cache.multi();
            userList.forEach(user -> {
                //下载次数计算
                int saveCount = BasicUtil.getNumber(user.getSaveCount());
                if (saveCount != -1) {
                    saveCount = saveCount + userLevel.getSaveCount() - ctl.getSaveCount();
                } else {
                    saveCount = BasicUtil.getNumber(userLevel.getSaveCount());
                }

                //观影次数计算
                int viewingCount = BasicUtil.getNumber(user.getViewingCount());
                if (viewingCount != -1) {
                    viewingCount = viewingCount + userLevel.getViewingCount() - ctl.getViewingCount();
                } else {
                    viewingCount = BasicUtil.getNumber(userLevel.getViewingCount());
                }
                user.setSaveCount(saveCount).setViewingCount(viewingCount).setUserLevel(userLevel.getLevel());

                String cacheKey = Constants.CACHE_VISITOR_USER_INFO + user.getId();
                cache.set(cacheKey, user, DateUtil.getSurplusSecondOfToday());
            });
            //刷新修改层级的玩家观影次数
            boolean updated = userService.updateBatchById(userList);
            //更新用户缓存
            if (!updated) {
                cache.discard();
                throw new ResponsiveException("用户等级添加失败");
            } else {
                cache.exec();
            }
        }
        this.save(userLevel);

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public void update(UserLevel userLevel) {
        UserLevel level = this.getById(userLevel.getId());

        if (level == null)
            throw new ResponsiveException("当前层级不存在或已被删除");

        User user2 = new User();
        boolean  readjustLevel = true;
        if (Objects.equals(userLevel.getRecommendCount(), level.getRecommendCount())) {
            readjustLevel = false;
            if (!Objects.equals(userLevel.getSaveCount(), level.getSaveCount())) {
                user2.setSaveCount(userLevel.getSaveCount());
            }
            if (!Objects.equals(userLevel.getViewingCount(), level.getViewingCount())) {
                user2.setViewingCount(userLevel.getViewingCount());
            }
        }


        List<User> userList = new ArrayList<>();
        //当等级门槛降低（推广人数减少）时，需考虑玩家升级
        if(userLevel.getRecommendCount() < level.getRecommendCount()){
            UserLevel lowLevel = this.getOne(new QueryWrapper<UserLevel>().eq("level", level.getLevel() - 1));
            if (lowLevel != null) {
                userList = userService.list(new QueryWrapper<User>().eq("user_level", lowLevel.getLevel()));
            }
        }else{
            userList = userService.list(new QueryWrapper<User>().eq("user_level", level.getLevel()));
        }
        

        if (!ListUtils.isEmpty(userList)) {
            //开启redis事务控制
            cache.multi();
            for(User user : userList){
                //动态调整现有玩家等级
                if(readjustLevel) {
                    UserLevel nowUserLevel = getNewUserLevel(user.getRecommendCount(), userLevel);
                    user2.setUserLevel(nowUserLevel.getLevel()).setViewingCount(nowUserLevel.getViewingCount())
                            .setSaveCount(nowUserLevel.getSaveCount());
                }

                //刷新修改层级的玩家观影次数
                user.setUserLevel(BasicUtil.trinomial(user2.getUserLevel(), user.getUserLevel()))
                        .setViewingCount(BasicUtil.trinomial(user2.getViewingCount(), user.getViewingCount()))
                        .setSaveCount(BasicUtil.trinomial(user2.getSaveCount(), user.getSaveCount()));

                String cacheKey = Constants.CACHE_VISITOR_USER_INFO + user.getId();
                cache.set(cacheKey, user, DateUtil.getSurplusSecondOfToday());
            }

            //刷新修改层级的玩家观影次数
            boolean updated = userService.updateBatchById(userList);
            //更新用户缓存
            if (!updated) {
                cache.discard();
                throw new ResponsiveException("用户等级修改失败");
            } else {
                cache.exec();
            }
        }

        this.updateById(userLevel);
    }

    /**
     * 用户等级要求修改后 重置用户等级（升级，降级）
     *
     * @param levelRecommendCount
     * @return
     */
    private UserLevel getNewUserLevel(Integer levelRecommendCount,UserLevel newLevel) {
        List<UserLevel> list = this.list(new QueryWrapper<UserLevel>().eq("status", StatusEnum.ENABLE.key())
                .orderByAsc("level"));

        UserLevel userLevel = null;
        if (!ListUtils.isEmpty(list) && RegsUtil.valiIsNonnegative(levelRecommendCount)) {
            for (int i = 1; i < list.size(); i++) {
                userLevel = list.get(i);
                if(Objects.equals(userLevel.getLevel(),newLevel.getLevel())){
                    userLevel = newLevel;
                }

                if(levelRecommendCount < userLevel.getRecommendCount()){
                    userLevel = list.get(i-1);
                    break;
                }else if(Objects.equals(levelRecommendCount , userLevel.getRecommendCount()) ||
                        (levelRecommendCount > userLevel.getRecommendCount() && i == list.size() - 1)){
                    break;
                }
            }
        }

        return userLevel;
    }


    @Override
    public void remove(Integer id) {
        this.removeById(id);
    }


    @Override
    public List<UserLevel> findAll() {

        return this.list(new QueryWrapper<UserLevel>().orderByAsc("level"));
    }

    @Override
    public List<UserLevel> findCurrentLevel() {
        User currentUser = UserContext.getCurrentUser();

        return this.list(new QueryWrapper<UserLevel>().eq("status", StatusEnum.ENABLE.key())
                .in("level", new Integer[]{currentUser.getUserLevel(), currentUser.getUserLevel() + 1})
                .orderByAsc("level"));
    }

    @Override
    public UserLevel findLevelBySpread(Integer spreadCount) {
        List<UserLevel> userLevels = this.list(new QueryWrapper<UserLevel>().le("recommend_count", spreadCount).orderByDesc("level"));
        if (!ListUtils.isEmpty(userLevels)) {
            return userLevels.get(0);
        }
        return null;
    }
}
