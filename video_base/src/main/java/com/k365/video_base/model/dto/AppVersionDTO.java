package com.k365.video_base.model.dto;

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
public class AppVersionDTO extends SplitPageDTO implements BaseDTO {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * app版本类型id
     */
    private Integer osType;

    /**
     * app版本号
     */
    private String versionName;

    /**
     * 版本状态
     */
    private String status;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 下载url
     */
    private String appDownloadUrl;

    /**
     * 是否强制更新
     */
    private Boolean isForceUpdate;

    /**
     * app启动页logo
     */
    private String appUpImg;

    /**
     * APP类型
     */
    private Integer appType;
}
