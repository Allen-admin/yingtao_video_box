package com.k365.video_common.constant;

import java.util.Objects;

/**
 * @author Gavin
 * @date 2019/9/27 19:41
 * @description：APP类型
 */
public enum AppTypeEnum {

    XIAO_AI(1,"xiao_ai"),
    HEI_MEI(2,"hei_mei");

    private int key;

    private String code;

    public String getCode() {
        return code;
    }

    public int getKey() {
        return key;
    }

    AppTypeEnum(int key, String code){
        this.key = key;
        this.code = code;
    }

    public static AppTypeEnum getByCode(String code){
        for(AppTypeEnum appType : values()){
            if(Objects.equals(appType.code,code)) return appType;
        }
        return null;
    }

    public static AppTypeEnum getByKey(int key){
        for(AppTypeEnum appType : values()){
            if(Objects.equals(appType.key,key)) return appType;
        }
        return null;
    }

}
