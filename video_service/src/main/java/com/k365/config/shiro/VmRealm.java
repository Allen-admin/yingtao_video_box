package com.k365.config.shiro;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.k365.config.jwt.JwtToken;
import com.k365.video_base.common.SysUserContext;
import com.k365.manager_service.SysUserService;
import com.k365.video_base.model.po.SysUser;
import com.k365.video_common.constant.Constants;
import com.k365.video_common.constant.UserStatusEnum;
import com.k365.video_common.exception.CustomAuthException;
import com.k365.video_common.util.JwtUtil;
import com.k365.video_common.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author Gavin
 * @date 2019/6/21 11:31
 * @description：
 */
@Slf4j
public class VmRealm extends AuthorizingRealm {

    @Autowired(required = false)
    private SysUserService userService;

    @Autowired
    private RedisUtil cache;


    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    /**
     * 检查用户权限
     *
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        JwtToken token = (JwtToken) principals.getPrimaryPrincipal();
        String account = JwtUtil.getAccount(token.getToken());
        SysUser userResource = userService.findUserResourceByUName(account);
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

        //添加角色
        if (!CollectionUtils.isEmpty(userResource.getRoles())) {
            userResource.getRoles().forEach(role -> {
                if (StringUtils.isNotBlank(role.getName()) && role.getStatus() == 1) {
                    info.addRole(role.getName().trim());
                }
            });
            SysUserContext.getCurrentSysUser().setRoles(userResource.getRoles());
        }

        //添加权限
        if (!CollectionUtils.isEmpty(userResource.getResources())) {
            userResource.getResources().forEach(sysResource -> {
                if (StringUtils.isNotBlank(sysResource.getPermitCode()) && BooleanUtils.isTrue(sysResource.getVerification())) {
                    info.addStringPermission(sysResource.getPermitCode().trim());
                }
            });
            SysUserContext.getCurrentSysUser().setResources(userResource.getResources());
        }

        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth)
            throws AuthenticationException {
        JwtToken jwtToken = (JwtToken) auth;

        String account = jwtToken.getUsername();
        //首次登陆
        boolean isFristLogin = true;
        if (StringUtils.isBlank(account)) {
            account = JwtUtil.getAccount(jwtToken.getToken());
            isFristLogin = false;
        }

        if (account == null) {
            throw new UnknownAccountException();
        }

        SysUser sysUser = null;
        /*String sysUserCacheKey = Constants.CACHE_CURRENT_SYS_USER_INFO + account;
        if(cache.hasKey(sysUserCacheKey)){
            sysUser = (SysUser) cache.get(sysUserCacheKey);
        }else{
            sysUser = userService.getOne(new QueryWrapper<SysUser>()
                    .eq("username", account)
                    .select("id,username,status,password"));
            if(sysUser != null) {
                cache.set(sysUserCacheKey, sysUser, JwtUtil.DEFAULT_EXPIRE_TIME);
            }
        }*/

        sysUser = userService.getOne(new QueryWrapper<SysUser>()
                .eq("username", account)
                .select("id,username,status,password"));

        if (sysUser == null) {
            throw new UnknownAccountException();
        }

        if (sysUser.getStatus() != null && UserStatusEnum.NORMAL.code() != sysUser.getStatus()) {
            UserStatusEnum statusName = UserStatusEnum.getUserStatusName(sysUser.getStatus());
            throw new CustomAuthException("当前用户已" + statusName.getName() + "，暂时无法登陆！");
        }

        if (!isFristLogin) {
            jwtToken.setUsername(sysUser.getUsername());
        } else {
            String time = String.valueOf(System.currentTimeMillis());
            String refreshTokenCacheKey = Constants.CACHE_SHIRO_SYS_USER_TOKEN_REFRESH + account;
            String sign = JwtUtil.sign(sysUser.getUsername(), time);
            jwtToken.setToken(sign);
            //缓存用户Token过期时间（Token更新时间）是过期时间的5倍
            cache.set(refreshTokenCacheKey, time, JwtUtil.DEFAULT_EXPIRE_TIME * 5);
        }

        jwtToken.setUid(sysUser.getId());

        if (SysUserContext.getCurrentSysUser() != null) {
            SysUser currentUser = SysUserContext.getCurrentSysUser();
            currentUser.setTryLoginCount(currentUser.getTryLoginCount() + 1);
        } else {
            sysUser.setTryLoginCount(1);
            new SysUserContext(sysUser);
        }
        return new SimpleAuthenticationInfo(jwtToken, sysUser.getPassword(), getName());
    }

    /**
     * 清除所有用户的缓存
     */
    public void clearAuthorizationInfoCache() {
        Cache<Object, AuthorizationInfo> cache = getAuthorizationCache();
        if (cache != null) {
            cache.clear();
        }
    }

    /**
     * 清除指定用户的缓存
     *
     * @param uid
     */
    public void clearAuthorizationInfoCache(String uid) {
        Cache<Object, AuthorizationInfo> cache = getAuthorizationCache();
        cache.remove(uid);
    }


}


