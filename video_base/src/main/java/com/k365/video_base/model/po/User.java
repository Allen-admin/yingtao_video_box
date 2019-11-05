package com.k365.video_base.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * <p>
 * </p>
 *
 * @author Gavin
 * @since 2019-07-17
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class User implements Serializable {

    private static final long serialVersionUID = -7353986491903228533L;

    @TableId(type = IdType.UUID)
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
     * 视频下载次数
     */
    private Integer saveCount;

    /**
     * 推广码
     */
    private String recommendCode;

    /**
     * 最后登录时间
     */
    private Date lastLoginTime;

    /**
     * 最后登录ip
     */
    private String lastLoginIp;

    /**
     * 账号状态
     */
    private Integer status;

    /**
     * 推荐用户数
     */
    private Integer recommendCount;

    /**
     * 已用下载次数
     */
    private Integer usedSaveCount;

    /**
     * 已用观影次数
     */
    private Integer usedViewingCount;

    /**
     * 总观影次数
     */
    private Integer viewingCount;

    /**
     * 推荐人id，和user表id对应
     */
    private String recommenderId;

    /**
     * 奖励观影次数
     */
    private Integer awardViewingCount;

    /**
     * 奖励下载次数
     */
    private Integer awardSaveCount;

    /**
     * 推广二维码路径
     */
    private String spreadQrcodeUrl;

    /**
     * app系统类型
     */
    private Integer appType;

    /**
     * vip到期时间
     */
    private Date vipEndTime;

    /**
     * VIP类型（0不是VIP 1是vip未到期 2VIP已到期）
     */
    private Integer vipType;

    /**
     * 推广渠道
     */
    private String registerChannel;

    /**
     * 最近使用时长
     */
    private Long lastTime;

}
