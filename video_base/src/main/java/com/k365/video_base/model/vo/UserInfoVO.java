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
public class UserInfoVO {

    private String id;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 性别（M-男，F-女，X-未知）
     */
    private String gender;

    /**
     * 用户头像
     */
    private String userIcon;

    /**
     * 个人简介
     */
    private String profile;

    /**
     * 用户等级id
     */
    private Integer userLevel;

    /**
     * 手机号
     */
    private String phone;

    /**
     * vip到期时间
     */
    private Date vipEndTime;

    /**
     * VIP类型（0不是VIP 1是vip未到期 2VIP已到期）
     */
    private Integer vipType;

    /**
     * 是否最高权限
     */
    private Integer boss;

}
