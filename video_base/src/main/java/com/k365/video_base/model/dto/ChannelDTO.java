package com.k365.video_base.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * <p>
 * <p>
 * </p>
 *
 * @author Allen
 * @since 2019-11-05
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChannelDTO extends SplitPageDTO implements BaseDTO {


    private Integer id;

    /**
     * 渠道名称
     */
    private String name;

    /**
     * 渠道链接
     */
    private String link;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     *渠道编号
     */
    private String channelCode;

    /**
     * 检索关键字 渠道编号+渠道名称
     */
    private String searchValue;

}
