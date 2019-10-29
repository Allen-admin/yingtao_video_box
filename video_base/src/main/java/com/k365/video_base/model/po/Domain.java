package com.k365.video_base.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.Date;

import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author Gavin
 * @since 2019-07-17
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Domain{

    @TableId(value = "id", type = IdType.AUTO)
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
