package com.k365.video_base.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SysRole extends Model<SysRole>{

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String name;

    private Integer status;

    private Integer sort;

    @TableField(exist = false)
    private List<SysResource> resources;

    private Date createDate;

    @Override
    public Serializable pkVal() {
        return id;
    }

    @TableField(exist = false)
    private Collection<Integer> resourcesIDs;
}