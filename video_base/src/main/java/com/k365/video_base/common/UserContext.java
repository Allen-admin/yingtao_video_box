package com.k365.video_base.common;

import com.k365.video_base.model.po.User;

/**
 * @author Gavin
 * @date 2019/7/31 14:51
 * @description：APP防弹衣上下文对向
 */
public class UserContext implements AutoCloseable {

    static final ThreadLocal<User> context = new ThreadLocal<>();

    public UserContext(User user) {
        context.set(user);
    }

    public static User getCurrentUser() {
        return context.get();
    }

    @Override
    public void close() throws Exception {
        context.remove();
    }
}
