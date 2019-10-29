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
public class ManagerEmployeeVO {

    private Integer id;

    //mac地址
    private String macAddr;

    //用户名
    private String username;

    //组ID
    private Integer groupId;

    //员工姓名
    private String name;

    //手机号
    private String phone;

    //创建时间
    private Date crttime;

    //用户层级
    private Integer level;

    //更新时间
    private Date upttime;

    //推广人数
    private Integer recommendCount;

    //备注
    private String remark;

    //组名
    private String groupName;
}
