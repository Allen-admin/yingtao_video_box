package com.k365.video_base.model.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.k365.video_base.model.vo.SysResourceVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Date;

/**
 * @author Gavin
 * @date 2019/7/19 19:22
 * @description：
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SysRoleDTO extends SplitPageDTO implements BaseDTO {

    @NotNull(groups = {Update.class,Remove.class},message = "角色id不能为空")
    private Integer id;

    @NotBlank(groups = {Add.class,Update.class},message = "角色名称不能为空")
    private String name;

    private Integer status;

    private Integer sort;

    private Date createDate;

    @TableField(exist = false)
    private Collection<Integer> resourcesIDs;

    @TableField(exist = false)
    private Collection<SysResourceVO> resources;

}
