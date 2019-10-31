package com.k365.video_base.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

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
public class UserActionAnaylzeDTO extends SplitPageDTO implements BaseDTO {

    private String id;

    /**
     * 视频id
     */
    private String videoId;

    /**
     * 用户mac地址
     */
    private String macAddr;

    /**
     * 视频标签id
     */
    private String videoLabelId;

    /**
     * 创建时间
     */
    private Date crttime;


}
