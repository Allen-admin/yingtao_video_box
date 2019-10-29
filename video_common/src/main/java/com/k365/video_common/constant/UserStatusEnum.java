package com.k365.video_common.constant;

/**
 * @author Gavin
 * @date 2019/6/20 20:58
 * @description：
 */
public enum  UserStatusEnum {
    NORMAL(1,"正常"),
    STOP_USING(2,"停用"),
    LOCKED(3,"锁定"),
    DELETED(4,"删除");


    private int code;
    private String name;

    UserStatusEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int code() {
        return this.code;
    }

    public String getName() {
        return this.name;
    }

    public static UserStatusEnum getUserStatusName(int code){
        for (UserStatusEnum userStatus : UserStatusEnum.values()) {
            if(userStatus.code == code)
                return userStatus;
        }
        return null;
    }

    @Override
    public String toString() {
        return "{\\\"code\\\":" + code +
                ",\\\"name\\\":\\\"" + name+ "\\\"}" ;
    }
}
