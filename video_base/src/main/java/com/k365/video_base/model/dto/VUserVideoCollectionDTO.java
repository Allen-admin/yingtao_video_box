package com.k365.video_base.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Gavin
 * @date 2019/8/29 16:27
 * @description：
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VUserVideoCollectionDTO extends SplitPageDTO implements BaseDTO{

    /**
     * id
     */
    private String vId;

    /**
     * 视频状态
     */
    private Integer vStatus;

    /**
     * 视频标题
     */
    private String vTitle;

    /**
     * id
     */
    private String uId;

    /**
     * mac地址
     */
    private String uMacAddr;

}
