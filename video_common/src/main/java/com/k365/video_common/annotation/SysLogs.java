package com.k365.video_common.annotation;

import java.lang.annotation.*;

/**
 * @author Gavin
 * @date 2019/6/21 11:31
 * @description：
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SysLogs {

    String value();

}
