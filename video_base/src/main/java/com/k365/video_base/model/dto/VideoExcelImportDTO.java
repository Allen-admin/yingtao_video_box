package com.k365.video_base.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Gavin
 * @date 2019/9/16 16:54
 * @description：
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoExcelImportDTO implements BaseDTO {

    private String videoTitle;

    private String videoSaveUrl;

    private String videoPlayUrl;

    private String videoTimeLen;

    private String videoCover;

    private String videoCode;

    private String actor;

    private String channel;

    private String subject;

    private String label;

}


