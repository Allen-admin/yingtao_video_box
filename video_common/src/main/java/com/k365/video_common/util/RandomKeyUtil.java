package com.k365.video_common.util;

import com.k365.video_common.constant.SysParamNameEnum;
import org.apache.commons.codec.binary.Base32;

import java.security.SecureRandom;
import java.util.Random;
import java.util.UUID;

/**
 * @author Gavin
 * @date 2019/6/20 15:55
 * @description：
 */
public class RandomKeyUtil {

    /**
     * 得到32位uuid
     * @return
     */
    public static String getRandomUUID(){
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replaceAll("-","");
    }


    /**
     * @param length 随机数的长度,长度大于0，默认6位
     * @param res    产生随机数的资源，可不传，默认为62个大小字符和10为阿拉伯数字
     * @return
     */
    public static String getRandomStr(int length,String ... res){
        if(length < 0 ) length = 6;
        if (res == null || res.length != 1) {
            res = new String[1];
            res[0] = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        }
        Random random = new Random();
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < length ; i++) {
            int num = random.nextInt(res[0].length());
            buf.append(res[0].charAt(num));
        }
        return buf.toString();

    }

    /**
     * @param length 随机数的长度,长度大于0，默认6位
     * @return
     */
    public static String getRandomDigitStr(int length){
        if(length < 0 ) length = 6;
        String res = "0123456789";
        Random random = new Random();
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < length ; i++) {
            int num = random.nextInt(res.length());
            buf.append(res.charAt(num));
        }
        return buf.toString();

    }



    /**
     * @param length 随机数的长度,长度大于0，默认6位
     * @return
     */
    public static String getRandomDigitStrNotZero(int length){
        if(length < 0 ) length = 6;
        String res = "123456789";
        Random random = new Random();
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < length ; i++) {
            int num = random.nextInt(res.length());
            buf.append(res.charAt(num));
        }
        return buf.toString();

    }

    /**
     * 生成一个32位字母数字随机数
     * @return
     */
    public static String createSecretKey() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[20];
        random.nextBytes(bytes);
        Base32 base32 = new Base32();
        String secretKey = base32.encodeToString(bytes);
        return secretKey.toUpperCase();
    }

}
