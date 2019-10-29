package com.k365.video_base.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
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
 * @since 2019-07-17
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Actor {

    @TableId(value = "id", type = IdType.AUTO)
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
