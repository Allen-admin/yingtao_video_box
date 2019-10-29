package com.k365.manager_service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.k365.video_base.model.po.AppVersion;
import com.k365.video_base.model.vo.AppVersionVO;
import com.k365.video_common.constant.AppTypeEnum;
import com.k365.video_common.constant.OSEnum;
import com.k365.video_common.util.AppInfo;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author Gavin
 * @since 2019-07-17
 */
public interface AppVersionService extends IService<AppVersion> {

    /**
     * App版本添加
     */
    void add(AppVersion appVersion);

    /**
     * App版本修改
     */
    void update(AppVersion appVersion);

    /**
     * app版本删除
     */
    void removeById(Integer id);

    /**
     * 根据os查询版本信息
     */
    List<AppVersion> findByOsType(OSEnum osEnum);

    /**
     * 根据os查询版本信息
     */
    List<AppVersion> findByVersionName(String name);

    /**
     * 根据os和版本查询
     */
    AppVersion findByVersionAndOs(OSEnum os, String versionName, AppTypeEnum appType);

    /**
     * 查询所有版本列表
     *
     * @return
     */
    List<AppVersion> findAll();

    /**
     * 检测最新版本
     */
    AppVersionVO findNewest(AppInfo appInfo);

    /**
     * 检测伪装数据开关
     */
    Boolean findArmorDataSwitch(AppInfo appInfo);

}
