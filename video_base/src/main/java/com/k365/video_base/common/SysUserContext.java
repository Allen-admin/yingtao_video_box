package com.k365.video_base.common;

import com.k365.video_base.model.po.SysUser;

/**
 * @author Gavin
 * @date 2019/7/5 16:32
 * @description：系统用户上下文对象
 */
public class SysUserContext  implements AutoCloseable {

    static final ThreadLocal<SysUser> current = new ThreadLocal<>();

    public SysUserContext(SysUser user) {
        current.set(user);
    }

    public static SysUser getCurrentSysUser() {
        return current.get();
    }


    @Override
    public void close() throws Exception {
        current.remove();
    }
}
