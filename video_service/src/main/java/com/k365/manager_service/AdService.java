package com.k365.manager_service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.k365.video_base.common.AppDisplayTypeEnum;
import com.k365.video_base.model.dto.AdDTO;
import com.k365.video_base.model.po.Ad;
import com.k365.video_base.model.vo.AdVO;
import com.k365.video_base.model.vo.VideoBasicInfoVO;

import javax.servlet.ServletRequest;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author Gavin
 * @since 2019-07-17
 */
public interface AdService extends IService<Ad> {

    /**
     * 新增广告
     */
    void add(AdDTO adDTO);

    /**
     * 根据广告类型查询广告
     */
    List<AdVO> findAdsByType(AppDisplayTypeEnum adtEnum,ServletRequest request);

    /**
     * 根据id修改
     */
    void update(Ad ad);

    /**
     * 根据Id删除广告
     */
    void remove(String id);

    /**
     * 获取一个用户对应的视频列表广告
     */
    AdVO getOneVAdBy4User(ServletRequest request);

    /**
     * 添加用户广告到视频列表
     */
    void getAd4User(Collection<VideoBasicInfoVO> list,ServletRequest request);

    /**
     * 点击广告
     */
    void clickAd();

    /**
     * 获取列表
     */
    List<Ad> getListByType(AppDisplayTypeEnum adtEnum,Integer app,ServletRequest request);

    /**
     * 获取播放页面所有广告
     */
    Map<String,AdVO> getPlayPageAd(ServletRequest request);


}
