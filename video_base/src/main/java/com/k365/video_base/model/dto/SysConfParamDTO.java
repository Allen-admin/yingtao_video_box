package com.k365.video_base.model.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * @author Gavin
 * @date 2019/7/17 13:08
 * @description：
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SysConfParamDTO extends SplitPageDTO implements BaseDTO{

    private Integer id;

    @NotBlank(groups = {Add.class},message = "参数名不能为空")
    private String paramName;

    private String paramCode;

    private Integer paramStatus;

    private String paramRemark;

    @NotNull(groups = {Add.class},message = "参数值不能为空")
    private Map<String,Object> paramValue;

    @TableField(exist = false)
    private Object paramValueName;
}
