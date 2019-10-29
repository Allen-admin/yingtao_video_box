package com.k365.manager_service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.k365.manager_service.AdService;
import com.k365.manager_service.DomainService;
import com.k365.manager_service.SysConfParamService;
import com.k365.user_service.UserService;
import com.k365.user_service.UserTaskRecordService;
import com.k365.video_base.common.AppDisplayTypeEnum;
import com.k365.video_base.common.UserContext;
import com.k365.video_base.common.VideoContants;
import com.k365.video_base.mapper.AdMapper;
import com.k365.video_base.model.dto.AdDTO;
import com.k365.video_base.model.po.Ad;
import com.k365.video_base.model.po.User;
import com.k365.video_base.model.vo.AdVO;
import com.k365.video_base.model.vo.VideoBasicInfoVO;
import com.k365.video_common.constant.StatusEnum;
import com.k365.video_common.exception.ResponsiveException;
import com.k365.video_common.handler.fastdfs.FastDFSHelper;
import com.k365.video_common.util.RedisUtil;
import com.k365.video_common.util.Trim;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.ListUtils;

import javax.servlet.ServletRequest;
import java.util.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Gavin
 * @since 2019-07-17
 */
@Service
public class AdServiceImpl extends ServiceImpl<AdMapper, Ad> implements AdService {

    private final static Long expireTime = 3L * 24 * 60 * 60;

    @Autowired
    private RedisUtil cache;

    @Autowired
    @Lazy
    private UserService userService;

    @Autowired
    private SysConfParamService sysConfParamService;

    @Autowired
    private UserTaskRecordService userTaskRecordService;

    @Autowired
    @Lazy
    private DomainService domainService;


    @Override
    public void add(AdDTO adDTO) {
        Ad ad = new Ad();
        BeanUtils.copyProperties(adDTO, ad);
        boolean saved = this.save(ad);

        //如果新增了视频列表广告则刷新一下缓存
        if (saved && Objects.equals(ad.getShowType(), AppDisplayTypeEnum.AD_DISPLAY_FOR_VIDEO_LIST.key()))
            refreshVideoAdsCache(ad);

    }

    public void refreshVideoAdsCache(Ad ad) {
        List<Ad> list = this.list(new QueryWrapper<Ad>().eq("show_type", AppDisplayTypeEnum.AD_DISPLAY_FOR_VIDEO_LIST.key())
                .eq("status", StatusEnum.ENABLE.key()).eq("app_type", ad.getAdType()).orderByAsc("sort").select("id,title,cover,sort,details_url"));

        Map<String, AdVO> map = new HashMap<>();
        if (!ListUtils.isEmpty(list)) {
            list.forEach(ad2 -> map.put(ad2.getId(), AdVO.builder().id(ad2.getId()).cover(ad2.getCover())
                    .title(ad2.getTitle()).detailsUrl(ad2.getDetailsUrl()).sort(ad2.getSort()).build()));

            String cacheKey = VideoContants.CACHE_VIDEO_AD_MAP + ad.getAppType();
            cache.set(cacheKey, map, expireTime);
        }
    }

    @Override
    public List<AdVO> findAdsByType(AppDisplayTypeEnum adtEnum,ServletRequest request) {

        String domainUrl = domainService.getAppPicDomain();

        //获取当前用户信息
        User currentUser = UserContext.getCurrentUser();
        QueryWrapper<Ad> queryWrapper = new QueryWrapper<>();
        if (currentUser != null && currentUser.getAppType() != null) {
            queryWrapper.eq("app_type", currentUser.getAppType());
        }

        List<Ad> adList = this.list(queryWrapper.eq("show_type", adtEnum.key())
                .eq("status", StatusEnum.ENABLE.key()).orderByAsc("sort"));

        if (adList == null) {
            throw new ResponsiveException("广告不存在，请添加广告");
        }

        List<AdVO> voList = new ArrayList<>();
        for (Ad ad : adList) {
            AdVO vo = new AdVO();
            BeanUtils.copyProperties(ad, vo);
            if (vo.getCover() != null && !vo.getCover().equals("")) {
                vo.setCover(domainUrl + Trim.custom_ltrim(vo.getCover(), "group"));
            }
            voList.add(vo);
        }

        return voList;
    }


    @Override
    public void update(Ad ad) {
        Ad ad2 = this.getById(ad.getId());
        if (ad2 == null)
            throw new ResponsiveException("广告不存在或已被删除");

        boolean updated = this.updateById(ad);

        //如果新增了视频列表广告则刷新一下缓存
        if (updated && Objects.equals(ad.getAdType(), AppDisplayTypeEnum.AD_DISPLAY_FOR_VIDEO_LIST.key()))
            refreshVideoAdsCache(ad2);

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public void remove(String id) {
        Ad ad = this.getById(id);
        if (ad == null)
            throw new ResponsiveException("广告不存在或已被删除");


        boolean removed = this.removeById(id);

        //如果新增了视频列表广告则刷新一下缓存
        if (removed && Objects.equals(ad.getAdType(), AppDisplayTypeEnum.AD_DISPLAY_FOR_VIDEO_LIST.key()))
            refreshVideoAdsCache(ad);

        if (removed && StringUtils.isNotBlank(ad.getDetailsUrl())) {
            String[] dfsPath = ad.getCover().split("/", 5);
            FastDFSHelper.deleteFile(dfsPath[3], dfsPath[4]);
        }
    }

    @Override
    public AdVO getOneVAdBy4User(ServletRequest request) {
        User currentUser = UserContext.getCurrentUser();

        String adListCacheKey = VideoContants.CACHE_VIDEO_AD_MAP + currentUser.getAppType();
        AdVO result = null;
        Set<String> adIds4User = new HashSet<>();
        //用户广告id缓存key
        String adUserCacheKey = VideoContants.CACHE_VIDEO_AD_IDS_FOR_UID + currentUser.getId();
        if (cache.hasKey(adListCacheKey)) {
            //视频广告缓存map
            HashMap adMap = JSON.parseObject(JSON.toJSONString(cache.get(adListCacheKey), SerializerFeature.WriteMapNullValue), HashMap.class);

            Iterator it = adMap.keySet().iterator();
            boolean hasAdUserCacheKey = cache.hasKey(adUserCacheKey);

            if (hasAdUserCacheKey) {
                adIds4User = JSON.parseObject(JSON.toJSONString(cache.get(adUserCacheKey)), Set.class);
            }
            if (adIds4User.size() < adMap.size()) {
                while (it.hasNext()) {
                    String key = (String) it.next();

                    //获取第一张广告 || 获取第n张广告
                    if (!hasAdUserCacheKey || !adIds4User.contains(key)) {
                        result = JSONObject.parseObject(JSON.toJSONString(adMap.get(key)), AdVO.class);
                        break;
                    }
                }
            } else { //轮询一遍之后
                adIds4User = new HashSet<>();
            }
        }

        if (result == null) {
            List<AdVO> voList = this.findAdsByType(AppDisplayTypeEnum.AD_DISPLAY_FOR_VIDEO_LIST,request);
            if (!ListUtils.isEmpty(voList)) {
                Map<String, AdVO> cacheMap = new HashMap<>();
                voList.forEach(adVO ->
                        cacheMap.put(adVO.getId(), adVO)
                );
                cache.set(adListCacheKey, cacheMap, expireTime);
                result = voList.get(0);
            }
        }

        if (result != null) {
            adIds4User.add(result.getId());
            cache.set(adUserCacheKey, adIds4User, expireTime);
        }

        String domainUrl = domainService.getAppPicDomain();
        if (result.getCover() != null && !result.getCover().equals("")) {
            result.setCover(domainUrl + Trim.custom_ltrim(result.getCover(), "group"));
        }

        return result;
    }


    @Override
    public void getAd4User(Collection<VideoBasicInfoVO> list,ServletRequest request) {
        AdVO adVO = this.getOneVAdBy4User(request);
        if (adVO != null)
            list.add(new VideoBasicInfoVO().setCover(adVO.getCover()).setIsAd(true)
                    .setTitle(adVO.getTitle()).setAdUrl(adVO.getDetailsUrl()));
    }

    @Override
    public void clickAd() {
        User currentUser = UserContext.getCurrentUser();
        //每日任务点击广告
        userTaskRecordService.toDoClickAd(currentUser.getId());
    }

    @Override
    public List<Ad> getListByType(AppDisplayTypeEnum adtEnum, Integer app,ServletRequest request) {
        String domain = domainService.getAppPicDomain();
        List<Ad> list = this.list(new QueryWrapper<Ad>().eq("ad_type", adtEnum.key()).eq("app_type", app).orderByAsc("sort"));
        for (Ad ad : list) {
            if (ad.getCover() != null && !ad.getCover().equals("")) {
                ad.setCover(domain + Trim.custom_ltrim(ad.getCover(), "group"));
            }
        }
        return list;
    }

    @Override
    public Map<String, AdVO> getPlayPageAd(ServletRequest request) {
        String domainUrl = domainService.getAppPicDomain();

        Integer[] showTypes = new Integer[]{
                AppDisplayTypeEnum.AD_DISPLAY_FOR_BEFORE_PLAY.key(),
                AppDisplayTypeEnum.AD_DISPLAY_FOR_VIDEO_STOP.key(),
                AppDisplayTypeEnum.AD_DISPLAY_FOR_PLAY_PAGE_SMALL.key(),
                AppDisplayTypeEnum.AD_DISPLAY_FOR_PLAY_PAGE_BIG.key()
        };

        User currentUser = UserContext.getCurrentUser();
        List<Ad> adList = this.list(new QueryWrapper<Ad>().in("show_type", Arrays.asList(showTypes))
                .eq("ad_type", AppDisplayTypeEnum.AD.key()).eq("status", StatusEnum.ENABLE.key())
                .orderByAsc("show_type").eq("app_type", currentUser.getAppType()).orderByAsc("sort"));

        Map<String, AdVO> result = new HashMap<>();
        String[] showTypeStrs = new String[]{
                AppDisplayTypeEnum.AD_DISPLAY_FOR_BEFORE_PLAY.name(),
                AppDisplayTypeEnum.AD_DISPLAY_FOR_VIDEO_STOP.name(),
                AppDisplayTypeEnum.AD_DISPLAY_FOR_PLAY_PAGE_SMALL.name(),
                AppDisplayTypeEnum.AD_DISPLAY_FOR_PLAY_PAGE_BIG.name()
        };

        for (Ad ad : adList) {
            if (result.get(showTypeStrs[0]) == null && Objects.equals(ad.getShowType(), showTypes[0])) {
                AdVO vo0 = AdVO.builder().id(ad.getId()).cover(ad.getCover()).detailsUrl(ad.getDetailsUrl()).build();
                if (vo0.getCover() != null && !vo0.getCover().equals("")) {
                    vo0.setCover(domainUrl + Trim.custom_ltrim(vo0.getCover(), "group"));
                }
                result.put(showTypeStrs[0], vo0);

            } else if (result.get(showTypeStrs[1]) == null && Objects.equals(ad.getShowType(), showTypes[1])) {
                AdVO vo1 = AdVO.builder().id(ad.getId()).cover(ad.getCover()).detailsUrl(ad.getDetailsUrl()).build();
                if (vo1.getCover() != null && !vo1.getCover().equals("")) {
                    vo1.setCover(domainUrl + Trim.custom_ltrim(vo1.getCover(), "group"));
                }
                result.put(showTypeStrs[1], vo1);

            } else if (result.get(showTypeStrs[2]) == null && Objects.equals(ad.getShowType(), showTypes[2])) {
                AdVO vo2 = AdVO.builder().id(ad.getId()).cover(ad.getCover()).detailsUrl(ad.getDetailsUrl()).build();
                if (vo2.getCover() != null) {
                    vo2.setCover(domainUrl + Trim.custom_ltrim(vo2.getCover(), "group"));
                }
                result.put(showTypeStrs[2], vo2);

            } else if (result.get(showTypeStrs[3]) == null && Objects.equals(ad.getShowType(), showTypes[3])) {
                AdVO vo3 = AdVO.builder().id(ad.getId()).cover(ad.getCover()).detailsUrl(ad.getDetailsUrl()).build();
                if (vo3.getCover() != null && vo3.getCover().equals("")) {
                    vo3.setCover(domainUrl + Trim.custom_ltrim(vo3.getCover(), "group"));
                }
                result.put(showTypeStrs[3], vo3);
            }
        }

        return result;
    }


}
