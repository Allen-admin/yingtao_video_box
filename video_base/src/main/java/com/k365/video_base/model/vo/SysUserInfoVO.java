package com.k365.video_base.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.Date;

/**
 * @author Gavin
 * @date 2019/7/17 14:07
 * @description：
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SysUserInfoVO {

    private String id;

    private String username;

    private String password;

    private String icon;

    private Date createDate;

    private Integer status;

    private Date lastLoginDate;

    private String nickname;

    private String secretKey;

    private String qrBarcodeUrl;

    private Collection<SysRoleVO> roles;


}
