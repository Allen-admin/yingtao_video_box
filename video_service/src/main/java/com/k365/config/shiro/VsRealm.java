package com.k365.config.shiro;

import com.k365.config.jwt.JwtToken;
import com.k365.manager_service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author Gavin
 * @date 2019/7/3 15:39
 * @description：
 */
@Slf4j
public class VsRealm extends AuthorizingRealm {

    @Autowired(required = false)
    private SysUserService userService;

    @Autowired(required = false)
    private CacheManager cacheManager;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {

        /* log.info("Shiro权限验证执行");
        String username = JwtUtil.getUsername(principals.toString());

        if (StringUtils.isNotBlank(username)) {
            SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
            //根据用户名查询用户信息
            SysUser findUser = sysUserService.findUserByName(username, true);
            if (findUser != null) {
                if (findUser.getRoles() != null) {
                    findUser.getRoles().forEach(role -> {
                        info.addRole(role.getName());
                        if (role.getResources() != null) {
                            role.getResources().forEach(v -> {
                                if (!"".equals(v.getPermission().trim())) {
                                    info.addStringPermission(v.getPermission());
                                }
                            });
                        }
                    });
                }
                return info;
            }
        }*/

        throw new DisabledAccountException("用户信息异常，请重新登录！");
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth) throws AuthenticationException {

       /* JwtToken token = (JwtToken) auth;
        SysUser sysUser;
        String username = null;
        try {
            username = token.getUsername() != null ?
                    token.getUsername() : JwtUtil.getUsername(token.getToken());

            sysUser = sysUserService.selectOne(new EntityWrapper<SysUser>()
                    .eq("username", username)
                    .setSqlSelect("id,username,status,password"));
        } catch (Exception e) {
            throw new GeneralException(String.format("用户[%s]尝试登陆失败",username),e);
        }

        if (sysUser == null) {
            throw new ResponsiveException(ExCodeMsgEnum.LOGIN_FAIL);
        }
        if (sysUser.getStatus() != null && UserStatusEnum.NORMAL.code() != sysUser.getStatus()) {
            UserStatusEnum statusName = UserStatusEnum.getUserStatusName(sysUser.getStatus());
            throw new ResponsiveException("当前用户已" + statusName.getName() + "，暂时无法登陆！");
        }

        if (token.getUsername() == null) token.setUsername(sysUser.getUsername());
        String sign = JwtUtil.sign(sysUser.getId(), sysUser.getUsername(), sysUser.getPassword());
        if (token.getToken() == null) token.setToken(sign);
        token.setUid(sysUser.getId());
        return new SimpleAuthenticationInfo(token, sysUser.getPassword(), sysUser.getId());*/
        return null;

    }

    public void clearAuthByUserId(String uid, Boolean author, Boolean out) {

        //获取所有session
        Cache<Object, Object> cache = cacheManager
                .getCache(VmRealm.class.getName() + ".SysResource");
        cache.remove(uid);

    }

    public void clearAuthByUserIdCollection(List<String> userList, Boolean author, Boolean out) {

        Cache<Object, Object> cache = cacheManager
                .getCache(VmRealm.class.getName() + ".authorizationCache");
        userList.forEach(cache::remove);

    }


}
