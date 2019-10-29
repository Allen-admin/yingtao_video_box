package com.k365.video_base.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Gavin
 * @date 2019/8/20 13:34
 * @description：
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppVersionVO {

    private Integer id;
    /**
     * app版本号
     */
    private String versionName;
    /**
     * 更新内容
     */
    private String content;
    /**
     * 是否强制更新
     */
    private Boolean isForceUpdate;
    /**
     * 下载url
     */
    private String appDownloadUrl;

}
