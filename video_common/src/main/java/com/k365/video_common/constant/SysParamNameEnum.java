package com.k365.video_common.constant;

import java.util.Objects;

/**
 * @author Gavin
 * @date 2019/6/23 19:37
 * @description：系统参数名称
 */
public enum SysParamNameEnum {

    SYS_GOOGLE_AUTHENTICATOR("sys_google_auth_param", "管理系统Google身份验证器参数"),
//    MOBILE_DATA_ARMOR("mobile_data_armor","移动端伪装数据"),
    MOBILE_AD_CLICK("mobile_ad_click","广告点击"),
    APP_DOWNLOAD_PAGE("app_download_page","app下载页面"),
    QR_CODE_LOGO("qr_code_logo","二维码logo"),
    AD_COUNTDOWN("ad_countdown","广告倒计时"),
    VIP_VIEWING_COUNT("vip_viewing","vip观影限制"),
    VIEW_INFINITE_VALUE("view_infinite","观影次数限制");


    private final String code;

    private final String name;

    SysParamNameEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String code() {
        return code;
    }

    public String getName() {
        return name;
    }

    /**
     * 根据key返回枚举
     * @param code
     * @return
     */
    public static SysParamNameEnum getSysParamNameEnum(String code) {
        for (SysParamNameEnum spn : SysParamNameEnum.values()) {
            if (Objects.equals(spn.code, code)) {
                return spn;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "{\\\"code\\\":\\\"" + code +"\\\""+
                ",\\\"name\\\":\\\"" + name+ "\\\"}" ;
    }

}
