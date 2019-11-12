package com.k365.user_service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.k365.video_base.model.dto.UserDTO;
import com.k365.video_base.model.po.User;
import com.k365.video_base.model.so.UserSO;
import com.k365.video_base.model.vo.*;
import com.k365.video_common.constant.AppTypeEnum;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author Gavin
 * @since 2019-07-17
 */
public interface UserService extends IService<User> {

    /**
     * 游客进入自动注册
     */
    Map<String,Object> visitorAutoRegister(ServletRequest request, UserDTO userDTO);

    /**
     * 发送短信验证码
     */
    void sendVerifyCode(String mobile);

    /**
     * 查询手机号是否存在
     */
    boolean mobileIsExists(String mobile);

    /**
     * 重新发送验证码
     */
//    void resendVerifyCode(String mobile);

    /**
     * 拨打语音验证码
     */
    void callVoiceVerifyCode(String mobile);

    /**
     * 获取当前用户
     */
    User getCurrentUser(ServletRequest request, ServletResponse response);

    /**
     * 获取用户详情信息
     */
    UserInfoVO getUserInfo();

    /**
     * 查询所用用户的信息
     */
    BaseListVO<UserVO> findAll(UserDTO userDTO);

    /**
     * 根据id查询用户信息
     */
    UserVO findById(String id);

    /**
     * 根据id删除
     */
    void remove(String id);

    /**
     * 后台管理修改用户信息
     */
    boolean updateUserById(User user);

    /**
     * 执行修改用户信息
     */
    boolean doUpdateUser(User user);

    /**
     * 前台修改用户信息
     */
    boolean updateUser(User user);

    /**
     * 跟据用户id修改用户等级
     */
    void updateLevelByUId(String id, Integer level);

    /**
     * 修改用户头像
     */
    Boolean uploadPortrait(String imgPath);

    /**
     * 用户绑定手机号
     */
    UserVO bindPhone(String phone, String verifyCode);

    /**
     * 推广玩家注册
     */
    void spreadRegister(String spreadCode,String registerChannel);

    /**
     * 查询推广列表
     */
    List<SpreadRecordVO> spreadRecordList();

    /**
     * 生成推广二维码
     */
    String createSpreadQrcodeImg(String uId,String spreadQrcodeUrl,AppTypeEnum appType);

    /**
     * 获取用户观影信息
     */
    VideoViewingInfoVO getViewingCount();

    /**
     * 模糊查詢
     */
    BaseListVO<UserVO> search(UserSO userSO);

    /**
     * 验证推广二维码地址是否正确
     */
    boolean verifySpreadQr(String spreadQrcodeUrl, AppTypeEnum appType);

    /**
     * 查询结束时间
     * @param
     */
    void EndTime();


}
