package com.k365.manager_service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.k365.config.jwt.JwtToken;
import com.k365.config.shiro.AuthAbnormalDecomposer;
import com.k365.global.ShiroService;
import com.k365.manager_service.*;
import com.k365.video_base.common.SysUserContext;
import com.k365.video_base.mapper.SysUserMapper;
import com.k365.video_base.model.dto.SysUserDTO;
import com.k365.video_base.model.po.*;
import com.k365.video_base.model.vo.SysRoleVO;
import com.k365.video_base.model.vo.SysUserInfoVO;
import com.k365.video_base.model.vo.SysUserVO;
import com.k365.video_common.constant.*;
import com.k365.video_common.exception.GeneralException;
import com.k365.video_common.exception.ResponsiveException;
import com.k365.video_common.util.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.ListUtils;
import org.thymeleaf.util.MapUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author Gavin
 * @date 2019/6/29 21:14
 * @description：
 */
@Service
@Slf4j
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser>
        implements SysUserService {

    @Autowired
    private VSysUserRoleResourceService vSysUserRoleResourceService;

    @Autowired
    private SysConfParamService sysConfParamService;

    @Autowired
    private RedisUtil cache;

    @Autowired
    private ShiroService shiroService;

    @Autowired
    private VSysUserRoleService vSysUserRoleService;

    @Autowired
    private SysUserRoleService sysUserRoleService;

    @Override
    public SysUser findUserByName(String name) {
        if (StringUtils.isBlank(name))
            return null;

        return this.getOne(new QueryWrapper<SysUser>().eq("username", name));
    }

    @Override
    public List<SysUserVO> getAllUserBySplitPage(SysUserDTO sysUserDTO) {
        List<SysUser> sysUserList = this.page(new Page<SysUser>()
                        .setSize(sysUserDTO.getPageSize())
                        .setCurrent(sysUserDTO.getPage()),
                new QueryWrapper<SysUser>()
                        .orderByAsc("create_date")).getRecords();

        List<SysUserVO> voList = new ArrayList<>();
        if (ListUtils.isEmpty(sysUserList))
            return voList;

        sysUserList.forEach(sysUser -> {
            SysUserVO vo = new SysUserVO();
            BeanUtils.copyProperties(sysUser, vo);
            voList.add(vo);
        });

        return voList;
    }


    @Override
    public SysUser findUserResourceByUName(String uName) {
        List<VSysUserRoleResource> vSysUserRoleResources =
                vSysUserRoleResourceService.findListByUsername(uName);

        SysUser sysUser = SysUser.builder()
                .roles(new HashSet<>())
                .resources(new HashSet<>()).build();

        if (!ListUtils.isEmpty(vSysUserRoleResources)) {
            Set<String> ids = new HashSet<>();
            vSysUserRoleResources.forEach(vSysUserRoleResource -> {
                if (!ids.contains("RO-" + vSysUserRoleResource.getRoleId())) {
                    //添加角色信息
                    sysUser.getRoles().add(SysRole.builder().name(vSysUserRoleResource.getRoleName())
                            .id(vSysUserRoleResource.getRoleId())
                            .status(vSysUserRoleResource.getRoleStatus())
                            .sort(vSysUserRoleResource.getRoleSort())
                            .build());
                    ids.add("RO-" + vSysUserRoleResource.getRoleId());
                }

                if (!ids.contains("RE-" + vSysUserRoleResource.getResourceId())) {
                    //添加资源信息
                    sysUser.getResources().add(SysResource.builder().permitCode(vSysUserRoleResource.getResourcePermitCode())
                            .id(vSysUserRoleResource.getResourceId())
                            .typeCode(vSysUserRoleResource.getResourceTypeCode())
                            .url(vSysUserRoleResource.getResourceUrl())
                            .verification(vSysUserRoleResource.getResourceVerification())
                            .sort(vSysUserRoleResource.getResourceSort())
                            .build());
                    ids.add("RE-" + vSysUserRoleResource.getResourceId());
                }

                //添加用户信息,只需添加一次
                if (uName.equalsIgnoreCase(vSysUserRoleResource.getUsername()) && StringUtils.isBlank(sysUser.getId())) {
                    sysUser.setUsername(vSysUserRoleResource.getUsername());
                    sysUser.setId(vSysUserRoleResource.getUserId());
                    sysUser.setStatus(vSysUserRoleResource.getUserStatus());
                }

            });
        }
        return sysUser;
    }


    @Override
    public void signIn(SysUserDTO sysUserDTO) {
        if (!this.checkAuth(sysUserDTO))//验证身份份验证码
            throw new ResponsiveException(ExCodeMsgEnum.AUTH_WRONG);

        JwtToken token = JwtToken.builder().username(sysUserDTO.getUsername())
                .password(sysUserDTO.getPassword()).build();

        try {
            Subject subject = SecurityUtils.getSubject();
            subject.login(token);
            if (!subject.isAuthenticated()) {
                throw new ResponsiveException(ExCodeMsgEnum.IDENTITY_HAS_EXPIRED);
            }
        } catch (AuthenticationException e) {
            ExCodeMsgEnum exCodeMsg = AuthAbnormalDecomposer.decoAuthenticationException(e);
            log.info("用户[{}]登陆失败,原因[{}]", sysUserDTO.getUsername(), e.getClass().getName() + " : " + e.getMessage());
            throw new ResponsiveException(exCodeMsg);
        } catch (Exception e) {
            throw new GeneralException(String.format("用户[%s]登陆失败", sysUserDTO.getUsername()), e);
        }
    }

    @Override
    public void signOut(ServletRequest request, ServletResponse response) {
        HttpServletRequest httpRequest = WebUtils.toHttp(request);
        String token = httpRequest.getHeader("Authorization");
        if (StringUtils.isNotBlank(token)) {

            String account = JwtUtil.getAccount(token);
            SysUser sysUser = this.findUserByName(account);
            if (sysUser == null) {
                throw new ResponsiveException(ExCodeMsgEnum.ACCOUNT_NOT_EXISTS);
            }
            doSignOut(sysUser);

        } else {
            throw new ResponsiveException(ExCodeMsgEnum.LOGOUT_FAIL);
        }

    }

    protected void doSignOut(SysUser sysUser) {
        try {

            Subject subject = SecurityUtils.getSubject();

            subject.logout();
            shiroService.clearAuthByUserId(sysUser.getId());
            String refreshTokenCacheKey = Constants.CACHE_SHIRO_SYS_USER_TOKEN_REFRESH + sysUser.getUsername();
            //清空缓存
            if (cache.hasKey(refreshTokenCacheKey)) {
                cache.del(refreshTokenCacheKey);
            }
        } catch (Exception e) {
            throw new GeneralException(String.format("用户[%s]注销登录失败,msg:%s", sysUser.getUsername(), e.getMessage()), e);
        }

    }


    @Override
    public boolean checkAuth(SysUserDTO sysUserDTO) {
        Map<String, Object> paramMap =
                sysConfParamService.findParamValByName(SysParamNameEnum.SYS_GOOGLE_AUTHENTICATOR);

        if (!MapUtils.isEmpty(paramMap)) {
            Object isNeedAuth = paramMap.get(SysParamValueNameEnum.IS_NEED_AUTH.code());
            if (isNeedAuth != null && Boolean.valueOf(isNeedAuth.toString())) {
                if (StringUtils.isBlank(sysUserDTO.getAuthCode())) {
                    return false;
                }

                int timeOffset = paramMap.get(SysParamValueNameEnum.TIME_OFFSET.code()) == null ?
                        0 : Integer.parseInt(paramMap.get(SysParamValueNameEnum.TIME_OFFSET.code()).toString());

                SysUser sysUser =
                        this.getOne(new QueryWrapper<SysUser>()
                                .eq("username", sysUserDTO.getUsername()).select("secret_key"));

                long code = Long.valueOf(sysUserDTO.getAuthCode());
                long t = System.currentTimeMillis();
                GoogleAuthenticator ga = new GoogleAuthenticator();
                ga.setWindowSize(timeOffset); //should give timeOffset * 30 seconds of grace...
                return ga.check_code(sysUser.getSecretKey(), code, t);
            }
        }

        return true;
    }

    @Override
    public SysUserVO findSysUserById(String id) {
        SysUser sysUser = this.getById(id);
        SysUserVO vo = new SysUserVO();
        if (sysUser != null)
            BeanUtils.copyProperties(sysUser, vo);

        return vo;
    }

    @Override
    public SysUserInfoVO findSysUserInfoById(String id) {
        SysUser sysUser = this.getById(id);
        if (sysUser == null || StringUtils.isBlank(sysUser.getId())) {
            throw new ResponsiveException(ExCodeMsgEnum.ACCOUNT_DELETED);
        }
        SysUserInfoVO sysUserInfoVO = new SysUserInfoVO();
        BeanUtils.copyProperties(sysUser, sysUserInfoVO);
        List<VSysUserRole> vSysUserRoles = vSysUserRoleService.findByUserId(sysUserInfoVO.getId());

        Set<SysRoleVO> sysRoleVOS = new HashSet<>();
        if (!ListUtils.isEmpty(vSysUserRoles)) {
            vSysUserRoles.forEach(vSysUserRole ->
                    sysRoleVOS.add(SysRoleVO.builder()
                            .id(vSysUserRole.getRoleId())
                            .name(vSysUserRole.getRoleName())
                            .status(vSysUserRole.getRoleStatus())
                            .build())
            );
        }
        sysUserInfoVO.setRoles(sysRoleVOS);

        //按照sort排序
        //sysRoleVOS.stream().sorted(Comparator.comparing(SysRoleVO::getSort));

        return sysUserInfoVO;
    }

    @Override
    public SysUserVO getCurrentSysUser(ServletRequest request) {
        SysUser currentUser = SysUserContext.getCurrentSysUser();
        String username;
        if (currentUser == null) {
            HttpServletRequest httpRequest = WebUtils.toHttp(request);
            String token = httpRequest.getHeader("Authorization");
            if (StringUtils.isNotBlank(token)) {
                username = JwtUtil.getAccount(token);
            } else {
                return null;
            }
        } else {
            username = currentUser.getUsername();
        }

        currentUser = this.getOne(new QueryWrapper<SysUser>().eq("username", username));
        if (currentUser == null) {
            throw new ResponsiveException("身份异常，请重新登录");
        }
        SysUserVO vo = SysUserVO.builder().build();
        BeanUtils.copyProperties(currentUser, vo);
        return vo;
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public void add(SysUserDTO sysUserDTO) {
        int count = this.baseMapper.selectCount(
                new QueryWrapper<SysUser>().eq("username", sysUserDTO.getUsername()));

        if (count > 0) {
            throw new ResponsiveException(ExCodeMsgEnum.ACCOUNT_EXISTS);
        }


        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(sysUserDTO, sysUser);
        sysUser.setPassword(MD5Util.mixKeyMD5(sysUserDTO.getPassword()));
        sysUser.setStatus(UserStatusEnum.NORMAL.code());
        //生成身份验证码
        String secretKey = RandomKeyUtil.createSecretKey();
        Object paramVal = sysConfParamService.getValByValName(SysParamValueNameEnum.AUTH_HOST);
        String hostName = paramVal == null ? "小爱视频后台管理系统" : paramVal.toString();
        String url = GoogleAuthenticator.getQRBarcodeURL(sysUser.getUsername(), hostName, secretKey);
        sysUser.setSecretKey(secretKey);
        sysUser.setQrBarcodeUrl(url);
        sysUser.setCreateDate(new Date());

        this.save(sysUser);

        //用户关联表
        Collection<SysUserRole> sysUserRoles = new ArrayList<>();
        sysUserDTO.getRoleIds().forEach(roleid -> {
            SysUserRole sysUserRole = SysUserRole.builder()
                    .sysRoleId(roleid).sysUserId(sysUser.getId()).build();
            sysUserRoles.add(sysUserRole);
        });
        sysUserRoleService.saveBatch(sysUserRoles);

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT)
    public void removeUser(String id) {
        SysUser sysUser = this.getById(id);
        if (sysUser == null || sysUser.getId().equals(SysUserContext.getCurrentSysUser().getId()))
            throw new ResponsiveException("不能删除当前账户");

        this.removeById(id);
        sysUserRoleService.removeBySysUserId(id);
        doSignOut(sysUser);
    }

    @Override
    public void update(SysUserDTO sysUserDTO) {
        SysUser sysUser = SysUserContext.getCurrentSysUser();

        BeanUtils.copyProperties(sysUserDTO, sysUser);

        //旧角色id
        List<Integer> roleIds = JSONArray.parseArray(JSON.toJSONString(sysUserRoleService.listObjs(
                new QueryWrapper<SysUserRole>().eq("sys_user_id", sysUserDTO.getId())
                        .select("sys_role_id"))), Integer.class);

        if (!sysUserDTO.getRoleIds().containsAll(roleIds) || !roleIds.containsAll(sysUserDTO.getRoleIds())) {
            //删除旧的关联关系
            if (!ListUtils.isEmpty(roleIds)) {
                sysUserRoleService.remove(new UpdateWrapper<SysUserRole>().eq("sys_user_id",sysUserDTO.getId()).in("sys_role_id", roleIds));
            }
            List<SysUserRole> sysUserRoleList = new ArrayList<>();
            sysUserDTO.getRoleIds().forEach(roleId ->
                    sysUserRoleList.add(SysUserRole.builder().sysRoleId(roleId).sysUserId(sysUser.getId()).build())
            );

            if (!ListUtils.isEmpty(sysUserRoleList))
                sysUserRoleService.saveBatch(sysUserRoleList);
        }

        this.updateById(sysUser);
    }

    @Override
    public String regenerateAuth() {
        SysUser sysUser = SysUserContext.getCurrentSysUser();
        //生成身份验证码
        String secretKey = RandomKeyUtil.createSecretKey();
        Object paramVal = sysConfParamService.getValByValName(SysParamValueNameEnum.AUTH_HOST);
        String hostName = paramVal == null ? "365视频后台管理系统" : paramVal.toString();
        String url = GoogleAuthenticator.getQRBarcodeURL(sysUser.getUsername(), hostName, secretKey);
        //用户身份验证器秘钥和二维码路径
        this.updateById(SysUser.builder().id(sysUser.getId()).secretKey(secretKey).qrBarcodeUrl(url).build());

        return url;
    }

    @Override
    public void resetPassword(ServletRequest request, ServletResponse response, SysUserDTO sysUserDTO) {
        SysUser sysUser = this.getOne(new QueryWrapper<SysUser>().eq("username", sysUserDTO.getUsername()));
        if (sysUser == null) {
            throw new ResponsiveException(ExCodeMsgEnum.ACCOUNT_NOT_EXISTS);
        }
        //修改密码后需要注销重新登录
        sysUser.setPassword(MD5Util.mixKeyMD5(sysUserDTO.getPassword()));
        this.updateById(sysUser);
        doSignOut(sysUser);
    }


    @Override
    public void statusChangeByUName(String uName, UserStatusEnum status) {
        SysUser sysUser = this.findUserByName(uName);
        if (sysUser == null)
            throw new ResponsiveException(ExCodeMsgEnum.ACCOUNT_NOT_EXISTS);

        this.update(new UpdateWrapper<SysUser>()
                .eq("username", uName).set("status", status.code()));

        if (UserStatusEnum.NORMAL.code() != status.code()) {
            //踢出在线的用户
            doSignOut(sysUser);
        }

    }

    @Override
    public void statusChangeByUId(String uId, UserStatusEnum status) {
        SysUser sysUser = this.getById(uId);

        if (sysUser == null || Objects.equals(uId, SysUserContext.getCurrentSysUser().getId()))
            throw new ResponsiveException("不能修改当前账户状态");

        this.update(new UpdateWrapper<SysUser>()
                .eq("id", uId).set("status", status.code()));

        if (UserStatusEnum.NORMAL.code() != status.code()) {
            //踢出在线的用户
            doSignOut(sysUser);
        }

    }
}


