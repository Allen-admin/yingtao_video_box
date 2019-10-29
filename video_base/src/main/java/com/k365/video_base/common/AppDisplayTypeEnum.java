package com.k365.video_base.common;

import java.util.Arrays;
import java.util.Objects;

/**
 * @author Gavin
 * @date 2019/7/30 20:07
 * @description：
 */
public enum AppDisplayTypeEnum {
    AD(1, "AD", "广告"),
    NOTICE(2, "NOTICE", "公告"),
    BANNER(3, "BANNER", "轮播图"),

    AD_DISPLAY_FOR_VIDEO_LIST(10, AD.type, "视频列表广告"),
    AD_DISPLAY_FOR_BEFORE_PLAY(11, AD.type, "视频播放前广告"),
    AD_DISPLAY_FOR_APP_UP(12, AD.type, "App启动页广告"),
    AD_DISPLAY_FOR_GAME_TOP(13, AD.type, "游戏头部广告"),
    AD_DISPLAY_FOR_GAME_LIST(14, AD.type, "游戏列表广告"),
    AD_DISPLAY_FOR_VIDEO_STOP(15, AD.type, "视频暂停时广告"),
    AD_DISPLAY_FOR_FEATURED_VIDEO(16, AD.type, "编辑精选广告"),
    AD_DISPLAY_FOR_PLAY_PAGE_SMALL(17, AD.type, "播放页面小广告"),
    AD_DISPLAY_FOR_PLAY_PAGE_BIG(18, AD.type, "播放页面大广告"),


    AD_DISPLAY_FOR_RECOMMEND_VIDEO_BANNER(30, BANNER.type, "推荐页轮播图"),
    AD_DISPLAY_FOR_THEMATIC_VIDEO_BANNER(31, BANNER.type, "专题视频轮播图"),
    AD_DISPLAY_FOR_LATEST_VIDEO_BANNER(32, BANNER.type, "最新页轮播图"),
    AD_DISPLAY_FOR_HOTTEST_VIDEO_BANNER(33, BANNER.type, "最热页轮播图"),
    AD_DISPLAY_FOR_VIP_VIDEO_BANNER(34, BANNER.type, "VIP视频页面轮播图"),


    NOTICE_DISPLAY_FOR_POPUP(50, NOTICE.type, "弹窗公告"),
    NOTICE_DISPLAY_FOR_GAME(51, NOTICE.type, "游戏公告"),
    NOTICE_DISPLAY_FOR_NEWEST(52, NOTICE.type, "最新公告"),
    NOTICE_DISPLAY_FOR_VIP(53,NOTICE.type,"VIP公告");

    private int key;

    private String type;

    private String explain;

    AppDisplayTypeEnum(int key, String type, String explain) {
        this.key = key;
        this.type = type;
        this.explain = explain;
    }

    /**
     * 判断是否存在该类型的key
     *
     * @param key
     * @param type
     * @return
     */
    public static boolean hasKey4Type(int key, String type) {
        for (AppDisplayTypeEnum adt : AppDisplayTypeEnum.values()) {
            if (adt.type.equals(type) && Objects.equals(adt.key, key)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 根据类型返回所对应的的所有枚举
     *
     * @param adtType
     * @return
     */
    public static AppDisplayTypeEnum[] getAdtEnumsByType(AppDisplayTypeEnum adtType) {
        AppDisplayTypeEnum[] appDisplayTypeEnums = new AppDisplayTypeEnum[AppDisplayTypeEnum.values().length];
        int i = 0;
        for (AppDisplayTypeEnum adt : AppDisplayTypeEnum.values()) {
            if (adt.type.equals(adtType.type()) && adt.key > 9) {

                appDisplayTypeEnums[i] = adt;
                ++i;
            }
        }
        return Arrays.copyOf(appDisplayTypeEnums, i);
    }

    /**
     * 根据类型和key返回枚举
     *
     * @param key
     * @param type
     * @return
     */
    public static AppDisplayTypeEnum getAdtEnum(int key, String type) {
        for (AppDisplayTypeEnum adt : AppDisplayTypeEnum.values()) {
            if (adt.type.equals(type) && Objects.equals(adt.key, key)) {
                return adt;
            }
        }
        return null;
    }

    /**
     * 根据类型和key返回枚举
     *
     * @param key
     * @return
     */
    public static AppDisplayTypeEnum getAdtEnum(int key) {
        for (AppDisplayTypeEnum adt : AppDisplayTypeEnum.values()) {
            if (Objects.equals(adt.key, key)) {
                return adt;
            }
        }
        return null;
    }

    public int key() {
        return this.key;
    }

    public String type() {
        return this.type;
    }

    public String explain() {
        return this.explain;
    }

    @Override
    public String toString() {
        return "{\"key\":" + key +
                ",\"type\":\"" + type + "\"" +
                ",\"explain\":\"" + explain + "\"}";
    }

}
