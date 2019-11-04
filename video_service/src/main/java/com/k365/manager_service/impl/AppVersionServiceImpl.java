package com.k365.manager_service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.k365.manager_service.AppVersionService;
import com.k365.video_base.common.VideoContants;
import com.k365.video_base.mapper.AppVersionMapper;
import com.k365.video_base.model.po.AppVersion;
import com.k365.video_base.model.vo.AppVersionVO;
import com.k365.video_common.constant.AppTypeEnum;
import com.k365.video_common.constant.OSEnum;
import com.k365.video_common.constant.StatusEnum;
import com.k365.video_common.exception.ResponsiveException;
import com.k365.video_common.util.AppInfo;
import com.k365.video_common.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.ListUtils;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Gavin
 * @since 2019-07-17
 */
@Service
@Slf4j
public class AppVersionServiceImpl extends
        ServiceImpl<AppVersionMapper, AppVersion> implements AppVersionService {

    @Autowired
    private RedisUtil cache;

    @Override
    public void add(AppVersion appVersion) {
        AppVersion appVersion2 = this.getOne(new UpdateWrapper<AppVersion>().eq("app_type", appVersion.getAppType())
                .eq("os_type", appVersion.getOsType()).eq("version_name", appVersion.getVersionName()));

        if (appVersion2 != null)
            throw new ResponsiveException("版本号已存在");

        appVersion.setUpdateTime(new Date());
        this.save(appVersion);

    }

    @Override
    public void update(AppVersion appVersion) {
        if (this.getById(appVersion.getId()) == null)
            throw new ResponsiveException("版本号不存在或已被删除");

        OSEnum osEnum = OSEnum.getByKey(appVersion.getOsType());
        AppTypeEnum appTypeEnum = AppTypeEnum.getByKey(appVersion.getAppType());
        if (osEnum != null && appTypeEnum != null) {
            String osSwitchCacheKey =
                    StringUtils.join(VideoContants.CACHE_OS_ARMOR_SWITCH, osEnum.getName(), ":",
                            appTypeEnum.getCode(), ":", appVersion.getVersionName());

            if (cache.hasKey(osSwitchCacheKey) && !Objects.equals(cache.get(osSwitchCacheKey), appVersion.getArmorData()))
                cache.set(osSwitchCacheKey, appVersion.getArmorData());

            log.info("修改了APP版本信息，os：{},appType:{},appVersion:{},switch:{}", osEnum.getName(), appTypeEnum.getCode(), appVersion.getVersionName(), appVersion.getArmorData());
        }

        this.updateById(appVersion);

    }

    @Override
    public void removeById(Integer id) {
        super.removeById(id);
    }

    @Override
    public List<AppVersion> findByOsType(OSEnum osEnum) {
        return this.list(new QueryWrapper<AppVersion>().eq("os_type", osEnum.key()));
    }

    @Override
    public List<AppVersion> findByVersionName(String name) {
        return this.list(new QueryWrapper<AppVersion>().eq("version_name", name));
    }

    @Override
    public AppVersion findByVersionAndOs(OSEnum os, String versionName, AppTypeEnum appType) {
        return this.getOne(new QueryWrapper<AppVersion>().eq("os_type", os.key()).eq("app_type", appType.getKey())
                .eq("version_name", versionName));
    }

    @Override
    public List<AppVersion> findAll() {

        return this.list(new QueryWrapper<AppVersion>().orderByDesc("update_time"));
    }

    @Override
    public AppVersionVO findNewest(AppInfo appInfo) {
        List<AppVersion> versions = this.list(new QueryWrapper<AppVersion>().eq("os_type", appInfo.getOsKey())
                .eq("app_type", appInfo.getAppTypeKey()).eq("status", StatusEnum.ENABLE.key()).
                        orderByDesc("version_name"));

        AppVersionVO vo = AppVersionVO.builder().build();

        if (!ListUtils.isEmpty(versions)) {
            AppVersion appVersion = versions.get(0);
            vo = vo.builder().id(appVersion.getId()).content(appVersion.getContent())
                    .appDownloadUrl(appVersion.getAppDownloadUrl()).versionName(appVersion.getVersionName())
                    .isForceUpdate(appVersion.getIsForceUpdate()).build();

        }

        log.info("OS_Type[{}],检测最新版本【{}】,是否强制更新[{}]", appInfo.getOsName(), vo.getVersionName(), vo.getIsForceUpdate());
        return vo;
    }

    @Override
    public Boolean findArmorDataSwitch(AppInfo appInfo) {
       AppVersion version = this.getOne(new QueryWrapper<AppVersion>().eq("os_type", appInfo.getOsKey())
                .eq("status", StatusEnum.ENABLE.key()).eq("version_name", appInfo.getAppVersion()));

        log.info("获取伪装数据开关，os={},appVersion={},switch={}", appInfo.getOsName(), appInfo.getAppVersion(), version != null ? version.getArmorData() : null);
        return version == null || BooleanUtils.isNotFalse(version.getArmorData());
    }

}
