package com.k365.video_common.constant;

/**
 * @author Gavin
 * @date 2019/7/8 19:20
 * @description：
 */
public enum ResourceTypeEnum {

    MENU(1,"一级菜单"),
    SUBMENU(2,"二级菜单"),
    BUTTON(3,"按钮");

    private int key;
    private String explain;

    public int key(){
        return key;
    }

    ResourceTypeEnum(int key, String explain) {
        this.key = key;
        this.explain = explain;
    }

    public static ResourceTypeEnum getResourceType(int key){
        for (ResourceTypeEnum resourceTypeEnum : ResourceTypeEnum.values()) {
            if(resourceTypeEnum.key == key){
                return resourceTypeEnum;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "{\\\"key\\\":" + key +
                ",\\\"explain\\\":\\\"" + explain+ "\\\"}" ;
    }
}
