package com.k365.config.shiro;


import com.k365.config.jwt.AllowJwtFilter;
import com.k365.config.jwt.VmUrlFilter;
import com.k365.global.ShiroService;
import com.k365.global.impl.ShiroServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.config.MethodInvokingFactoryBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.DelegatingFilterProxy;

import java.util.Map;

/**
 * @author Gavin
 * @date 2019/6/21 11:31
 * @description：
 */
@Configuration
@Slf4j
public class ShiroConfig {

    private static final String AUTHORIZATION_CACHE_NAME = ":authorization_cache";

    @Bean
    public CredentialsMatcher getCredentialsMatcher() {
        return new CredentialsMatcher();
    }


    @Bean
    public VmRealm vmRealm() {
        VmRealm vmRealm = new VmRealm();
        vmRealm.setCredentialsMatcher(getCredentialsMatcher());
        vmRealm.setAuthorizationCacheName(VmRealm.class.getName() + AUTHORIZATION_CACHE_NAME);
        return vmRealm;
    }


    @Bean
    public ShiroService shiroService() {
        return new ShiroServiceImpl();
    }


    @Bean("securityManager")
    public DefaultWebSecurityManager securityManager() {
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
        //**使用自定义JwtRealm
        manager.setRealm(vmRealm());
        manager.setCacheManager(shiroRedisCacheManager());

        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
        manager.setSubjectDAO(subjectDAO);

        return manager;
    }

    /**
     * redis缓存方案
     *
     * @return
     */
    @Bean
    public CacheManager shiroRedisCacheManager() {
        return new ShiroCacheManager();
    }


    @Bean("perms")
    public VmUrlFilter getVmUrlFilter() {
        return new VmUrlFilter();
    }

    @Bean("allow")
    public AllowJwtFilter getAllowJwtFilter() {
        return new AllowJwtFilter();
    }


    @Bean("shiroFilter")
    public ShiroFilterFactoryBean shiroFilterFactoryBean(DefaultWebSecurityManager securityManager) {
        ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();

        factoryBean.setSecurityManager(securityManager);

        //**自定义url规则
        Map<String, String> filterRuleMap = shiroService().getVmFilterChainMap();
        factoryBean.setFilterChainDefinitionMap(filterRuleMap);

        return factoryBean;
    }

    @Bean(name = "lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator creator = new DefaultAdvisorAutoProxyCreator();
        creator.setProxyTargetClass(true);
        return creator;
    }

    /**
     * 开启shiro aop注解支持
     *
     * @param securityManager
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }

    @Bean
    public MethodInvokingFactoryBean methodInvokingFactoryBean(SecurityManager securityManager) {
        MethodInvokingFactoryBean bean = new MethodInvokingFactoryBean();
        bean.setStaticMethod("org.apache.shiro.SecurityUtils.setSecurityManager");
        bean.setArguments(securityManager);
        return bean;
    }

    @Bean
    public FilterRegistrationBean delegatingFilterProxy() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        DelegatingFilterProxy proxy = new DelegatingFilterProxy();
        proxy.setTargetFilterLifecycle(true);
        proxy.setTargetBeanName("shiroFilter");
        filterRegistrationBean.setFilter(proxy);
        return filterRegistrationBean;
    }


}

