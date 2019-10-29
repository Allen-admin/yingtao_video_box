package com.k365.global;


import java.util.Map;

/**
 * @author Gavin
 * @date 2019/6/21 11:31
 * @description：
 */
public interface ShiroService {

    /**
     * 获取拦截器数据
     *
     * @return
     */
    Map<String, String> getVmFilterChainMap();

    /**
     * 重新加载权限
     */
    void reloadPerms();

    /**
     * 清除指定用户ID的授权信息
     *
     * @param uid 用户ID
     */
    void clearAuthByUserId(String uid);

    /**
     * 清除指定用户ID集合的授权信息
     */
    void clearAuthByUserIdCollection();

}
