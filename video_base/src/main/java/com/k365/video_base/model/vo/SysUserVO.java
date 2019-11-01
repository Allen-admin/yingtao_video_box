package com.k365.video_base.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SysUserVO {

    private String id;

    private String username;

    private String icon;

    private Date createDate;

    private Integer status;

    private String nickname;

    private Collection<String> roleNames;

    private Collection<Integer> roleIds;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SysUserVO)) return false;
        SysUserVO sysUserVO = (SysUserVO) o;
        return Objects.equals(id, sysUserVO.id) &&
                Objects.equals(username, sysUserVO.username) &&
                Objects.equals(status, sysUserVO.status);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), id, username, status);
    }

}