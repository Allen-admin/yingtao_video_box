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
 * @author Gavin
 * @since 2019-07-17
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DomainDTO extends SplitPageDTO implements BaseDTO {

    private Integer id;

    /**
     * 域名类型
     */
    private Integer domainType;

    /**
     * 域名
     */
    private String domain;

    /**
     * 域名状态
     */
    private Boolean status;

    /**
     * 域名排序
     */
    private Integer sort;

    /**
     * 更新时间
     */
    private Date updateTime;


}
