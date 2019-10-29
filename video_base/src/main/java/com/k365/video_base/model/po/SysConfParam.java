package com.k365.video_base.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.k365.video_common.constant.SysParamValueNameEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SysConfParam {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String paramName;

    private String paramCode;

    private Integer paramStatus;

    private String paramRemark;

    private String paramValue;

    @TableField(exist = false)
    private List<SysParamValueNameEnum> paramValueName;

}