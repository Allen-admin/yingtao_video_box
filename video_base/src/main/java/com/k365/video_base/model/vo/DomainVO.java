package com.k365.video_base.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Gavin
 * @date 2019/8/2 11:16
 * @description：
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DomainVO {

    /**
     * 域名
     */
    private String domain;

}
