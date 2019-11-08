package com.k365.video_base.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author Allen
 * @date 2019/8/29 16:33
 * @description：
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class VUserChannelVO {

    /**
     * id
     */
    private Integer cId;

    /**
     * 名称
     */
    private String cName;

    /**
     *编号
     */
    private String cChannelCode;

    /**
     * 创建时间
     */
    private Date cCreateDate;

    /**
     * 数量
     */
    private Integer tRegisterCount;


}
