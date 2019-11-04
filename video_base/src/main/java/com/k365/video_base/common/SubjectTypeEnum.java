package com.k365.video_base.common;

public enum SubjectTypeEnum {

    LEVEL1(1,"普通专题"),
    LEVEL2(2,"精品专栏");

    private int key;

    private String name;

    SubjectTypeEnum(int key, String name) {
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
