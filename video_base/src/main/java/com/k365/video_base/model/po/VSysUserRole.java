package com.k365.video_base.model.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * <p>
 * VIEW
 * </p>
 *
 * @author Gavin
 * @since 2019-07-17
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VSysUserRole {

    /**
     * 管理员账号
     */
    private String username;

    /**
     * 状态（1-正常，2-停用，3-锁定，4-已删除）
     */
    private Integer userStatus;

    private String userId;

    /**
     * 管理用户图标
     */
    private String userIcon;

    /**
     * 创建时间
     */
    private Date userCreateDate;

    /**
     * 昵称
     */
    private String userNickname;

    /**
     * 角色名称
     */
    private String roleName;

    private Integer roleId;

    /**
     * 角色状态（状态（1-正常，2-停用，3-锁定，4-已删除））
     */
    private Integer roleStatus;

    /**
     * 排序
     */
    private Integer roleSort;


}
