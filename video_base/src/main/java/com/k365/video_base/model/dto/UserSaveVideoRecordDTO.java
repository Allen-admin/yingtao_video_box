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
public class UserSaveVideoRecordDTO extends SplitPageDTO implements BaseDTO {

    private String id;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 视频id
     */
    private String videoId;

    /**
     * 视频下载记录时间
     */
    private Date recordTime;

    /**
     * 视频大小
     */
    private Double videoSize;

    /**
     * 已下载大小
     */
    private Double saveVideoSize;

    /**
     * 视频下载状态
     */
    private Integer saveStatus;


}
