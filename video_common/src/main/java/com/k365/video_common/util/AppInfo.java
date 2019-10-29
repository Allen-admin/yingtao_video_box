package com.k365.video_common.util;

/**
 * @author Gavin
 * @date 2019/9/28 9:35
 * @description：App终端信息
 */

public class AppInfo {

    /**
     * app版本号
     */
    private String appVersion;

    /**
     * APP系统名称
     */
    private String osName;

    /**
     * app系统key
     */
    private Integer osKey;

    /**
     * app名称
     */
    private String appTypeName;

    /**
     * app key
     */
    private Integer appTypeKey;

    /**
     * 终端浏览器
     */
    private String browser;


    public String getAppVersion() {

        return appVersion;
    }

    public void setAppVersion(String appVersion) {

        this.appVersion = appVersion;
    }

    public String getOsName() {

        return osName;
    }

    public void setOsName(String osName) {
        this.osName = osName;
    }

    public Integer getOsKey() {
        return osKey;
    }

    public void setOsKey(Integer osKey) {

        this.osKey = osKey;
    }

    public String getAppTypeName() {

        return appTypeName;
    }

    public void setAppTypeName(String appTypeName) {

        this.appTypeName = appTypeName;
    }

    public Integer getAppTypeKey() {

        return appTypeKey;
    }

    public void setAppTypeKey(Integer appTypeKey) {

        this.appTypeKey = appTypeKey;
    }

    public String getBrowser() {

        return browser;
    }

    public void setBrowser(String browser) {

        this.browser = browser;
    }
}
