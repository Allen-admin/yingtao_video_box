package com.k365.global.impl;

import com.k365.config.shiro.VmRealm;
import com.k365.global.ShiroService;
import com.k365.manager_service.SysResourceService;
import com.k365.video_base.model.po.SysResource;
import com.k365.video_common.exception.GeneralException;
import com.k365.video_common.util.SpringUtil;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.ListUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Gavin
 * @date 2019/6/21 11:31
 * @description：
 */
@Service
public class ShiroServiceImpl implements ShiroService {

    @Autowired
    private SysResourceService sysResourceService;

    @Override
    public Map<String, String> getVmFilterChainMap() {

        List<SysResource> list = sysResourceService.list();
        Map<String, String> resultMap = new LinkedHashMap<>();
        if (ListUtils.isEmpty(list)) {
            return resultMap;
        }


        // 权限遍历
        list.forEach(sysResource -> {
            if (BooleanUtils.isTrue(sysResource.getVerification())) {
                resultMap.put(sysResource.getUrl() + "/**", "perms[" + sysResource.getPermitCode() + ":*]");
            } else {
                resultMap.put(sysResource.getUrl() + "/**", "allow[*]");
            }

        });

//        list.forEach(sysPermit -> {
//            if (BooleanUtils.isNotTrue(sysPermit.getVerification())) {
//                resultMap.put(sysPermit.getUrl() + "/**", "anon");
//            }
//        });

        resultMap.put("/**", "anon");

        return resultMap;

    }


    @Override
    public void reloadPerms() {

        ShiroFilterFactoryBean shiroFilterFactoryBean = SpringUtil.getBean(ShiroFilterFactoryBean.class);

        AbstractShiroFilter abstractShiroFilter;
        try {
            abstractShiroFilter = (AbstractShiroFilter) shiroFilterFactoryBean.getObject();
        } catch (Exception e) {
            throw new GeneralException("重新加载权限失败", e);
        }
        PathMatchingFilterChainResolver filterChainResolver =
                (PathMatchingFilterChainResolver) abstractShiroFilter.getFilterChainResolver();
        DefaultFilterChainManager manager = (DefaultFilterChainManager) filterChainResolver
                .getFilterChainManager();

        /*清除旧版权限*/
//        manager.getFilterChains().clear();
//        shiroFilterFactoryBean.getFilterChainDefinitionMap().clear();
        clearAuthByUserIdCollection();

        /*更新新数据*/
        Map<String, String> filterChainDefinitionMap = getVmFilterChainMap();
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        filterChainDefinitionMap.forEach(manager::createChain);
    }

    @Override
    public void clearAuthByUserId(String uid) {
        VmRealm vmRealm = SpringUtil.getBean(VmRealm.class);
        vmRealm.clearAuthorizationInfoCache(uid);
    }

    @Override
    public void clearAuthByUserIdCollection() {
        VmRealm vmRealm = SpringUtil.getBean(VmRealm.class);
        vmRealm.clearAuthorizationInfoCache();
    }


}


