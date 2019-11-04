package com.k365.video_common.util;

import java.util.*;

/**
 * @author Gavin
 * @date 2019/11/2 13:18
 * @description：
 */
public class MyHashMap<K, V> extends HashMap<K, V> {

    /**
     * key相同 Integer类型相加
     */
    public V putInteger(K key, V value) {
        V newV = value;
        if (containsKey(key)) {
            V oldv = get(key);
            newV = (V) (Integer.valueOf((Integer) oldv + (Integer) newV));
        }
        return super.put(key, newV);
    }

    /**
     * key相同 String类型追加
     */
    public V putString(K key,V value){
        V newV = value;
        if(containsKey(key)){
            V oldv = get(key);
            newV =(V)(oldv+"--"+newV);
        }
        return super.put(key, newV);
    }


    //降序排序
    public <K, V extends Comparable<? super V>> Map<K, V> sortByValueDescending(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
            public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
                int compare = (o1.getValue()).compareTo(o2.getValue());
                return -compare;
            }
        });

        Map<K, V> result = new LinkedHashMap<K, V>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    //升序排序
    public <K, V extends Comparable<? super V>> Map<K, V> sortByValueAscending(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
            public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
                int compare = (o1.getValue()).compareTo(o2.getValue());
                return compare;
            }
        });

        Map<K, V> result = new LinkedHashMap<K, V>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

    public static void main(String[] Args) {
        MyHashMap map = new MyHashMap<>();

//        map.putInteger(100000000, 1);
//        map.putInteger(100000000, 1);
//        map.putInteger(100000000, 1);
//        map.putInteger(1000000001, 1);
//        System.out.println("map size:" + map.size());
//        System.out.println("testing same key value count:" + map.get(100000000));
//        System.out.println("testing same key value count:" + map.get(1000000001));

        map.putInteger(1, 8);
        map.putInteger(2, 58);
        map.putInteger(3, 2);
        map.putInteger(4, 90);
        map.putInteger(5, 89);
        map.putInteger(6, 87);

        System.out.println("map:" + map.sortByValueDescending(map));
        System.out.println("map:" + map);


        Map<Integer, Integer> probs = new TreeMap<Integer, Integer>();
        probs = map.sortByValueDescending(map);

        List<Integer> labelIdList = new ArrayList<>();

        int flag = 0;
        for (Map.Entry<Integer, Integer> entry : probs.entrySet()) {
            if (flag < 4) {
                flag++;
                labelIdList.add(entry.getKey());
                System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
            }

        }

        System.out.println("lableIdList:"+labelIdList.size()+"-------------"+labelIdList);
    }
}
