package com.k365.video_common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Gavin
 * @date 2019/6/21 11:31
 * @description：
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface JwtClaim {

    String value() default "username";

    boolean exception() default true;

}
