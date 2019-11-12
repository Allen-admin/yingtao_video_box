package com.k365.video_base.common;

public enum TopPerfectEnum {

    ISTOP(1,"置顶"),
    NOTTOP(0,"不是置顶");

    private int key;

    private String name;

    TopPerfectEnum(int key, String name) {
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
