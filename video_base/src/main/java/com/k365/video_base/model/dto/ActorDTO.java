package com.k365.video_base.model.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Allen
 * @date 2019/8/10 13:07
 * @description：
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActorDTO extends SplitPageDTO implements BaseDTO{

    private Integer id;

    /**
     * 女优姓名
     */
    private String name;

    /**
     * 女优头像
     */
    private String actorIcon;

    /**
     * 女优所属字母分类
     */
    private String acronym;

    /**
     * 女优热度值
     */
    private Integer hot;


}
