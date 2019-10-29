package com.k365.video_base.common;

/**
 * @author Gavin
 * @date 2019/8/1 15:13
 * @description：
 */
public enum AppVideoTopLabelEnum {

    RECOMMEND_VIDEO("recommend", "首页", 1),
    SUBJECT_VIDEO("subject", "专题", 2),
    LATEST_VIDEO("latest", "最新", 3),
    HOTTEST_VIDEO("hottest", "最热", 4);
//    ACTOR("actor", "女优", 5);


//    FEATURED_VIDEO(1,"精选视频"),
//    U_LIKES_VIDEO(2,"猜你喜欢视频"),
//    RELEVANT_VIDEO(6,"相关视频");

    private String code;

    private String name;

    private int sort;

    AppVideoTopLabelEnum(String code, String name, int sort) {
        this.code = code;
        this.name = name;
        this.sort = sort;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public int getSort() {
        return sort;
    }

    @Override
    public String toString() {
        return "{\\\"code\\\":" + code +
                ",\\\"name\\\":\\\"" + name + "\\\"" +
                ",\\\"sort\\\":\\\"" + sort + "\\\"}" ;
    }

}


