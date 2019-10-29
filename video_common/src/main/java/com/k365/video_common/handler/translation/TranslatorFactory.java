package com.k365.video_common.handler.translation;

/**
 * @author Gavin
 * @date 2019/8/5 11:31
 * @description：
 */
public class TranslatorFactory {

    public static final String YOUDAO = "youdao";

    public static final String GOOGLE = "google";

    public static AbstractTranslator createTranslator(String type) {
        switch (type){
            case YOUDAO :
                return new YoudaoTranslator();
            default :
                return new GoogleTranslator();
        }

    }
}
