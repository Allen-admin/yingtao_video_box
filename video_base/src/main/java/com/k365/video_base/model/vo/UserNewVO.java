package com.k365.video_base.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserNewVO {

    //姓名
    private String name;

    //mac地址
    private String macAddr;

    //手机号
    private String phone;

    //注册时间
    private Date registerTime;

    //推荐总数
    private int recommendCount;
}
