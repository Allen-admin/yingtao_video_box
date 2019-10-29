package com.k365.video_common.constant;

import java.io.Serializable;

/**
 * @author Gavin
 * @date 2019/6/20 15:47
 * @description：
 */
public enum DateStyleEnum {

    yyyymmdd("0", "yyyyMMdd"),
    yyyy_mm_dd_hh_mm_ss("1", "yyyy-MM-dd HH:mm:ss"),
    yyyy_MM_dd("2","yyyy-MM-dd"),
    yyyymmddhhmmss("1", "yyyyMMddHHmmss");

    private final Serializable key;

    private final Serializable value;


    DateStyleEnum( Serializable key, Serializable value ) {
        this.key = key;
        this.value = value;
    }


    public Serializable key() {
        return key;
    }


    public Serializable value() {
        return value;
    }
}
