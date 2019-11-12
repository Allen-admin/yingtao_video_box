package com.k365.video_base.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserChannelVO {

    /**
     * 用户名
     */
    private String nickname;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 最后登录时间
     */
    private Date lastLoginTime;


    /**
     * 最近使用时长
     */
    private Long lastTime;


    /**
     * 最后登录ip
     */
    private String lastLoginIp;


    /**
     * 用户等级id
     */
    private Integer userLevel;

    /**
     * vip到期时间
     */
    private Date vipEndTime;

    /**
     * 注册时间
     */
    private Date registerTime;

    /**
     *根据macAddr,判断终端ios,安卓
     */
    private String  terminal;

    /**
     * 所属地
     */
    private String address;

}
