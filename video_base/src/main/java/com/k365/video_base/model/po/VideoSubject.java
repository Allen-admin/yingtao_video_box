package com.k365.video_base.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * <p>
 * </p>
 *
 * @author Gavin
 * @since 2019-08-14
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VideoSubject {


    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 专题名称
     */
    private String name;

    /**
     * 专题封面
     */
    private String cover;

    /**
     * 专题排序
     */
    private Integer sort;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 专题描述
     */
    private String content;

    /**
     * 视频数量
     */
    @TableField(exist = false)
    private Integer videoTotal;

    /**
     * 主题类型
     */
    private Integer subjectType;

    /**
     * 主题图标
     */
    private String icon;

}


