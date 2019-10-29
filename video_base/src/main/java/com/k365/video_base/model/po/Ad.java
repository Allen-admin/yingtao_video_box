package com.k365.video_base.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
public class Ad {

    @TableId(type = IdType.UUID)
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
     * APP类型
     */
    private Integer appType;

}
