package com.k365.video_common.constant;

import java.util.Objects;

/**
 * @author Gavin
 * @date 2019/7/31 11:35
 * @description：
 */
public enum OSEnum {

    IOS(1,"iPhone"),
    ANDROID(2,"Android"),
    WINDOWS(3,"Windows"),
    BROWSER(4,"Browser"),
    APP(5,"App");

    private int key;
    private String name;

    OSEnum(int key,String name) {
        this.key = key;
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    public int key(){
        return this.key;
    }

    @Override
    public String toString() {
        return "{\\\"key\\\":" + key +
                ",\\\"name\\\":\\\"" + name + "\\\"}" ;
    }

    public static OSEnum getByKey(int key){
        for(OSEnum os : values()){
            if(Objects.equals(os.key,key)){
                return os;
            }
        }
        return null;
    }

}
