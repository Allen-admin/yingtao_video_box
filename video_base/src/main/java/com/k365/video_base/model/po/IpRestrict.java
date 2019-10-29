package com.k365.video_base.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * ip黑白名单限制表
 * </p>
 *
 * @author Gavin
 * @since 2019-09-11
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class IpRestrict implements Serializable{


    private static final long serialVersionUID = 1268014717928714250L;

    /**
     * 白名单
     */
    public static final Integer WHITE_LIST = 1;

    /**
     * 黑名单
     */
    public static final Integer BLACKLIST = 2;


    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * ip地址
     */
    private String ip;

    /**
     * 限制类型（1-白名单，2黑名单）
     */
    private Integer restrictType;

    /**
     * 访问系统（1-所有系统，2-前台系统，3-后台管理系统）
     */
    private Integer project;

    /**
     * 限制方式（1-单个ip限制，2-ip网段限制，3-ip地区限制）
     */
    private Integer restrictWay;

    /**
     * 国家
     */
    private String country;

    /**
     * 省份
     */
    private String region;

    /**
     * 城市
     */
    private String city;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 网段开始ip
     */
    private String ipFrom;

    /**
     * 网段结束ip
     */
    private String ipEnd;

    /**
     * 状态，true-开启，false-关闭
     */
    private Boolean status;


    @Override
    public String toString() {

        return "{id:"+ id +
                ",ip:" + ip +
                ",restrictType:" +  restrictType +
                ",project:" + project +
                ",restrictWay:" + restrictWay +
                ",country:" + country +
                ",region:" + region +
                ",city:" + city +
                ",remarks:" + remarks +
                ",ipFrom:" + ipFrom +
                ",ipEnd:" + ipEnd +
                ",status:" + status +
                "}";
    }
}
