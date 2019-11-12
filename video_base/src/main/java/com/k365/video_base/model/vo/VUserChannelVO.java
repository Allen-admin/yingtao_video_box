package com.k365.video_base.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

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
     * 名称
     */
    private String name;

    /**
     * 编号
     */
    private String channelCode;


    /**
     * 数量
     */
    private Integer channelNum;


}
