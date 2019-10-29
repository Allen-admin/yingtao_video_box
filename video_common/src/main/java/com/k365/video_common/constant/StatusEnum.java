package com.k365.video_common.constant;

/**
 * @author Gavin
 * @date 2019/7/17 13:16
 * @description：
 */
public enum StatusEnum {

    ENABLE(1, "启用"),
    PROHIBIT(2, "禁用"),
    SPARE(3, "待启用"),

    FINISHED(4,"完成"),
    UNFINISHED(5,"未完成"),

    ACCEPTED(6,"已受理"),
    NOT_ACCEPTED(7,"未受理");


    private int key;

    private String explain;

    StatusEnum(int key, String explain) {
        this.key = key;
        this.explain = explain;
    }

    public int key() {
        return this.key;
    }


    @Override
    public String toString() {
        return "{\\\"key\\\":" + key +
                ",\\\"explain\\\":\\\"" + explain+ "\\\"}" ;
    }
}
