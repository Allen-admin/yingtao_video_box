package com.k365.video_base.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @author Allen
 * @date 2019/9/27 19:06
 * @description：
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UmengResultVo {

    //活跃用户数
    private Integer activityUsers;

    //新用户数
    private Integer newUsers;

    //推广用户数
    private Integer launches;

    //总用户数
    private Integer totalUsers;

}
