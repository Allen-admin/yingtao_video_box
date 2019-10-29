package com.k365.video_base.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Gavin
 * @date 2019/7/3 21:16
 * @description：自定义配置信息
 */
@Component
public class CustomAttr {

    @Value("${custom-attr.fastdfs.storage-server}")
    private String fastdfsServer;

    @Value("${custom-attr.fastdfs.config}")
    private String fastdfsConfig;

    @Value("${custom-attr.swagger2-ui-switch}")
    private Boolean swaggerUiSwitch;

    @Value("${custom-attr.druid-view.login-name}")
    private String druidViewName;

    @Value("${custom-attr.druid-view.login-password}")
    private String druidViewPwd;

    @Value("${custom-attr.druid-view.ip-allow}")
    private String druidViewIpAllow;

    @Value("${custom-attr.druid-view.ip-deny}")
    private String druidViewIpDeny;

    @Value("${server.servlet.context-path}")
    private String servletContextPath;

    public String getFastdfsServer() {
        return fastdfsServer == null ? "" : fastdfsServer.trim();
    }

    public String getFastdfsConfig() {
        return fastdfsConfig == null ? "" : fastdfsConfig.trim();
    }

    public Boolean getSwaggerUiSwitch() {
        return swaggerUiSwitch;
    }

    public String getDruidViewName() {
        return druidViewName;
    }

    public String getDruidViewPwd() {
        return druidViewPwd;
    }

    public String getDruidViewIpAllow() {
        return druidViewIpAllow;
    }

    public String getDruidViewIpDeny() {
        return druidViewIpDeny;
    }

    public String getServletContextPath() {
        return servletContextPath;
    }
}
