package com.k365.video_base.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Gavin
 * @date 2019/6/29 21:44
 * @description：
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SysResourceVO {

    private Integer id;

    private String name;

    private String icon;

    private String url;

    private String permitCode;

    private Short typeCode;

    private Integer sort;

    private List<SysResourceVO> submenus;

    private Integer parentId;

    private Boolean verification;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SysResourceVO)) return false;
        if (!super.equals(o)) return false;
        SysResourceVO that = (SysResourceVO) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(icon, that.icon) &&
                Objects.equals(url, that.url) &&
                Objects.equals(sort, that.sort) &&
                Objects.equals(submenus, that.submenus);
    }

    @Override
    public int hashCode() {

        return Objects.hash(super.hashCode(), name, icon, url, sort, submenus);
    }

}
