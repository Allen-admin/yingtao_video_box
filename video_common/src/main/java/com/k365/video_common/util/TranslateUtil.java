package com.k365.video_common.util;

import com.k365.video_common.constant.SysParamNameEnum;
import com.k365.video_common.handler.translation.AbstractTranslator;
import com.k365.video_common.handler.translation.TranslatorFactory;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Gavin
 * @date 2019/8/5 14:05
 * @description：翻译工具类
 */
public class TranslateUtil {

    /**
     * 日文翻译简体中文
     * @param text 文本
     * @return
     * @throws Exception
     */
    public static String jaToZh_cn(String text) throws Exception {
        AbstractTranslator translator =
                TranslatorFactory.createTranslator(TranslatorFactory.GOOGLE);

        String result = translator.doTranslate(AbstractTranslator.ja, AbstractTranslator.zh_CN, text);

        if (!StringUtils.isBlank(result)) {
            translator = TranslatorFactory.createTranslator(TranslatorFactory.YOUDAO);
            result = translator.doTranslate(AbstractTranslator.ja, AbstractTranslator.zh_CN, text) ;
        }

        return StringUtils.isNotBlank(result) ? result : text;
    }

    /**
     *
     * @param number
     * @return
     */
    public static String arabicNumberToZh_cn(int number) {
        String prefix = "";
        if(number < 0 ){
            prefix = "负";
            number = Math.abs(number);
        }
        StringBuilder sb = new StringBuilder(prefix);
        char[] chars = String.valueOf(number).toCharArray();
        int numLen = chars.length;
        int i = 0;
        do {
            switch (Integer.valueOf(String.valueOf(chars[i]))) {
                case 0:
                    sb.append("零");
                    break;
                case 1:
                    sb.append("一");
                    break;
                case 2:
                    sb.append("二");
                    break;
                case 3:
                    sb.append("三");
                    break;
                case 4:
                    sb.append("四");
                    break;
                case 5:
                    sb.append("五");
                    break;
                case 6:
                    sb.append("六");
                    break;
                case 7:
                    sb.append("七");
                    break;
                case 8:
                    sb.append("八");
                    break;
                case 9:
                    sb.append("九");
                    break;
            }
            ++i;
        }while (number >= 10 && numLen > i);
        return sb.toString();
    }

    public static void main(String[] args) {
        int n =
                10203202;
        System.out.println(n);
        System.out.println(arabicNumberToZh_cn(n) );
    }


}
