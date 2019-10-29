package com.k365.video_base.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author Gavin
 * @date 2019/9/11 18:36
 * @description：
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SysLogDTO extends SplitPageDTO implements BaseDTO{

    private Integer id;

    /**
     * 操作时间
     */
    private Date time;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户ip
     */
    private String userIp;

    /**
     * 操作功能
     */
    private String operate;

    /**
     * 是否成功（true-成功，false-失败）
     */
    private Boolean status;

    /**
     * 访问路径
     */
    private String accessPath;

}
