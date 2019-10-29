package com.k365.video_base.common;


import com.google.common.base.Objects;

/**
 * @author Gavin
 * @date 2019/7/30 20:07
 * @description：
 */
public enum TaskTypeEnum {

    WELFARE_TASKS(1,"福利任务"),
    DAILY_TASKS(2,"每日任务"),
    PROMOTION_TASK(3,"推广任务");

    private int key;

    private String name;

    TaskTypeEnum(int key, String name) {
        this.key = key;
        this.name = name;
    }

    public int key() { return this.key; }

    public String getName() { return this.name; }

    public static TaskTypeEnum getByKey(int key){
        for(TaskTypeEnum taskTypeEnum : TaskTypeEnum.values()){
            if(Objects.equal(taskTypeEnum.key,key))
                return taskTypeEnum;
        }
        return null;
    }

    @Override
    public String toString() {
        return "{\\\"key\\\":" + key +
                ",\\\"name\\\":\\\"" + name + "\\\"}" ;
    }

}
