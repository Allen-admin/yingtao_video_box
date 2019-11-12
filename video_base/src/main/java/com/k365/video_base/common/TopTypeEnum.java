package com.k365.video_base.common;

public enum TopTypeEnum {

    NOSETTOP(2,"取消置顶"),
    SETTOP(1,"设置置顶");

    private int key;

    private String name;

    TopTypeEnum(int key, String name) {
        this.key = key;
        this.name = name;
    }

    public int key() {
        return this.key;
    }


    @Override
    public String toString() {
        return "{\\\"key\\\":" + key +
                ",\\\"explain\\\":\\\"" + name+ "\\\"}" ;
    }
}
