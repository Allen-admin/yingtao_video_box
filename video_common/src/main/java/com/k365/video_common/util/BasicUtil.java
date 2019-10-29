package com.k365.video_common.util;

/**
 * @author Gavin
 * @date 2019/6/28 18:45
 * @description：
 */
public class BasicUtil {

    /**
     * 去除字符串所有空格
     * @param str
     * @return
     */
    public static String clearSpace(String str) {
        return str.replaceAll(" ", "");
    }

    /**
     * 去除多个字符串所有空格
     * @param str
     * @return
     */
    public static String[] clearSpace(String... str) {
        String[] temps = new String[str.length];

        for(int i = 0; i < str.length; ++i) {
            temps[i] = str[i].replaceAll(" ", "");
        }

        return temps;
    }

    /**
     * 获取long值，null则返回0L
     * @param obj
     * @return
     */
    public static long getNumber(Object obj){
        if(obj == null){
            return 0;
        }
        return Long.valueOf((String)obj);
    }

    /**
     * 获取long值，null则返回0L
     * @param num
     * @return
     */
    public static long getNumber(Long num){
        if(num == null){
            return 0L;
        }
        return num;
    }

    /**
     * 获取int值，null则返回0
     * @param num
     * @return
     */
    public static int getNumber(Integer num){
        if(num == null){
            return 0;
        }
        return num;
    }

    /**
     * 三目运算 b 为true，返回参1，否则返回参2
     * @param b
     * @param num1
     * @param num2
     * @return
     */
    public static Integer trinomial(boolean b,Integer num1,Integer num2){

        return b ? num1 : num2;
    }

    /**
     * 三目运算 参1为null或非正整数，返回参2，否则返回参1
     * @param num1
     * @param num2
     * @return
     */
    public static Integer trinomial(Integer num1,Integer num2){
        return num1 == null ? num2 : num1;
    }

}
