package com.k365.video_manager_api;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/**
 * @author Gavin
 * @date 2019/7/31 12:08
 * @description：
 */

@Aspect
@Component
public class IOSArmor {

    @Before(value = "execution(* com.k365.video_manager_api.VideoManagerApiApplicationTests.doST())")
    public String before() {
        return "结束";
    }
}