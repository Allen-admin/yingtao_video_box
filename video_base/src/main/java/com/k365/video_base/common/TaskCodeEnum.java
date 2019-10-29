package com.k365.video_base.common;

/**
 * @author Gavin
 * @date 2019/8/26 20:14
 * @description：
 */
public enum TaskCodeEnum {

    //福利任务
    FRIST_UP_PORTRAIT(1, "首次上传头像", TaskTypeEnum.WELFARE_TASKS),
    FRIST_BIND_PHONE(2, "首次绑定手机号", TaskTypeEnum.WELFARE_TASKS),
    SAVE_QR(3, "保存二维码", TaskTypeEnum.WELFARE_TASKS),

    //每日任务
    CLICK_AD(30,"点击游戏页面广告",TaskTypeEnum.DAILY_TASKS);



    private int key;

    private String name;

    private TaskTypeEnum taskType;

    TaskCodeEnum(int key, String name, TaskTypeEnum taskType) {
        this.key = key;
        this.name = name;
        this.taskType = taskType;
    }

    public int key() {
        return this.key;
    }

    public String getName() {
        return this.name;
    }

    public TaskTypeEnum getTaskType() {
        return taskType;
    }

    @Override
    public String toString() {
        return "{\\\"key\\\":" + key +
                ",\\\"name\\\":\\\"" + name + "\\\"}";
    }
}
