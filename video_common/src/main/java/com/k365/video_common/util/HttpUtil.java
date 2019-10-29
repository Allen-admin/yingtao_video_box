package com.k365.video_common.util;

import com.k365.video_common.constant.AppTypeEnum;
import com.k365.video_common.constant.OSEnum;
import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

/**
 * @author Gavin
 * @date 2019/7/31 14:09
 * @description：
 */
public class HttpUtil {

    /**
     * 获取HttpServletRequest
     */
    public static HttpServletRequest getHttpReq(ServletRequest request) {
        return WebUtils.toHttp(request);
    }

    /**
     * 获取User-Agent信息
     */
    public static UserAgent getUserAgent(ServletRequest request) {
        HttpServletRequest req = getHttpReq(request);
        String ua = req.getHeader("User-Agent");
        if (StringUtils.isNotBlank(ua)) {
            UserAgent userAgent = UserAgent.parseUserAgentString(ua);
            if (userAgent != null)
                return userAgent;
        }
        return new UserAgent(OperatingSystem.UNKNOWN, Browser.UNKNOWN);
    }

    /**
     * 获取APP版本
     */
    public static String getAppVersion(HttpServletRequest request) {
        return request.getHeader("App-Version");
    }

    /**
     * 获取APP类型
     */
    public static AppTypeEnum getAppType(HttpServletRequest request) {
        return AppTypeEnum.getByCode(request.getHeader("App-Type"));
    }

    /**
     * 获取APP类型
     */
    public static AppTypeEnum getAppType(ServletRequest request) {
        return getAppType(getHttpReq(request));
    }

    /**
     * 获取APP版本
     */
    public static String getAppVersion(ServletRequest request) {
        return getAppVersion(getHttpReq(request));
    }

    /**
     * 获取OS
     */
    public static OSEnum getOS(ServletRequest request) {
        UserAgent userAgent = getUserAgent(request);
        OperatingSystem os = userAgent.getOperatingSystem();

        if (os != null && StringUtils.isNotBlank(os.getName())) {
            if (os.getName().contains(OSEnum.IOS.getName())) {
                return OSEnum.IOS;
            }
            if (os.getName().contains(OSEnum.ANDROID.getName())) {
                return OSEnum.ANDROID;
            }
            if (os.getName().contains(OSEnum.WINDOWS.getName())) {
                return OSEnum.WINDOWS;
            }
        }

        return null;

    }

    /**
     *  获取App信息
     */
    public static AppInfo getAppInfo(ServletRequest request) {
        AppInfo appInfo = new AppInfo();
        AppTypeEnum appType = getAppType(request);
        OSEnum os = getOS(request);

        if(appType != null) {
            appInfo.setAppTypeKey(appType.getKey());
            appInfo.setAppTypeName(appType.getCode());
        }
        if(os != null) {
            appInfo.setOsKey(os.key());
            appInfo.setOsName(os.getName());
        }

        appInfo.setAppVersion(getAppVersion(request));
        appInfo.setBrowser(getBrowser(request));
        return appInfo;
    }

    /**
     * 获取浏览器名称和版本
     */
    public static String getBrowser(ServletRequest request) {
        UserAgent userAgent = getUserAgent(request);
        Browser browser = userAgent.getBrowser();
        return browser == null ? null : browser.getName();
    }

    /**
     *
     * 判断浏览器  App
     *
     * */
    public static OSEnum getOS3(ServletRequest request){
        HttpServletRequest httpReq = getHttpReq(request);
        if (httpReq.getHeader("User-Agent").startsWith("Mozilla")) {
           return OSEnum.BROWSER;
        } else {
            return  OSEnum.APP;
        }
    }

}
