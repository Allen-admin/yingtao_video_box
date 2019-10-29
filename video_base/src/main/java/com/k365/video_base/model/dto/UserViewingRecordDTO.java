package com.k365.video_base.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
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
public class UserViewingRecordDTO extends SplitPageDTO implements BaseDTO {

    private String id;

    private String userId;

    private String videoId;

    private Date playTimeLen;

    private Date recordTime;

    private String versionResolution;


}
