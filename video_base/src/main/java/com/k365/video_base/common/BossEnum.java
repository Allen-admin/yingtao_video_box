package com.k365.video_base.common;

public enum BossEnum {

    ISBOSS(1,"最高权限"),
    NOTBOSS(0,"不是最高权限");

    private int key;

    private String name;

    BossEnum(int key, String name) {
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
