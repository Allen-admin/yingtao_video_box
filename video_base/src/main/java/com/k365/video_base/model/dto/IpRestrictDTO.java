package com.k365.video_base.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Gavin
 * @date 2019/9/12 15:43
 * @description：
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IpRestrictDTO extends SplitPageDTO implements BaseDTO {

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
}
