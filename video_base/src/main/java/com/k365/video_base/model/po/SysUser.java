package com.k365.video_base.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SysUser implements Serializable {

    private static final long serialVersionUID = 5337874143102294463L;

    @TableId(type = IdType.UUID)
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

    @TableField(exist = false)
    private Collection<SysRole> roles;

    @TableField(exist = false)
    private Collection<SysResource> resources;

    @TableField(exist = false)
    private Integer tryLoginCount = 0;


}