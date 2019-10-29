package com.k365.video_common.container;

/**
 * @author Gavin
 * @date 2019/7/7 20:53
 * @description：
 */
public class EnhanceMap<I, K, V> {

    private I index;
    private K key;
    private V value;

    public EnhanceMap(I index, K key, V value) {
        this.index = index;
        this.key = key;
        this.value = value;
    }
}
