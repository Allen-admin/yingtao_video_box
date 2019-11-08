package com.k365.video_base.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author Allen
 * @date 2019/11/6
 * @description：
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VUserChannelDTO extends SplitPageDTO implements BaseDTO{

    /**
     * cId
     */
    private Integer cId;

    /**
     * 名称
     */
    private String cName;

    /**
     * 编号
     */
    private String cChannelCode;


    /**
     * 创建时间
     */
    private Date cCreateDate;

    /**
     * 检索关键字 渠道编号+渠道名称
     */
    private String searchValue;

}
