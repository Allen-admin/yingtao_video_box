package com.k365.video_base.model.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SysUserDTO extends SplitPageDTO implements BaseDTO{
    private String id;

    @NotBlank(groups = {Login.class},message = "用户名不能为空")
    private String username;

    @NotBlank(groups = {Login.class},message = "密码不能为空")
    private String password;

    @TableField(exist = false)
    private Collection<Integer> roleIds;

    private String icon;

    private Date createDate;

    private Integer status;

    private Date lastLoginDate;

    private String nickname;

    private String secretKey;

    private String qrBarcodeUrl;

    private String authCode;

    //  确认密码
    @TableField(exist = false)
    private String conf_password;
}