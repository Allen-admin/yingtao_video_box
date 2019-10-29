package com.k365.video_base.model.vo;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Allen
 * @date 2019/8/10 13:35
 * @description：
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActorVO {

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
}
