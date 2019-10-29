package com.k365.video_common.constant;

/**
 * @author Gavin
 * @date 2019/7/1 19:47
 * @description：
 */
public enum DataSourceTypeEnum {

    VIDEO_SITE_DB("video-site-db"),
    VIDEO_MANAGER_SYSTEM_DB("video-manager-system-db");

    private String value;

    DataSourceTypeEnum(String value) {
        this.value = value;
    }

    //    VIDEO_SITE_DB(0,"video-site-db","视频站点数据源"),
//    VIDEO_MANAGER_SYSTEM_DB(1,"video-manager-system-db","视频管理系统数据源");
//
//    private int key;
//    private String datasource;
//    private String desc;
//
//    DataSourceTypeEnum(int key,String datasource, String desc) {
//        this.key = key;
//        this.datasource = datasource;
//        this.desc = desc;
//    }
//
//    public static DataSourceTypeEnum getDataSourceType(int idType) {
//        DataSourceTypeEnum[] its = values();
//        DataSourceTypeEnum[] arr$ = its;
//        int len$ = its.length;
//
//        for(int i$ = 0; i$ < len$; ++i$) {
//            DataSourceTypeEnum it = arr$[i$];
//            if (it.getKey() == idType) {
//                return it;
//            }
//        }
//
//        return VIDEO_SITE_DB;
//    }
//
//    public int getKey() {
//        return this.key;
//    }
//
//    public String datasource() {
//        return this.datasource;
//    }
//
//    public String getDesc() {
//        return this.desc;
//    }
}
