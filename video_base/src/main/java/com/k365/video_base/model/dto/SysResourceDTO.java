package com.k365.video_base.model.dto;

import lombok.Data;

import java.util.Date;

/**
 * @author Gavin
 * @date 2019/6/29 21:44
 * @description：
 */
@Data
public class SysResourceDTO extends SplitPageDTO implements BaseDTO {

    private Integer id;

    private Short typeCode;

    private Integer parentId;

    private String name;

    private String permitCode;

    private String icon;

    private Integer sort;

    private String url;

    private Date createDate;

    private Boolean verification;


}
