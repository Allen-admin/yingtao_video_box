package com.k365.video_common.handler.translation;

import com.k365.video_common.response.HttpClientResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;

/**
 * @author Gavin
 * @date 2019/8/5 11:38
 * @description：
 */
public abstract class AbstractTranslator {

    protected static final String ENCODING = "utf-8";

    /**
     * 中文　»　英语
     */
    public static final String ZH_CN2EN = "ZH_CN2EN";
    /**
     * 中文　»　日语
     */
    public static final String ZH_CN2JA = "ZH_CN2JA";
    /**
     * 中文　»　韩语
     */
    public static final String ZH_CN2KR = "ZH_CN2KR";
    /**
     * 中文　»　法语
     */
    public static final String ZH_CN2FR = "ZH_CN2FR";
    /**
     * 中文　»　俄语
     */
    public static final String ZH_CN2RU = "ZH_CN2RU";
    /**
     * 日语　»　中文
     */
    public static final String JA2ZH_CN = "JA2ZH_CN";
    /**
     * 英语　»　中文
     */
    public static final String EN2ZH_CN = "EN2ZH_CN";

    /**
     * 中文简体
     */
    public static final String zh_CN = "zh_CN";
    /**
     * 中文繁体
     */
    public static final String zh_TW = "zh_TW";
    /**
     * 日文
     */
    public static final String ja = "ja";
    /**
     * 英文
     */
    public static final String en = "en";
    /**
     * 自动检测
     */
    public static final String AUTO = "auto";


    public abstract String doTranslate(@NonNull String from, @NonNull String to, @NonNull String text) throws Exception;


    protected static boolean hasContent(HttpClientResult httpClientResult) {
        if (httpClientResult.getCode() == 200
                && httpClientResult.getContent() != null
                && StringUtils.isNotBlank(String.valueOf(httpClientResult.getContent()))) {

            return true;
        }

        return false;
    }


}
