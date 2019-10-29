package com.k365.video_base.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SysResource {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String name;

    private Short typeCode;

    private Integer parentId;

    private String permitCode;

    private String icon;

    private Integer sort;

    private String url;

    private Date createDate;

    private Boolean verification;

    @TableField(exist = false)
    private List<SysResource> children;

}