package com.k365.video_common.annotation;

import com.k365.video_common.constant.OSEnum;

import java.lang.annotation.*;

/**
 * @author Gavin
 * @date 2019/7/31 13:24
 * @description：
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface Armor {

    OSEnum[] target() default OSEnum.IOS;
}
