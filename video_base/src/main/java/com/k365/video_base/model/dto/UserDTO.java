package com.k365.video_base.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author Gavin
 * @date 2019/7/23 20:26
 * @description：
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO extends SplitPageDTO implements BaseDTO {

    private String id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * mac地址
     */
    @NotBlank(groups = {Add.class},message = "mac地址不能为空")
    private String macAddr;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 是否为游客
     */
    private Boolean isVisitor;

    /**
     * 性别（M-男，F-女，X-未知）
     */
    private String gender ;

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
     * 推广码
     */
    private String recommendCode;

    /**
     * 推广二维码路径
     */
    private String spreadQrcodeUrl;

    /**
     * 短信验证码
     */
    private String verifyCode;

    /**
     * 推广码
     */
    private String spreadCode;
}
