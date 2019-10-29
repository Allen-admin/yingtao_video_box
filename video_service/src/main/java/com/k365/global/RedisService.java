package com.k365.global;

/**
 * @author Gavin
 * @date 2019/6/20 18:29
 * @description：
 */
public interface RedisService {

    boolean setString(String key, String value);

    String getString(String key);

    boolean expire(String key, long expire);

    boolean hasKey(String key);
}
