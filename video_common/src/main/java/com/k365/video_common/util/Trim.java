package com.k365.video_common.util;

/**
 * 自定义字符串修剪方法
 * Created by ylchou@foxmail.com on 18/3/10.
 */
public class Trim {


    /**
     * 去掉指定字符串前面指定的字符
     *
     * @param str
     * @param c
     * @return
     */
    public static String custom_ltrim(String str, String c) {
        int index = str.indexOf(c); //获取出现key字符串的第一个位置，这里要保证前面没有跟KEY重复
        String left = str.substring(index);
        return left;
    }


}