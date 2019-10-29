package com.k365.manager_service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.k365.video_base.model.dto.SysUserDTO;
import com.k365.video_base.model.po.SysUser;
import com.k365.video_base.model.vo.SysUserInfoVO;
import com.k365.video_base.model.vo.SysUserVO;
import com.k365.video_common.constant.UserStatusEnum;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.List;

/**
 * @author Gavin
 * @date 2019/6/29 21:01
 * @description：
 */
public interface SysUserService extends IService<SysUser> {

    /**
     * 根据用户名查找用户
     */
    SysUser findUserByName(String name);

    /**
     * 分页查询系统用户信息
     */
    List<SysUserVO> getAllUserBySplitPage(SysUserDTO sysUserDTO);

    /**
     * 根据用户名查询用户、所属角色、权限信息
     */
    SysUser findUserResourceByUName(String uName);


    /**
     * 用户登录操作
     */
    void signIn(SysUserDTO sysUserDTO);

    /**
     * 退出登录
     */
    void signOut(ServletRequest request, ServletResponse response);

    /**
     * 检测省份验证码
     */
    boolean checkAuth(SysUserDTO sysUserDTO);


    /**
     * 根据ID查找用户
     */
    SysUserVO findSysUserById(String id);

    /**
     *根据用户id查询用户信息
     */
    SysUserInfoVO findSysUserInfoById(String id);

    /**
     * 修改用户状态
     */
    void statusChangeByUName(String uName,UserStatusEnum status);

    /**
     * 修改用户状态
     */
    void statusChangeByUId(String uId,UserStatusEnum status);


    /**
     * 获取当前登录用户信息
     */
    SysUserVO getCurrentSysUser(ServletRequest request);

    /**
     * 添加用户
     */
    void add(SysUserDTO sysUserDTO);

    /**
     * 删除用户
     */
    void removeUser(String id);

    /**
     * 修改系统用户密码
     */
    void resetPassword(ServletRequest request,ServletResponse response,SysUserDTO sysUserDTO);

    /**
     * 修改系统用户信息
     */
    void update(SysUserDTO sysUserDTO);

    /**
     * 重新生成google身份验证码
     */
    String regenerateAuth();
}
