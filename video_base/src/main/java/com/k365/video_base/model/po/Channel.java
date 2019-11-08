package com.k365.video_base.model.po;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author Allen
 * @since 2019-11-05
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Channel {

    @TableId(value = "id",type = IdType.AUTO)
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


}
