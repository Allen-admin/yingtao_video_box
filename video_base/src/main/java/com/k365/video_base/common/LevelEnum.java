package com.k365.video_base.common;

public enum LevelEnum {

    LEVELS(-1,"超级管理员"),
    LEVEL0(0,"管理员"),
    LEVEL1(1,"组长"),
    LEVEL2(2,"普通员工");

    private int key;

    private String name;

    LevelEnum(int key, String name) {
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
