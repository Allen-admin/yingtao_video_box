package com.k365.video_base.model.so;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author Gavin
 * @date 2019/8/16 13:12
 * @description：
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VUserChannelSO extends BaseSO{

    /**
     * id
     */
    private String cId;

    /**
     * 名称
     */
    private String cName;

    /**
     *
     */
    private String vChannelCode;

    /**
     * 创建时间
     */
    private Date cCreateDate;

    /**
     * 总数
     */
    private Integer tRegisterCount;


    /**
     * 检索关键字 渠道编号+渠道名称
     */
    private String searchValue;


    /**
     * 开始时间
     */
    private String beginTime;

    /**
     * 结束时间
     */
    private String endTime;

}
