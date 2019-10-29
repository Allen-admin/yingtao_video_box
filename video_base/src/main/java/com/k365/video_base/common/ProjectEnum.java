package com.k365.video_base.common;

/**
 * @author Gavin
 * @date 2019/9/12 19:19
 * @description：
 */
public enum ProjectEnum {

    ALL_PROJECT(1, "/**"),
    VIDEO_SITE(2, "/videosite"),
    VIDEO_MANAGER(3, "/videomanager");

    private int code;

    private String path;

    ProjectEnum(int code, String path) {
        this.code = code;
        this.path = path;

    }

    public int code() {
        return this.code;
    }

    public String getPath() {
        return this.path;
    }

    public static ProjectEnum getByPath(String path) {
        for (ProjectEnum project : values()) {
            if (project.path.equalsIgnoreCase(path.trim())) {
                return project;
            }
        }
        return null;
    }


    @Override
    public String toString() {
        return "{\\\"code\\\":" + code +
                ",\\\"path\\\":\\\"" + path + "\\\"}";
    }
}
