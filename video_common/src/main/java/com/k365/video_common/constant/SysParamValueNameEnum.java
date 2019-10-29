package com.k365.video_common.constant;

import java.util.*;

/**
 * @author Gavin
 * @date 2019/6/24 10:29
 * @description：
 */
public enum SysParamValueNameEnum {

    IS_NEED_AUTH("is_need_auth", "后台系统是否需要身份验证码", SysParamNameEnum.SYS_GOOGLE_AUTHENTICATOR),
    TIME_OFFSET("time_offset", "身份验证码时间偏移量", SysParamNameEnum.SYS_GOOGLE_AUTHENTICATOR),
    AUTH_HOST("auth_host", "身份验证器前台展示名称", SysParamNameEnum.SYS_GOOGLE_AUTHENTICATOR),
//    ISO_ARMOR_SWITCH(OSEnum.IOS.getName(),"ios伪装开关",SysParamNameEnum.MOBILE_DATA_ARMOR),
//    ANDROID_ARMOR_SWITCH(OSEnum.ANDROID.getName(),"android伪装开关",SysParamNameEnum.MOBILE_DATA_ARMOR),
    AD_DAILY_CLICKS("ad_daily_clicks","每日广告点击次数上限",SysParamNameEnum.MOBILE_AD_CLICK),
    AD_EACH_AWARD("ad_each_award","每次点击广告奖励观影数",SysParamNameEnum.MOBILE_AD_CLICK),
    XIAOAI_PAGE_URl("xiaoai_page_url","小爱app下载页面路径",SysParamNameEnum.APP_DOWNLOAD_PAGE),
    HEIMEI_PAGE_URl("heimei_page_url","黑莓app下载页面路径",SysParamNameEnum.APP_DOWNLOAD_PAGE),
    XIAOAI_LOGO_URL("xiaoai_logo_url","小爱aPP下载二维码Logo",SysParamNameEnum.QR_CODE_LOGO),
    HEIMEI_LOGO_URL("heimei_logo_url","黑莓aPP下载二维码Logo",SysParamNameEnum.QR_CODE_LOGO),
    AD_COUNTDOWN_SECOND("second","倒计时秒",SysParamNameEnum.AD_COUNTDOWN),
    VIP_TRY_VIEWING_COUNT("try_viewing_count","vip视频试看次数",SysParamNameEnum.VIP_VIEWING_COUNT),
    VIP_VIEWING_LEVEL("vip_viewing_level","vip视频观影等级",SysParamNameEnum.VIP_VIEWING_COUNT),
    INFINITE_VALUE("infinite_value","观影次数是否无限",SysParamNameEnum.VIEW_INFINITE_VALUE);

    /**
     * 参数名
     */
    private final String code;

    /**
     * 参数说明
     */
    private final String name;

    /**
     * 所属柜类
     */
    private final SysParamNameEnum sysParamNameEnum;


    SysParamValueNameEnum(String code, String name, SysParamNameEnum sysParamNameEnum) {
        this.code = code;
        this.name = name;
        this.sysParamNameEnum = sysParamNameEnum;
    }

    public String code() {
        return code;
    }

    public String getName() {
        return name;
    }

    public SysParamNameEnum getSysParamNameEnum() {
        return sysParamNameEnum;
    }

    /**
     * 根据类型返回所对应的的所有枚举
     * @param paramName
     * @return
     */
    public static List<SysParamValueNameEnum> getSysParamValueNameEnumByName(SysParamNameEnum paramName) {
        List<SysParamValueNameEnum> list = new ArrayList<>();
        for (SysParamValueNameEnum pvn : SysParamValueNameEnum.values()) {
            if (pvn.sysParamNameEnum == paramName) {
                list .add(pvn);
            }
        }
        return list;
    }


    @Override
    public String toString() {
        return "{\\\"code\\\":\\\"" + code +"\\\""+
                ",\\\"name\\\":\\\"" + name + "\\\"" +
                ",\\\"sysParamNameEnum\\\":" + sysParamNameEnum + "}" ;
    }

}
