package com.k365.video_common.util;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.Key;
import java.security.MessageDigest;
import java.security.SecureRandom;

/**
 * @author Gavin
 * @date 2019/6/21 11:31
 * @description：
 */
public class Encrypt {
    /**
     * AES 加密默认秘钥
     */
    private static final String AES_SECRET_KEY0 = "F3Fsdf1CMU5ysX8A";

    /**
     * AES 加密默认秘钥
     */
    private static final String AES_SECRET_KEY = "F3FANKVMH6W6H41roBI4MHHCMU0Isf1744bae02f9c25UyOTIs973ad30AYQBECUwODgU492fHW2PXKIMGOKJKGF4OBA72e77380iOjE1Nbfb04deHSUD85Hf53623e7";

    /**
     * 加密算法
     */
    private static final String AES_KEY_ALGORITHM = "AES";

    /**
     * "算法/模式/补码方式"
     */
    private static final String AES_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";

    /**
     * 编解码
     */
    private static final String ENCODING = "utf-8";

    /**
     * MD5加密-32位小写
     */
    public static String md5(String encryptStr) {
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] md5Bytes = md5.digest(encryptStr.getBytes());
            StringBuffer hexValue = new StringBuffer();
            for (int i = 0; i < md5Bytes.length; i++) {
                int val = ((int) md5Bytes[i]) & 0xff;
                if (val < 16)
                    hexValue.append("0");
                hexValue.append(Integer.toHexString(val));
            }
            encryptStr = hexValue.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return encryptStr;
    }

    /**
     * MD5加密-16位小写
     */
    public static String md5_16(String encryptStr) {
        return md5(encryptStr).substring(8, 24);
    }

    /**
     * base64加密
     */
    public static String base64Encode(String str) {
        byte[] b = null;
        String s = null;
        try {
            b = str.getBytes(ENCODING);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (b != null) {
            s = new BASE64Encoder().encode(b);
        }
        return s;
    }

    /**
     * Base64解密
     */
    public static String base64Decode(String s) {
        byte[] b = null;
        String result = null;
        if (s != null) {
            BASE64Decoder decoder = new BASE64Decoder();
            try {
                b = decoder.decodeBuffer(s);
                result = new String(b, ENCODING);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * AES 加密
     *
     * @param str
     * @return
     * @throws Exception
     */
    public static String aesEncrypt(String str) throws Exception {
        return aesEncrypt(str, AES_SECRET_KEY);
    }

    /**
     * AES 加密2
     *
     * @param str
     * @return
     * @throws Exception
     */
    public static String aesEncrypt2(String str) throws Exception {
        String results="";
        try {
            //生成密钥
            KeyGenerator keyGenerator = KeyGenerator.getInstance(AES_KEY_ALGORITHM);
            keyGenerator.init(new SecureRandom());
            SecretKey secretKey = keyGenerator.generateKey();
            byte[] keyBytes = secretKey.getEncoded();

            //转换密钥
            Key key = new SecretKeySpec(keyBytes, AES_KEY_ALGORITHM);

            //加密
            Cipher cipher = Cipher.getInstance(AES_CIPHER_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] result = cipher.doFinal(str.getBytes());
            results=new BigInteger(1, result).toString(16);

           /* //解密
            cipher.init(Cipher.DECRYPT_MODE, key);
            result = cipher.doFinal(result);
            System.out.println(new String(result));*/
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }

    /**
     * AES 加密
     *
     * @param str
     * @param key 秘钥
     * @return
     * @throws Exception
     */
    public static String aesEncrypt(String str, String key) throws Exception {

        byte[] raw = key.getBytes(ENCODING);
        SecretKeySpec skeySpec = new SecretKeySpec(raw, AES_KEY_ALGORITHM);
        Cipher cipher = Cipher.getInstance(AES_CIPHER_ALGORITHM);
        //使用CBC模式，需要一个向量iv，可增加加密算法的强度
        IvParameterSpec iv = new IvParameterSpec(AES_SECRET_KEY.getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
        byte[] encrypted = cipher.doFinal(str.getBytes());
        return new String(encrypted);
    }

    /**
     * AES 解密
     *
     * @param str
     * @return
     * @throws Exception
     */
    public static String aesDecrypt(String str) throws Exception {
        return aesDecrypt(str, AES_SECRET_KEY);
    }

    /**
     * AES 解密
     *
     * @param str
     * @param key 秘钥
     * @return
     * @throws Exception
     */
    public static String aesDecrypt(String str, String key) throws Exception {
        byte[] raw = key.getBytes(ENCODING);
        SecretKeySpec skeySpec = new SecretKeySpec(raw, AES_KEY_ALGORITHM);
        Cipher cipher = Cipher.getInstance(AES_CIPHER_ALGORITHM);
        IvParameterSpec iv = new IvParameterSpec(key.getBytes());
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
        byte[] original = cipher.doFinal(str.getBytes());
        return new String(original);
    }

    /**
     * AES 解密2
     *
     * @param str
     * @return
     * @throws Exception
     */
    public static String aesDecrypt2(String str) throws Exception {
        String results="";
        try {
            //生成密钥
            KeyGenerator keyGenerator = KeyGenerator.getInstance(AES_KEY_ALGORITHM);
            keyGenerator.init(new SecureRandom());
            SecretKey secretKey = keyGenerator.generateKey();
            byte[] keyBytes = secretKey.getEncoded();

            //转换密钥
            Key key = new SecretKeySpec(keyBytes, AES_KEY_ALGORITHM);

            //加密
            Cipher cipher = Cipher.getInstance(AES_CIPHER_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);
           /* byte[] result = cipher.doFinal(str.getBytes());
            results=new BigInteger(1, result).toString(16);*/

            //解密
            byte[] bytes = str.getBytes();
            cipher.init(Cipher.DECRYPT_MODE, key);
            results=new String( cipher.doFinal(bytes));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return results;
    }

   /* public static void main(String[] args) throws Exception{
        String str = "中国 2018 IS Good";
        String aesEncrypt = aesEncrypt(str);
        System.out.println(aesEncrypt);

        System.out.println(aesDecrypt(aesEncrypt));
    }*/

   /* public static void main(String[] args) {
        String key2 = "RjNGQU5LVk1INlc2SDQxcm9CSTRNSEhDTVUwSXNmMTc0NGJhZTAyZjljMjVVeU9USXM5NzNhZDMwQVlRQkVDVXdPRGdVNDkyZkhXMlBYS0lNR09LSktHRjRPQkE3MmU3NzM4MGlPakUxTmJmYjA0ZGVIU1VEODVIZjUzNjIzZTc=";
        System.out.println(key2);
        try {
            //生成密钥
            KeyGenerator keyGenerator = KeyGenerator.getInstance(AES_KEY_ALGORITHM);
            keyGenerator.init(new SecureRandom());
            SecretKey secretKey = keyGenerator.generateKey();
            byte[] keyBytes = secretKey.getEncoded();

            //转换密钥
            Key key = new SecretKeySpec(keyBytes, AES_KEY_ALGORITHM);

            //加密
            Cipher cipher = Cipher.getInstance(AES_CIPHER_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] result = cipher.doFinal("http://www.ludobe.net/group1/M00/00/C5/uY3Nml1mWtOAD8nkAAAOWg-yQOw44.m3u8".getBytes());
            System.out.println(new BigInteger(1, result).toString(16)+"======");

            //解密
            cipher.init(Cipher.DECRYPT_MODE, key);
            result = cipher.doFinal(result);
            System.out.println(new String(result));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

}
