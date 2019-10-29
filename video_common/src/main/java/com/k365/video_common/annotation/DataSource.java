package com.k365.video_common.annotation;

import com.k365.video_common.constant.DataSourceTypeEnum;

import java.lang.annotation.*;

/**
 * @author Gavin
 * @date 2019/7/1 19:43
 * @description：用于手动指定数据源
 */

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataSource {

    DataSourceTypeEnum type() default DataSourceTypeEnum.VIDEO_SITE_DB;
}
