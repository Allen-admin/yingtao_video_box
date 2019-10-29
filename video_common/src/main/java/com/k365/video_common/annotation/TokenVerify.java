package com.k365.video_common.annotation;

import java.lang.annotation.*;

/**
 * @author Gavin
 * @date 2019/8/8 20:33
 * @description：
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.TYPE})
@Documented
public @interface TokenVerify {



    boolean skip() default false;

}
