package com.k365.video_base.model.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author Baozi
 * @date 2019/10/21
 * @description：
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ManagerEmployeeDTO extends SplitPageDTO implements BaseDTO{

    private Integer id;

    //用户名
    private String username;

    //密码
    private String password;

    //mac地址
    private String macAddr;

    //组ID
    private Integer groupId;

    //员工姓名
    private String name;

    //手机号
    private String phone;

    //用户级别
    private Integer level;

    //创建时间
    private Date crttime;

    //更新时间
    private Date upttime;

    //推广人数
    private Integer recommendCount;

    //备注
    private String remark;

    //appid
    private String appid;
}
