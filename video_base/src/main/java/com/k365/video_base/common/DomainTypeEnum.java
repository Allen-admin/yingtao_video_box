package com.k365.video_base.common;

/**
 * @author Gavin
 * @date 2019/8/26 13:32
 * @description：
 */
public enum DomainTypeEnum {

    XIAOAI_OFFICIAL_DOMAIN(1,"小爱官网域名"),
    HEIMEI_OFFICIAL_DOMAIN(2,"黑莓官网域名"),
    XIAOAI_DOMAIN(3,"小爱APP访问域名"),
    HEIMEI_DOMAIN(4,"黑莓APP访问域名"),
    FILM_PICTURE_DOMAIN_APP(5,"app影片图片域名"),
    FILM_PICTURE_DOMAIN_BROWSER(6,"浏览器影片图片域名"),
    FILM_APPPICTURE_DOMAIN_APP(7,"app图片域名");


    private int key;

    private String name;

    DomainTypeEnum(int key, String name) {
        this.key = key;
        this.name = name;
    }

    public int key() { return this.key; }

    public String getName() { return this.name; }

    @Override
    public String toString() {
        return "{\\\"key\\\":" + key +
                ",\\\"name\\\":\\\"" + name + "\\\"}" ;
    }

}
