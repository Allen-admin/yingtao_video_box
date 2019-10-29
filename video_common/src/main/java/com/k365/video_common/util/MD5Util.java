package com.k365.video_common.util;

import org.apache.commons.codec.digest.DigestUtils;

import java.security.MessageDigest;

import static com.k365.video_common.util.RandomKeyUtil.createSecretKey;
import static com.k365.video_common.util.RandomKeyUtil.getRandomUUID;

/**
 * @author Gavin
 * @date 2019/6/22 9:56
 * @description：
 */
public class MD5Util {

    public final static String CIPHERTEXT = "0c2148cf5454b04sd4f12311ds3375656930f4883cb";

    public static String mixKeyMD5(String text) {
        return md5(doubleMD5(text,CIPHERTEXT));
    }

    public static String doubleMD5(String text, String key) {
        return md5(md5(text,key));
    }

    public static String md5(String text) {
        return DigestUtils.md5Hex(text);
    }

    public static boolean verify(String text, String md5){
        //根据传入的密钥进行验证
        String md5Text = md5(text);
        if(md5Text.equalsIgnoreCase(md5))
        {
            return true;
        }
        return false;
    }


    /**
     * md5加密
     * @param encryptStr
     * @return
     */
    public static String encrypt(String encryptStr) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] md5Bytes = md5.digest(encryptStr.getBytes());
            StringBuilder hexValue = new StringBuilder();
            byte[] var4 = md5Bytes;
            int var5 = md5Bytes.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                byte md5Byte = var4[var6];
                int val = md5Byte & 255;
                if (val < 16) {
                    hexValue.append("0");
                }

                hexValue.append(Integer.toHexString(val));
            }

            encryptStr = hexValue.toString();
            return encryptStr;
        } catch (Exception var9) {
            throw new RuntimeException(var9);
        }
    }
    /**
     * MD5加密
     *
     * @param text 明文
     * @param key 密钥
     * @return 密文
     */
    public static String md5(String text, String key) {
        return DigestUtils.md5Hex(text + key);
    }

    /**
     * MD5验证方法
     *
     * @param text 明文
     * @param key 密钥
     * @param md5 密文
     * @return true/false
     */
    public static boolean verify(String text, String key, String md5){
        //根据传入的密钥进行验证
        String md5Text = md5(text, key);
        if(md5Text.equalsIgnoreCase(md5))
        {
            return true;
        }
        return false;
    }

}
