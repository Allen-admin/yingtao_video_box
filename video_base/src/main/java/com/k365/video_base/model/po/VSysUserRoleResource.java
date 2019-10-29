package com.k365.video_base.model.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VSysUserRoleResource {
    private String username;

    private Integer userStatus;

    private String userId;

    private String roleName;

    private Integer roleId;

    private Integer roleStatus;

    private Integer roleSort;

    private Integer resourceSort;

    private String resourceName;

    private Short resourceTypeCode;

    private Integer resourceParentId;

    private String resourcePermitCode;

    private Boolean resourceVerification;

    private String resourceUrl;

    private Integer resourceId;

    private String resourceIcon;

}
