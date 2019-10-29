package com.k365.video_common.util;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.k365.video_common.constant.DateStyleEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.AnnotationUtils;

import java.io.OutputStream;
import java.text.SimpleDateFormat;

/**
 * @author Gavin
 * @date 2019/6/24 11:03
 * @description：
 */
@Slf4j
public class JsonUtil {

    private static ObjectMapper objectMapper ;

    static {

        objectMapper = new ObjectMapper();

        objectMapper.setDateFormat(new SimpleDateFormat(DateStyleEnum.yyyy_mm_dd_hh_mm_ss.value().toString()));
        //设置在反序列化时忽略在JSON字符串中存在，而在Java中不存在的属性
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.setFilters(new SimpleFilterProvider().setFailOnUnknownId(false));
    }

    /*
    public static JsonUtil getInstance() {

        if (instance == null) {
            synchronized (JsonUtil.class) {
                if (instance == null) {
                    instance = new JsonUtil();
                }
            }
        }

        return instance;
    }
    */

    public static String stringify(Object object) {

        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return null;
    }

    public static String stringify(Object object, String... properties) {

        try {
            return objectMapper
                    .writer(new SimpleFilterProvider().addFilter(
                            AnnotationUtils.getValue(
                                    AnnotationUtils.findAnnotation(object.getClass(), JsonFilter.class)).toString(),
                            SimpleBeanPropertyFilter.filterOutAllExcept(properties)))
                    .writeValueAsString(object);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return null;
    }

    public static void stringify(OutputStream out, Object object) {

        try {
            objectMapper.writeValue(out, object);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public static void stringify(OutputStream out, Object object, String... properties) {

        try {
            objectMapper
                    .writer(new SimpleFilterProvider().addFilter(
                            AnnotationUtils.getValue(
                                    AnnotationUtils.findAnnotation(object.getClass(), JsonFilter.class)).toString(),
                            SimpleBeanPropertyFilter.filterOutAllExcept(properties)))
                    .writeValue(out, object);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public static <T> T parse(String json, Class<T> clazz) {

        if (json == null || json.length() == 0) {
            return null;
        }

        try {
            return objectMapper.readValue(json, clazz);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return null;
    }

}
