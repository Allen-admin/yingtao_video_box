package com.k365.video_base.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author Gavin
 * @date 2019/7/17 14:09
 * @description：
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SysRoleVO {

    private Integer id;

    private String name;

    private Integer status;

    private Integer sort;

    private Collection<SysResourceVO> resources;

    private Collection<SysUserVO> sysUsers;

    private Date createDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SysRoleVO)) return false;
        if (!super.equals(o)) return false;
        SysRoleVO sysRoleVO = (SysRoleVO) o;
        return Objects.equals(id, sysRoleVO.id) &&
                Objects.equals(name, sysRoleVO.name) &&
                Objects.equals(status, sysRoleVO.status);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), id, name, status);
    }
}
