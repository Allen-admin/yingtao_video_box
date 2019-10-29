package com.k365.video_site_api.armorcontroller;


import com.k365.video_base.common.AppDisplayTypeEnum;
import com.k365.video_base.common.ResultFactory;
import com.k365.video_base.model.dto.BaseDTO;
import com.k365.video_base.model.dto.UserDTO;
import com.k365.video_base.model.dto.VUserVideoCollectionDTO;
import com.k365.video_base.model.dto.VideoCommentDTO;
import com.k365.video_base.model.so.VideoSO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import javax.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Gavin
 * @date 2019/8/8 19:38
 * @description：
 */
@RestController
@RequestMapping("/armor-data")
@Api(tags = "APP伪装管理")
public class ArmorDataController {

    @Autowired
    private ArmorData armorData;


    @GetMapping("/ad/get/app-up-ad")
    @ApiOperation(value = "获取app启动页广告")
    public String getAppUpAd() {
        Map<String, Object> map = new HashMap<>();
        map.put("countdown", 5);
        map.put("appUpAd", armorData.appUpAdList);
        return ResultFactory.buildNotNullResult(map);
    }

    @GetMapping("/ad/get/ad/{key}")
    @ApiOperation(value = "根据类型查询广告")
    public String findAdByType(@PathVariable("key") Integer key) {
        AppDisplayTypeEnum adtEnum = AppDisplayTypeEnum.getAdtEnum(key, AppDisplayTypeEnum.AD.type());
        return ResultFactory.buildNotNullResult(armorData.findAdsByType(adtEnum));
    }


    @GetMapping("/ad/get/banner/{key}")
    @ApiOperation(value = "根据类型查询轮播图")
    public String findBannerByType(@PathVariable("key") Integer key) {
        AppDisplayTypeEnum adtEnum = AppDisplayTypeEnum.getAdtEnum(key, AppDisplayTypeEnum.BANNER.type());
        return ResultFactory.buildSuccessResult(armorData.findBannersByType(adtEnum));
    }


    @PostMapping("/ad/click")
    @ApiOperation(value = "点击广告")
    public String clickAd() {
        return ResultFactory.buildSuccessResult();
    }


    @GetMapping("/video-channel/get/all")
    @ApiOperation("获取所有视频频道")
    public String getAllChannel() {
        return ResultFactory.buildSuccessResult(armorData.getAllChannels);
    }


    @GetMapping("/domain/get/usable")
    @ApiOperation(value = "获取可用域名列表")
    public String findUsableDomain() {
        return ResultFactory.buildSuccessResult(armorData.findUsable);
    }


    @GetMapping("/notice/get/{key}")
    @ApiOperation("获取公告")
    public String getNotice(@PathVariable("key") Integer key) {
        AppDisplayTypeEnum adtEnum = AppDisplayTypeEnum.getAdtEnum(key, AppDisplayTypeEnum.NOTICE.type());
        return ResultFactory.buildNotNullResult(armorData.findNoticeByType(adtEnum));
    }

    @PostMapping(value = "/user/visitor/auto-register")
    @ApiOperation(value = "游客自动注册")
    public String autoRegister(ServletRequest request) {
        Object[] params = (Object[]) request.getAttribute("params");
        @NotBlank(groups = {BaseDTO.Add.class}, message = "mac地址不能为空") String macAddr = ((UserDTO) params[0]).getMacAddr();
        return ResultFactory.buildSuccessResult(armorData.visitorAutoRegister(UserDTO.builder().macAddr(macAddr).build()));
    }

    @PostMapping(value = "/user/user-info")
    @ApiOperation(value = "获取当前用户信息")
    public String userInfo(ServletRequest request) {
        return ResultFactory.buildSuccessResult(armorData.getUserInfo(request));
    }

    @PostMapping("/user-video-collection/add")
    @ApiOperation("添加收藏视频")
    public String addVideoCollection(ServletRequest request) {
        Object[] params = (Object[]) request.getAttribute("params");
        @NotBlank(groups = {BaseDTO.Add.class}, message = "视频id不能为空") String videoId = ((VUserVideoCollectionDTO) params[0]).getVId();
        armorData.addCollection(armorData.getUserMacAddr(request, new ArmorDTO().setVideoId(videoId)));
        return ResultFactory.buildSuccessResult();
    }

    @PostMapping("/user-video-collection/list")
    @ApiOperation("分页查询收藏列表")
    public String getVideoCollectionList(ServletRequest request) {
        return ResultFactory.buildSuccessResult(armorData.getVideoCollectionList(armorData.getUserMacAddr(request, new ArmorDTO())));
    }

    @PostMapping("/user-video-collection/remove")
    @ApiOperation("取消收藏")
    public String removeCollection(ServletRequest request) {
        Object[] params = (Object[]) request.getAttribute("params");
        @NotBlank(groups = {BaseDTO.Add.class}, message = "视频id不能为空") String videoId = ((VUserVideoCollectionDTO) params[0]).getVId();
        armorData.removeCollection(armorData.getUserMacAddr(request, new ArmorDTO().setVideoId(videoId)));
        return ResultFactory.buildSuccessResult();
    }

    @GetMapping(value = "/user/viewing-info")
    @ApiOperation(value = "剩余观影信息")
    public String viewingInfo(ServletRequest request) {
        return ResultFactory.buildSuccessResult(armorData.getViewingCount(request));
    }

    @PostMapping("/user-viewing-record/list")
    @ApiOperation(value = "查看历史记录")
    public String getUserViewingRecord(ServletRequest request) {
        return ResultFactory.buildSuccessResult(armorData.getUserViewingRecord(armorData.getUserMacAddr(request, new ArmorDTO())));
    }

    @PostMapping("/user-viewing-record/remove")
    @ApiOperation(value = "清除观影记录")
    public String removeUserViewingRecord(ServletRequest request) {
        armorData.removeViewing(armorData.getUserMacAddr(request, new ArmorDTO()));
        return ResultFactory.buildSuccessResult();
    }

    @PostMapping("/video-comment/get/list")
    @ApiOperation(value = "获取评论列表")
    public String getVideoCommentList(ServletRequest request) {
        Object[] params = (Object[]) request.getAttribute("params");
        @NotBlank(groups = {BaseDTO.Add.class}, message = "视频id不能为空") String videoId = ((VideoCommentDTO) params[0]).getVideoId();
        return ResultFactory.buildSuccessResult(armorData.getVideoCommentList(ArmorDTO.builder().videoId(videoId).build()));
    }

    @PostMapping("/video/get/featured")
    @ApiOperation(value = "编辑精选视频")
    public String findFeatured() {
        return ResultFactory.buildSuccessResult(armorData.getVideoList(VideoTypeEnum.Featured));
    }

    @PostMapping("/video/get/u-likes")
    @ApiOperation(value = "猜你喜欢视频")
    public String findULikes() {
        return ResultFactory.buildSuccessResult(armorData.getVideoList(VideoTypeEnum.ULikes));
    }

    @PostMapping("/video/get/latest")
    @ApiOperation(value = "最新视频")
    public String findLatest() {
        return ResultFactory.buildSuccessResult(armorData.getVideoList(VideoTypeEnum.Latest));
    }

    @PostMapping("/video/get/hottest")
    @ApiOperation(value = "最热视频")
    public String findHottest() {
        return ResultFactory.buildSuccessResult(armorData.getVideoList(VideoTypeEnum.Hottest));
    }

    @PostMapping("/video/get/relevant")
    @ApiOperation(value = "相关视频")
    public String findRandomRelevant() {
        return ResultFactory.buildSuccessResult(armorData.getVideoList(VideoTypeEnum.Relevant));
    }

    @PostMapping("/video/get/play-info")
    @ApiOperation(value = "获取播放页数据")
    public String findPlayVideo(ServletRequest request) {
        Object[] params = (Object[]) request.getAttribute("params");
        @NotBlank(groups = {BaseDTO.Add.class}, message = "视频id不能为空") String videoId = ((VideoSO) params[0]).getVideoId();
        return ResultFactory.buildSuccessResult(armorData.getVideoPlayInfo(new ArmorDTO().setVideoId(videoId).setUser(armorData.getCurrentUser(request))));
    }

    @PostMapping("/video/get/vip-play-info")
    @ApiOperation(value = "查询vip视频播放信息")
    public String findVipPlayVideo(ServletRequest request) {
        Object[] params = (Object[]) request.getAttribute("params");
        @NotBlank(groups = {BaseDTO.Add.class}, message = "视频id不能为空") String videoId = ((VideoSO) params[0]).getVideoId();
        return ResultFactory.buildSuccessResult(armorData.getVideoPlayInfo(new ArmorDTO().setVideoId(videoId).setUser(armorData.getCurrentUser(request))));
    }


}
