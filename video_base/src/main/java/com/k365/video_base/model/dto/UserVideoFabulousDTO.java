package com.k365.video_base.model.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

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
public class UserVideoFabulousDTO {

    private Integer id;

    /**
     * 用户Id
     */
    private String userId;


    /**
     * 视频id
     */
    private String videoId;


    /**
     * 点赞时间
     */
    private Date createDate;
}
