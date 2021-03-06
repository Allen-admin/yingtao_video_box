package com.k365.video_base.model.vo;

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
 * @author Baozi
 * @since 2019-10-21
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ManagerGroupVO {

    private Integer id;

    //组名
    private String name;

    //创建时间
    private Date crttime;

    //更新时间
    private Date upttime;

    //组长名字
    private String leaderName;

}
