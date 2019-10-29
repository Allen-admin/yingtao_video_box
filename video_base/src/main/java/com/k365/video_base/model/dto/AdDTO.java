package com.k365.video_base.model.dto;

import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * @author Gavin
 * @date 2019/7/28 17:49
 * @description：
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdDTO extends SplitPageDTO implements BaseDTO{

    private String id;

    /**
     * 广告类型id
     */
    private Integer adType;

    /**
     * 广告展示位置
     */
    private Integer showType;

    /**
     * 广告标题
     */
    private String title;

    /**
     * 广告封面图片
     */
    private String cover;

    /**
     * 广告排序
     */
    private Integer sort;

    /**
     * 广告详情url
     */
    private String detailsUrl;

    /**
     * 广告内容·
     */
    private String content;

    /**
     * 广告状态
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * app 类型
     */
    private Integer appType;

}