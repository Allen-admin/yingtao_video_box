package com.k365.video_base.model.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * <p>
 * VIEW
 * </p>
 *
 * @author Allen
 * @since 2019-11-06
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VUserChannel {

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
    private String cChannelCode;

    /**
     * 创建时间
     */
    private Date cCreateDate;

    /**
     * 总数
     */
    private Integer tRegisterCount;


}
