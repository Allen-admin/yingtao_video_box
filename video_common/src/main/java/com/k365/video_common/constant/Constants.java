package com.k365.video_common.constant;

/**
 * @author Gavin
 * @date 2019/6/20 13:11
 * @description：
 */
public class Constants {

    public static final String CAPTCHA_CODE = "captcha-code";

    public static final String SPLIT_STR = "#-.-";

    public static final String SYS_USER_SESSION = "SysUserSession";

    // 登录标识
    public static final String LOGIN_SIGN = "Authorization";

    public static final String VIDEO_SITE_DB = "video-site-db";

    //request请求头属性
    public static final String REQUEST_AUTH_HEADER="Authorization";

    //request请求头属性
    public static final String REQUEST_TOKEN_HEADER="Token";

    //JWT-account
    public static final String ACCOUNT = "account";

    //JWT-currentTimeMillis
    public final static String CURRENT_TIME_MILLIS = "currentTimeMillis";

    //Shiro redis 前缀
    public static final String CACHE_PREFIX_SHIRO = "video_box:shiro_cache:";

    //Redis key
    //系统用户信息缓存key
    public final static String  CACHE_CURRENT_SYS_USER_INFO = "video_box:sys_user_info:";

    //系统用户token缓存key
    public final static String  CACHE_SHIRO_SYS_USER_TOKEN = "video_box:sys_user_token:";

    //系统用户token 刷新时间 缓存key
    public final static String  CACHE_SHIRO_SYS_USER_TOKEN_REFRESH = "shiro:video_box:sys_user_token_refresh:";

    //系统用户密码错误次数 缓存key
    public final static String  CACHE_SHIRO_SYS_USER_PWD_WRONG_COUNT = "video_box:sys_user_pwd_wrong_count:";

    //系统用户登录 缓存key
    public final static String  CACHE_SYS_USER_TOKEN = "video_box:sys_user_token:";

    //视频游客用户信息
    public final static String CACHE_VISITOR_USER_INFO = "video_box:visitor_user_info:";

    //视频用户短信验证码
    public final static String CACHE_USER_VERIFY_CODE = "video_box:user_verify_code:";

    //视频用户登录认证缓存
    public final static String CACHE_USER_TOKEN = "video_box:user_token:";

    //视频用户信息缓存
    public final static String CACHE_USER_INFO = "video_box:user_info:";

    //ip黑白名单缓存key
    public final static String CACHE_IP_RESTRICT = "video_box:ip_restrict";


}
