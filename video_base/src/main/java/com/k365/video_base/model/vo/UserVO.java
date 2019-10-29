package com.k365.video_base.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author Gavin
 * @date 2019/7/23 20:27
 * @description：
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserVO {

    private String id;

    /**
     * 用户等级id
     */
    private Integer userLevel;

    /**
     * 注册时间
     */
    private Date registerTime;

    /**
     * 手机号
     */
    private String phone;

    /**
     * mac地址
     */
    private String macAddr;

    /**
     * 用户名
     */
    private String nickname;

    /**
     * app类型
     */
    private Integer appType;

    /**
     * 账号状态
     */
    private Integer status;

    /**
     * vip到期时间
     */
    private Date vipEndTime;

    /**
     * VIP类型（0不是VIP 1是vip未到期 2VIP已到期）
     */
    private Integer vipType;

}
