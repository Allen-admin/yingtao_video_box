package com.k365.video_base.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

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
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class AppVersion {

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
     * 伪装数据开关
     */
    private Boolean armorData;

    /**
     * 更新内容
     */
    private String content;

    /**
     * APP类型
     */
    private Integer appType;


}
