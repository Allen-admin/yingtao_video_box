package com.k365.video_base.common;

public enum TopLocationEnum {

    TOPLIKE(2,"智能推荐"),
    TOPPERFECT(1,"编辑精选");

    private int key;

    private String name;

    TopLocationEnum(int key, String name) {
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
