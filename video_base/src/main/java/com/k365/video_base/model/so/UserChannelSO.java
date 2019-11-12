package com.k365.video_base.model.so;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Gavin
 * @date 2019/8/16 13:12
 * @description：
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserChannelSO extends BaseSO{

    /**
     * 用户等级id
     */
    private Integer userLevel;

    /**
     * mac地址
     */
    private String macAddr;

    /**
     * 最后登录ip
     */
    private String lastLoginIp;


    /**
     * VIP类型（1是vip）
     */
    private Integer vipType;

    /**
     * 推广渠道
     */
    private String channelCode;

    /**
     * 最近使用时长
     */
    private Long lastTime;


    /**
     * 注册开始时间
     */
    private String registerBeginTime;

    /**
     * 注册结束时间
     */
    private String registerEndTime;

    /**
     * 最后登录开始时间
     */
    private String lastloginBeginTime;

    /**
     * 最后登录结束时间
     */
    private String lastloginEndTime;

    /**
     * 终端（1是ios,2是andrid）
     */
    private Integer terminal;

    /**
     * 注册（1注册，其他未注册）
     */
    private Integer registered;

    /**
     *未登录超过天数
     */
    private  String notlogintDay;

    /**
     * 登录ip
     */
    private String loginIp;

}
