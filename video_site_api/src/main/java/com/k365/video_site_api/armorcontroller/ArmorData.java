package com.k365.video_site_api.armorcontroller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.k365.video_base.common.AppDisplayTypeEnum;
import com.k365.video_base.common.VideoContants;
import com.k365.video_base.model.dto.UserDTO;
import com.k365.video_base.model.po.User;
import com.k365.video_base.model.po.VideoChannel;
import com.k365.video_base.model.po.VideoComment;
import com.k365.video_base.model.vo.*;
import com.k365.video_common.constant.ExCodeMsgEnum;
import com.k365.video_common.constant.UserStatusEnum;
import com.k365.video_common.exception.ResponsiveException;
import com.k365.video_common.util.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.util.ListUtils;

import javax.servlet.ServletRequest;
import java.util.*;

/**
 * @author Gavin
 * @date 2019/9/5 19:46
 * @description：
 */
@Component
public class ArmorData {

    @Autowired
    private RedisUtil cache;

    /**
     * 用户信息缓存key
     */
    private static final String USER_INFO_CACHE = "armor_data:user_info";

    /**
     * 用户视频收藏缓存key
     */
    private static final String USER_COLLECTION_CACHE = "armor_data:user_video_collection:";

    /**
     * 用户观影记录缓存key
     */
    private static final String USER_VIEWING_CACHE = "armor_data:user_video_viewing:";

    /**
     * 用户观影次数上限
     */
    private static final int USER_VIEWING_MAX = 20;

    /**
     * token 过期时间 单位：秒
     */
    private static final long EXPIRE_TIME = 24 * 60 * 60;

    /**
     * 播放页广告
     */
    private static Map<String, AdVO> playAdList = new HashMap<String, AdVO>() {{
        //视频播放前广告
        put(AppDisplayTypeEnum.AD_DISPLAY_FOR_BEFORE_PLAY.name(), AdVO.builder().id("AD-uwgeuqehq12312312")
                .cover("http://www.ludobe.net/group1/M00/04/AC/a725Al2K2RqAF0PbAAX8cqgqvYQ757.jpg")
                .detailsUrl("https://keep.com/training").build());

        //视频暂停时广告
        put(AppDisplayTypeEnum.AD_DISPLAY_FOR_VIDEO_STOP.name(), AdVO.builder().id("AD-uwgeuqehq12312312")
                .cover("http://www.ludobe.net/group1/M00/07/DB/uY3Nml2K2RqAQDE8AATcZNlW2Bw875.jpg")
                .detailsUrl("https://keep.com/training").build());

        //播放页面小广告
        put(AppDisplayTypeEnum.AD_DISPLAY_FOR_PLAY_PAGE_SMALL.name(), AdVO.builder().id("AD-uwgeuqehq12312312")
                .cover("http://www.ludobe.net/group1/M00/04/AC/a725Al2K2RqAF0PbAAX8cqgqvYQ757.jpg")
                .detailsUrl("https://keep.com/training").build());

        //播放页面大广告
        put(AppDisplayTypeEnum.AD_DISPLAY_FOR_PLAY_PAGE_BIG.name(), AdVO.builder().id("AD-uwgeuqehq12312312")
                .cover("http://www.ludobe.net/group1/M00/07/DB/uY3Nml2K2RqAQDE8AATcZNlW2Bw875.jpg")
                .detailsUrl("https://keep.com/training").build());
    }};


    /**
     * 视频资源
     */
    private static List<ArmorVideoInfo> videos = new ArrayList<>();

    static {
        ArmorVideoInfo armorVideoInfo = new ArmorVideoInfo().setPlaySum(30823L)
                .setPlayUrl("http://www.ludobe.net/group1/M00/04/AD/a725Al2LSpKAWaW7A9Be-Jvms0g304.mp4");
        armorVideoInfo.setId("A-1sdlha342l4l2j324l3225").setTitle("How To Get Bigger Traps (One Exercise)").setTimeLen(308)
                .setCreateDate(DateUtil.getRandomDateOfYear())
                .setCover("http://www.ludobe.net/group1/M00/07/DB/uY3Nml2K2RqAOkprAAH7E73Yeho215.jpg");

        ArmorVideoInfo armorVideoInfo2 = new ArmorVideoInfo().setPlaySum(755364L)
                .setPlayUrl("http://www.ludobe.net/group1/M00/07/DB/uY3Nml2LSrOAcV4lBn_eDrbjXBs888.mp4");
        armorVideoInfo2.setId("A-2sdlha342l4l2j324l3225").setTitle("PEETA甩油計畫EP07 - 如何成功的減肥？").setTimeLen(754)
                .setCreateDate(DateUtil.getRandomDateOfYear())
                .setCover("http://www.ludobe.net/group1/M00/04/AC/a725Al2K2RqARkMfAADkzkNl2QM522.jpg");

        ArmorVideoInfo armorVideoInfo3 = new ArmorVideoInfo().setPlaySum(333254L)
                .setPlayUrl("http://www.ludobe.net/group1/M00/04/AD/a725Al2LSvyAZWsGApAumJqxwuQ704.mp4");
        armorVideoInfo3.setId("A-3sdlha342l4l2j324l3225").setTitle("【Saria 健身教室】不用跑步，在家也可以做的心肺運動!").setTimeLen(334)
                .setCreateDate(DateUtil.getRandomDateOfYear())
                .setCover("http://www.ludobe.net/group1/M00/07/DB/uY3Nml2K2RqAMUSNAAE6hrr3COM486.jpg");

        ArmorVideoInfo armorVideoInfo4 = new ArmorVideoInfo().setPlaySum(29810L)
                .setPlayUrl("http://www.ludobe.net/group1/M00/07/DB/uY3Nml2LSxSAWPm2ASVdw9bPItQ452.mp4");
        armorVideoInfo4.setId("A-4sdlha342l4l2j324l3225").setTitle("【Saria健身教室 】重訓+肌耐力+心肺訓練 三合一! ").setTimeLen(210)
                .setCreateDate(DateUtil.getRandomDateOfYear())
                .setCover("http://www.ludobe.net/group1/M00/04/AC/a725Al2K2RqARFP-AAEVoupM2V4966.jpg");

        ArmorVideoInfo armorVideoInfo5 = new ArmorVideoInfo().setPlaySum(56354L)
                .setPlayUrl("http://www.ludobe.net/group1/M00/04/AD/a725Al2LS0qAdJNKBgBBBD6F95k457.mp4");
        armorVideoInfo5.setId("A-5sdlha342l4l2j324l3225").setTitle("哪一种训练方式最有效？？ 大重量？多次数？？- 一种方法帮你增肌？？").setTimeLen(554)
                .setCreateDate(DateUtil.getRandomDateOfYear())
                .setCover("http://www.ludobe.net/group1/M00/07/DB/uY3Nml2K2RqAQbcKAAFOdy1qXkQ981.jpg");

        ArmorVideoInfo armorVideoInfo6 = new ArmorVideoInfo().setPlaySum(54236L)
                .setPlayUrl("http://www.ludobe.net/group1/M00/07/DB/uY3Nml2LS7mAKuDGCKkG3ynIL8Y143.mp4");
        armorVideoInfo6.setId("A-6sdlha342l4l2j324l3225").setTitle("女孩兒們的背部&腹部訓練").setTimeLen(546)
                .setCreateDate(DateUtil.getRandomDateOfYear())
                .setCover("http://www.ludobe.net/group1/M00/04/AC/a725Al2K2RqAYrrUAAIMOnxS7iU541.jpg");

        ArmorVideoInfo armorVideoInfo7 = new ArmorVideoInfo().setPlaySum(39825L)
                .setPlayUrl("http://www.ludobe.net/group1/M00/04/AD/a725Al2LS-CAe6NPA7qBW7_G9Lc057.mp4");
        armorVideoInfo7.setId("A-7sdlha342l4l2j324l3225").setTitle("女生健身如何瘦大腿+水蜜桃翹臀 ").setTimeLen(325)
                .setCreateDate(DateUtil.getRandomDateOfYear())
                .setCover("http://www.ludobe.net/group1/M00/07/DB/uY3Nml2K2RqANoIWAAG2qt-EZcc022.jpg");

        ArmorVideoInfo armorVideoInfo8 = new ArmorVideoInfo().setPlaySum(13236L)
                .setPlayUrl("http://www.ludobe.net/group1/M00/07/DB/uY3Nml2LTASABNmwAVjDWACM1A0510.mp4");
        armorVideoInfo8.setId("A-8sdlha342l4l2j324l3225").setTitle("徒手練狠胸 - 隨時隨地 沒有藉口了！ 上 中 下 胸 完整教學").setTimeLen(132)
                .setCreateDate(DateUtil.getRandomDateOfYear())
                .setCover("http://www.ludobe.net/group1/M00/04/AC/a725Al2K2RqAWrIbAAEnsKs_Nmk477.jpg");

        ArmorVideoInfo armorVideoInfo9 = new ArmorVideoInfo().setPlaySum(43581L)
                .setPlayUrl("http://www.ludobe.net/group1/M00/04/AD/a725Al2LTCSASzmeA0QyepWHRhk776.mp4");
        armorVideoInfo9.setId("A-9sdlha342l4l2j324l3225").setTitle("怎麼利用有_無氧練出腹肌以及強壯肌肉體態").setTimeLen(431)
                .setCreateDate(DateUtil.getRandomDateOfYear())
                .setCover("http://www.ludobe.net/group1/M00/07/DB/uY3Nml2K2RqAYwUBAAIvPF_IT6g627.jpg");

        ArmorVideoInfo armorVideoInfo10 = new ArmorVideoInfo().setPlaySum(36561L)
                .setPlayUrl("http://www.ludobe.net/group1/M00/07/DB/uY3Nml2LTESAAC_TA0ykKlLvd6c639.mp4");
        armorVideoInfo10.setId("A-10sdlha342l4l2j324l3225").setTitle("手臂訓練 (二頭肌,三頭肌)...重要嗎？").setTimeLen(361)
                .setCreateDate(DateUtil.getRandomDateOfYear())
                .setCover("http://www.ludobe.net/group1/M00/07/DB/uY3Nml2K2RqAOZq0AAEOMnN_doE805.jpg");

        ArmorVideoInfo armorVideoInfo11 = new ArmorVideoInfo().setPlaySum(48836L)
                .setPlayUrl("http://www.ludobe.net/group1/M00/04/AD/a725Al2LTFuAJOptA0CP5XcCvZY392.mp4");
        armorVideoInfo11.setId("A-11sdlha342l4l2j324l3225").setTitle("減掉5磅體脂肪最好又最快的方法 (中文字幕)").setTimeLen(361)
                .setCreateDate(DateUtil.getRandomDateOfYear())
                .setCover("http://www.ludobe.net/group1/M00/04/AC/a725Al2K2RqAGUPQAAE0NrE_MDw236.jpg");

        ArmorVideoInfo armorVideoInfo12 = new ArmorVideoInfo().setPlaySum(74897L)
                .setPlayUrl("http://www.ludobe.net/group1/M00/07/DB/uY3Nml2LTG-ASXMTA7CuvnL5veE328.mp4");
        armorVideoInfo12.setId("A-12sdlha342l4l2j324l3225").setTitle("訓練肩膀常見錯誤 (中文字幕)").setTimeLen(748)
                .setCreateDate(DateUtil.getRandomDateOfYear())
                .setCover("http://www.ludobe.net/group1/M00/07/DB/uY3Nml2K2RqAAAgwAAFU5mM9GKA559.jpg");

        ArmorVideoInfo armorVideoInfo13 = new ArmorVideoInfo().setPlaySum(42124L)
                .setPlayUrl("http://www.ludobe.net/group1/M00/04/AD/a725Al2LTIOAailoA8p7lQJLNUA965.mp4");
        armorVideoInfo13.setId("A-13sdlha342l4l2j324l3225").setTitle("頸後肩推 - 世界上最危險的訓練動作 (中文字幕)").setTimeLen(421)
                .setCreateDate(DateUtil.getRandomDateOfYear())
                .setCover("http://www.ludobe.net/group1/M00/04/AC/a725Al2K2RqAL1fnAAFqtDJ2gm8845.jpg");


        videos.add(armorVideoInfo);
        videos.add(armorVideoInfo2);
        videos.add(armorVideoInfo3);
        videos.add(armorVideoInfo4);
        videos.add(armorVideoInfo5);
        videos.add(armorVideoInfo6);
        videos.add(armorVideoInfo7);
        videos.add(armorVideoInfo8);
        videos.add(armorVideoInfo9);
        videos.add(armorVideoInfo10);
        videos.add(armorVideoInfo11);
        videos.add(armorVideoInfo12);
        videos.add(armorVideoInfo13);

    }

    /**
     * 视频评论列表
     */
    private static Map<String, List<VideoComment>> videoCommentList = new HashMap<String, List<VideoComment>>() {{
        put("A-1sdlha342l4l2j324l3225", new ArrayList<VideoComment>() {{
            add(VideoComment.builder().id("abcedf").time(new Date())
                    .userIcon(null)
                    .userId("asdakjfhadsadsksnf").userNickname("新用户HD355S").videoId("A-1sdlha342l4l2j324l3225").videoTitle("")
                    .content("一起加油！塑造完美的自己").build());

            add(VideoComment.builder().id("abcedf").time(new Date())
                    .userIcon(null)
                    .userId("asdakgfdg3jfhksnf").userNickname("新用户HD355S").videoId("A-1sdlha342l4l2j324l3225").videoTitle("")
                    .content("动作很规范").build());
        }});

        put("A-8sdlha342l4l2j324l3225", new ArrayList<VideoComment>() {{
            add(VideoComment.builder().id("abcedf").time(new Date())
                    .userIcon(null)
                    .userId("asdakjfhk2342dsnf").userNickname("新用户HD355S").videoId("A-8sdlha342l4l2j324l3225").videoTitle("")
                    .content("一部不错的视频！").build());

            add(VideoComment.builder().id("abcedf").time(new Date())
                    .userIcon(null)
                    .userId("asdakjfhksn3453dff").userNickname("新用户HD355S").videoId("A-8sdlha342l4l2j324l3225").videoTitle("")
                    .content("是的，这是一部不错的视频！").build());
        }});

        put("A-11sdlha342l4l2j324l3225", new ArrayList<VideoComment>() {{
            add(VideoComment.builder().id("abcedf").time(new Date())
                    .userIcon(null)
                    .userId("asdakjfh4567hksnf").userNickname("新用户HD355S").videoId("A-11sdlha342l4l2j324l3225").videoTitle("")
                    .content("教练身材很棒").build());

            add(VideoComment.builder().id("abcedf").time(new Date())
                    .userIcon(null)
                    .userId("asdaqwef5kjfhksnf").userNickname("新用户HD355S").videoId("A-11sdlha342l4l2j324l3225").videoTitle("")
                    .content("坚持晨跑三个月").build());
        }});

    }};


    /**
     * 广告数据
     */
    private static Map<String, List<AdVO>> adList = new HashMap<String, List<AdVO>>() {{
        //视频列表广告
        put(AppDisplayTypeEnum.AD_DISPLAY_FOR_VIDEO_LIST.name(), new ArrayList<AdVO>() {{
            add(AdVO.builder().id("AD-uwgeuqehq12312312")
                    .cover("http://103.100.142.208:8888/group1/M00/00/02/Z2SO0F1uFnWAT9SqABAAAOEshgo253.jpg")
                    .detailsUrl("https://keep.com/training").build());

            add(AdVO.builder().id("AD-uwgeuqehq12312312")
                    .cover("http://103.100.142.208:8888/group1/M00/00/02/Z2SO0F1uFnWAT9SqABAAAOEshgo253.jpg")
                    .detailsUrl("https://keep.com/training").build());

            add(AdVO.builder().id("AD-uwgeuqehq12312312")
                    .cover("http://103.100.142.208:8888/group1/M00/00/02/Z2SO0F1uFnWAT9SqABAAAOEshgo253.jpg")
                    .detailsUrl("https://keep.com/training").build());
        }});

        //游戏列表广告
        put(AppDisplayTypeEnum.AD_DISPLAY_FOR_GAME_LIST.name(), new ArrayList<AdVO>() {{
            add(AdVO.builder().id("AD-uwgeuqehq12312312")
                    .cover("http://www.ludobe.net/group1/M00/04/AC/a725Al2K2RqAF0PbAAX8cqgqvYQ757.jpg")
                    .detailsUrl("https://keep.com/training").build());

            add(AdVO.builder().id("AD-uwgeuqehq12312312")
                    .cover("http://www.ludobe.net/group1/M00/07/DB/uY3Nml2K2RqAQDE8AATcZNlW2Bw875.jpg")
                    .detailsUrl("https://keep.com/training").build());

        }});

        //游戏头部广告
        put(AppDisplayTypeEnum.AD_DISPLAY_FOR_GAME_TOP.name(), new ArrayList<AdVO>() {{
            add(AdVO.builder().id("AD-uwgeuqehq12312312")
                    .cover("http://www.ludobe.net/group1/M00/07/DB/uY3Nml2K2RqAQDE8AATcZNlW2Bw875.jpg")
                    .detailsUrl("https://keep.com/training").build());
        }});

        //编辑精选广告
        put(AppDisplayTypeEnum.AD_DISPLAY_FOR_FEATURED_VIDEO.name(), new ArrayList<AdVO>() {{
            add(AdVO.builder().id("AD-uwgeuqehq12312312")
                    .cover("http://www.ludobe.net/group1/M00/04/AC/a725Al2K2RqAF0PbAAX8cqgqvYQ757.jpg")
                    .detailsUrl("https://keep.com/training").build());
        }});

    }};

    /**
     * 轮播图
     */
    private static Map<String, List<AdVO>> bannerList = new HashMap<String, List<AdVO>>() {{
        //推荐页轮播图
        put(AppDisplayTypeEnum.AD_DISPLAY_FOR_RECOMMEND_VIDEO_BANNER.name(), new ArrayList<AdVO>() {{
            add(AdVO.builder().id("AD-uwgeuqehq12312312")
                    .cover("http://www.ludobe.net/group1/M00/04/AC/a725Al2K2RqAF0PbAAX8cqgqvYQ757.jpg")
                    .detailsUrl("https://keep.com/training").build());

            add(AdVO.builder().id("AD-uwgeuqehq12312312")
                    .cover("http://www.ludobe.net/group1/M00/07/DB/uY3Nml2K2RqAQDE8AATcZNlW2Bw875.jpg")
                    .detailsUrl("https://keep.com/training").build());

            add(AdVO.builder().id("AD-uwgeuqehq12312312")
                    .cover("http://www.ludobe.net/group1/M00/04/AC/a725Al2K2RqAF0PbAAX8cqgqvYQ757.jpg")
                    .detailsUrl("https://keep.com/training").build());

            add(AdVO.builder().id("AD-uwgeuqehq12312312")
                    .cover("http://www.ludobe.net/group1/M00/07/DB/uY3Nml2K2RqAQDE8AATcZNlW2Bw875.jpg")
                    .detailsUrl("https://keep.com/training").build());
        }});

        //专题视频轮播图
        put(AppDisplayTypeEnum.AD_DISPLAY_FOR_THEMATIC_VIDEO_BANNER.name(), new ArrayList<AdVO>() {{
            add(AdVO.builder().id("AD-uwgeuqehq12312312")
                    .cover("http://www.ludobe.net/group1/M00/04/AC/a725Al2K2RqAF0PbAAX8cqgqvYQ757.jpg")
                    .detailsUrl("https://keep.com/training").build());

            add(AdVO.builder().id("AD-uwgeuqehq12312312")
                    .cover("http://www.ludobe.net/group1/M00/07/DB/uY3Nml2K2RqAQDE8AATcZNlW2Bw875.jpg")
                    .detailsUrl("https://keep.com/training").build());

            add(AdVO.builder().id("AD-uwgeuqehq12312312")
                    .cover("http://www.ludobe.net/group1/M00/04/AC/a725Al2K2RqAF0PbAAX8cqgqvYQ757.jpg")
                    .detailsUrl("https://keep.com/training").build());

            add(AdVO.builder().id("AD-uwgeuqehq12312312")
                    .cover("http://www.ludobe.net/group1/M00/07/DB/uY3Nml2K2RqAQDE8AATcZNlW2Bw875.jpg")
                    .detailsUrl("https://keep.com/training").build());
        }});

        //最新页轮播图
        put(AppDisplayTypeEnum.AD_DISPLAY_FOR_LATEST_VIDEO_BANNER.name(), new ArrayList<AdVO>() {{
            add(AdVO.builder().id("AD-uwgeuqehq12312312")
                    .cover("http://www.ludobe.net/group1/M00/04/AC/a725Al2K2RqAF0PbAAX8cqgqvYQ757.jpg")
                    .detailsUrl("https://keep.com/training").build());

            add(AdVO.builder().id("AD-uwgeuqehq12312312")
                    .cover("http://www.ludobe.net/group1/M00/07/DB/uY3Nml2K2RqAQDE8AATcZNlW2Bw875.jpg")
                    .detailsUrl("https://keep.com/training").build());

            add(AdVO.builder().id("AD-uwgeuqehq12312312")
                    .cover("http://www.ludobe.net/group1/M00/04/AC/a725Al2K2RqAF0PbAAX8cqgqvYQ757.jpg")
                    .detailsUrl("https://keep.com/training").build());

            add(AdVO.builder().id("AD-uwgeuqehq12312312")
                    .cover("http://www.ludobe.net/group1/M00/07/DB/uY3Nml2K2RqAQDE8AATcZNlW2Bw875.jpg")
                    .detailsUrl("https://keep.com/training").build());
        }});

        //最热页轮播图
        put(AppDisplayTypeEnum.AD_DISPLAY_FOR_HOTTEST_VIDEO_BANNER.name(), new ArrayList<AdVO>() {{
            add(AdVO.builder().id("AD-uwgeuqehq12312312")
                    .cover("http://www.ludobe.net/group1/M00/04/AC/a725Al2K2RqAF0PbAAX8cqgqvYQ757.jpg")
                    .detailsUrl("https://keep.com/training").build());

            add(AdVO.builder().id("AD-uwgeuqehq12312312")
                    .cover("http://www.ludobe.net/group1/M00/07/DB/uY3Nml2K2RqAQDE8AATcZNlW2Bw875.jpg")
                    .detailsUrl("https://keep.com/training").build());

            add(AdVO.builder().id("AD-uwgeuqehq12312312")
                    .cover("http://www.ludobe.net/group1/M00/04/AC/a725Al2K2RqAF0PbAAX8cqgqvYQ757.jpg")
                    .detailsUrl("https://keep.com/training").build());

            add(AdVO.builder().id("AD-uwgeuqehq12312312")
                    .cover("http://www.ludobe.net/group1/M00/07/DB/uY3Nml2K2RqAQDE8AATcZNlW2Bw875.jpg")
                    .detailsUrl("https://keep.com/training").build());
        }});
    }};


    /**
     * APP启动页广告
     */
    public static List<AdVO> appUpAdList = new ArrayList<AdVO>() {{
        add(AdVO.builder().cover("http://www.ludobe.net/group1/M00/07/DB/uY3Nml2LQrSAF5UsAANCN6CpT4w785.jpg")
                .id("AD-uwgeuqehq12312312").detailsUrl("https://keep.com/training").build());
    }};


    /**
     * 所有视频频道
     */
    public static List<VideoChannel> getAllChannels = new ArrayList<VideoChannel>() {{
        add(VideoChannel.builder().id(-4).name("首页").sort(1).build());
        add(VideoChannel.builder().id(-3).name("最新").sort(2).build());
        add(VideoChannel.builder().id(-2).name("最热").sort(3).build());
    }};

    /**
     * 可用域名列表
     */
    public static List<DomainVO> findUsable = new ArrayList<DomainVO>() {{
//        add(DomainVO.builder().domain("www.baidu.com").build());
//        add(DomainVO.builder().domain("www.google.com").build());
    }};

    /**
     * 公告
     */
    private static Map<String, NoticeVO> noticeList = new HashMap<String, NoticeVO>() {{
        //弹窗公告
        put(AppDisplayTypeEnum.NOTICE_DISPLAY_FOR_POPUP.name(), NoticeVO.builder().type(50)
                .title("每天对自己说：")
                .content("累！累就对了！在你经历过风吹雨打后，也许会伤痕累累，不过当雨后的第一缕阳光投射到你那苍白、憔悴的脸庞时，你要欣喜若狂，并不是因为阳光的温暖，而是在苦了心志，劳了筋骨，饿了体肤后，你毅然站立在前进的路上，做着坚韧上进的自己!").build());

        //游戏公告
        put(AppDisplayTypeEnum.NOTICE_DISPLAY_FOR_GAME.name(), NoticeVO.builder().type(51)
                .title("是时候赋予自己力量了！")
                .content("想着要赢得比赛不会给你力量，在锻炼中挣扎力量才会增长，当你克服困难不想放弃时，这就是力量。——阿诺德·施瓦辛格").build());

        //最新公告
        put(AppDisplayTypeEnum.NOTICE_DISPLAY_FOR_NEWEST.name(), NoticeVO.builder().type(52)
                .title("坚持，你会变得更优秀！")
                .content("每一个优秀的人，都有一段沉默的时光。那一段时光，是付出了很多努力，忍受孤独和寂寞，不抱怨不诉苦，日后说起时，连自己都能被感动的日子。").build());

    }};


    //这是一条华丽的分割线 >> =============================================================================================


    /**
     * 根据类型查询广告
     */
    public List<AdVO> findAdsByType(AppDisplayTypeEnum adtEnum) {
        List<AdVO> voList = new ArrayList<>();
        if (adtEnum != null) {
            for (Map.Entry<String, List<AdVO>> adMap : adList.entrySet()) {
                if (Objects.equals(adMap.getKey(), adtEnum.name())) {
                    voList = adMap.getValue();
                }
            }
        }
        return voList;
    }

    /**
     * 根据类型查询轮播图
     */
    public List<AdVO> findBannersByType(AppDisplayTypeEnum adtEnum) {
        List<AdVO> voList = new ArrayList<>();
        if (adtEnum != null) {
            for (Map.Entry<String, List<AdVO>> adMap : bannerList.entrySet()) {
                if (Objects.equals(adMap.getKey(), adtEnum.name())) {
                    voList = adMap.getValue();
                }
            }
        }

        return voList;
    }


    /**
     * 根据类型查询系统公告
     */
    public List<NoticeVO> findNoticeByType(AppDisplayTypeEnum adtEnum) {
        List<NoticeVO> voList = new ArrayList<>();
        if (adtEnum != null) {
            for (Map.Entry<String, NoticeVO> adMap : noticeList.entrySet()) {
                if (Objects.equals(adMap.getKey(), adtEnum.name())) {
                    voList.add(adMap.getValue());
                }
            }
        }
        return voList;
    }


    /**
     * 游客进入自动注册
     */
    public Map<String, Object> visitorAutoRegister(UserDTO userDTO) {
        String macAddr = userDTO.getMacAddr();
        String cacheKey = StringUtils.join(USER_INFO_CACHE, macAddr);
        User user;
        if (cache.hHasKey(cacheKey, "id")) {
            user = JSONObject.toJavaObject((JSON) JSON.toJSON(cache.hmget(cacheKey)), User.class);
        } else {
            user = User.builder().id(UUID.randomUUID().toString()).macAddr(userDTO.getMacAddr()).userLevel(1)
                    .viewingCount(USER_VIEWING_MAX).saveCount(5).status(UserStatusEnum.NORMAL.code()).usedSaveCount(0)
                    .usedViewingCount(0).recommendCount(0).awardSaveCount(0).awardViewingCount(0).registerTime(new Date())
                    .nickname("新用户" + RandomKeyUtil.getRandomStr(6).toUpperCase()).build();

            Map<String, Object> map = (Map<String, Object>) JSONObject.parseObject(JSON.toJSONString(user), Map.class);

            cache.hmset(cacheKey, map, DateUtil.getSurplusSecondOfToday());
        }

        try {
            Map<String, Object> result = new HashMap<>();
            String currentTimeMillis = String.valueOf(System.currentTimeMillis());
            String sign = JwtUtil.sign(macAddr, VideoContants.VIDEO_SITE_USER_CIPHERTEXT, currentTimeMillis, EXPIRE_TIME);
            UserInfoVO userInfo = UserInfoVO.builder().build();
            BeanUtils.copyProperties(user, userInfo);
            result.put("token", sign);
            result.put("userInfo", userInfo);
            return result;
        } catch (Exception e) {
            throw new ResponsiveException("游客登录失败！");
        }
    }

    /**
     * 获取用户详情信息
     */
    public UserInfoVO getUserInfo(ServletRequest request) {
        String token = JwtUtil.getToken(request);
        String macAddr = JwtUtil.getAccount(token);
        if (StringUtils.isBlank(macAddr))
            throw new ResponsiveException(ExCodeMsgEnum.ACCOUNT_NOT_EXISTS);

        String cacheKey = StringUtils.join(USER_INFO_CACHE, macAddr);
        User user = JSONObject.toJavaObject((JSON) JSON.toJSON(cache.hmget(cacheKey)), User.class);

        return UserInfoVO.builder().id(user.getId()).gender(user.getGender())
                .nickname(user.getNickname()).profile(user.getProfile()).userIcon(user.getUserIcon())
                .userLevel(user.getUserLevel()).phone(RegsUtil.shadowPhone(user.getPhone())).build();
    }

    /**
     * 获取当前用户信息
     */
    public User getCurrentUser(ServletRequest request) {
        String token = JwtUtil.getToken(request);
        String macAddr = JwtUtil.getAccount(token);
        String cacheKey = StringUtils.join(USER_INFO_CACHE, macAddr);
        return JSONObject.toJavaObject((JSON) JSON.toJSON(cache.hmget(cacheKey)), User.class);
    }

    /**
     * 更新缓存用户信息
     */
    private void updateUser(User user) {
        String cacheKey = StringUtils.join(USER_INFO_CACHE, user.getMacAddr());
        Map<String, Object> map = (Map<String, Object>) JSONObject.parseObject(JSON.toJSONString(user), Map.class);
        cache.hmset(cacheKey, map, DateUtil.getSurplusSecondOfToday());
    }

    /**
     * 获取用户观影信息
     */
    public VideoViewingInfoVO getViewingCount(ServletRequest request) {
        User user = getCurrentUser(request);
        return VideoViewingInfoVO.builder().id(user.getId()).nextLevelCount(3).phone(user.getPhone())
                .recommendCount(user.getRecommendCount()).saveCount(user.getSaveCount()).usedSaveCount(user.getSaveCount())
                .usedViewingCount(user.getUsedSaveCount()).viewingCount(user.getViewingCount()).build();
    }

    /**
     * 获取用户Mac地址
     */
    public ArmorDTO getUserMacAddr(ServletRequest request, ArmorDTO dto) {
        dto.setMacAddr(getUserMacAddr(request));
        return dto;
    }

    /**
     * 获取用户Mac地址
     */
    private String getUserMacAddr(ServletRequest request) {
        String token = JwtUtil.getToken(request);
        return JwtUtil.getAccount(token);
    }

    /**
     * 添加收藏视频
     */
    public void addCollection(ArmorDTO dto) {
        String cacheKey = USER_COLLECTION_CACHE + dto.getMacAddr();
        if (!cache.sHasKey(cacheKey, dto.getVideoId())) {
            cache.sSetAndTime(cacheKey, EXPIRE_TIME, dto.getVideoId());
        }

    }

    /**
     * 查询用户收藏列表
     */
    public List<ArmorVideoInfo> getVideoCollectionList(ArmorDTO dto) {
        String cacheKey = USER_COLLECTION_CACHE + dto.getMacAddr();
        return getArmorVideoInfos(cacheKey);
    }


    /**
     * 取消收藏视频
     */
    public void removeCollection(ArmorDTO dto) {
        String cacheKey = USER_COLLECTION_CACHE + dto.getMacAddr();
        if (cache.sHasKey(cacheKey, dto.getVideoId())) {
            cache.setRemove(cacheKey, dto.getVideoId());
        }
    }

    /**
     * 获取视频播放信息
     */
    public Map<String, Object> getVideoPlayInfo(ArmorDTO dto) {
        User currentUser = dto.getUser();

        //获取视频相关信息
        VideoLabelListVO labelsByVId = new VideoLabelListVO();
        ArmorVideoInfo armorVideoInfo = new ArmorVideoInfo();
        if (StringUtils.isNotBlank(dto.getVideoId())) {
            for (ArmorVideoInfo video : videos) {
                if (Objects.equals(dto.getVideoId(), video.getId())) {
                    labelsByVId = labelsByVId.setVId(video.getId()).setVCode(null).setVCreateDate(video.getCreateDate().getTime())
                            .setVPlaySum(video.getPlaySum()).setVTitle(video.getTitle()).setSaveUrl(null)
                            .setPlayUrl(video.getPlayUrl());

                    armorVideoInfo = video;
                }
            }
        }

        boolean entitleViewing = true;
        if (currentUser.getViewingCount() != -1 && currentUser.getUsedViewingCount() >= currentUser.getViewingCount()) {
            labelsByVId.setPlayUrl(null);
            entitleViewing = false;
        }

        Map<String, Object> result = new HashMap<>();
        result.put("videoInfo", labelsByVId);
        //获取广告相关信息
        result.put("playPageAd", playAdList);

        if (entitleViewing) {
            //用户使用观影次数加一
            updateUser(currentUser.setUsedViewingCount(BasicUtil.getNumber(currentUser.getUsedViewingCount()) + 1));

            //视频播放量加一
            armorVideoInfo.setPlaySum(BasicUtil.getNumber(armorVideoInfo.getPlaySum()) + 1);

            //添加用户观影记录
            addViewing(ArmorDTO.builder().macAddr(currentUser.getMacAddr()).videoId(armorVideoInfo.getId()).build());
        }

        return result;
    }

    /**
     * 添加用户观影记录
     */
    public void addViewing(ArmorDTO dto) {
        String cacheKey = USER_VIEWING_CACHE + dto.getMacAddr();
        if (!cache.sHasKey(cacheKey, dto.getVideoId())) {
            cache.sSetAndTime(cacheKey, EXPIRE_TIME, dto.getVideoId());
        }
    }


    /**
     * 清空用户所有观影记录
     */
    public void removeViewing(ArmorDTO dto) {
        String cacheKey = USER_VIEWING_CACHE + dto.getMacAddr();
        if (cache.sGetSetSize(cacheKey) > 0) {
            cache.del(cacheKey);
        }
    }

    /**
     * 查询观影历史记录
     */
    public List<ArmorVideoInfo> getUserViewingRecord(ArmorDTO dto) {
        String cacheKey = USER_VIEWING_CACHE + dto.getMacAddr();
        return getArmorVideoInfos(cacheKey);
    }


    /**
     * 获取视频列表
     */
    private List<ArmorVideoInfo> getArmorVideoInfos(String cacheKey) {
        List<String> videoIds = JSONArray.parseArray(JSON.toJSONString(cache.sGet(cacheKey)), String.class);
        List<ArmorVideoInfo> result = new ArrayList<>();
        if (!ListUtils.isEmpty(videoIds)) {
            for (ArmorVideoInfo video : videos) {
                if (videoIds.contains(video.getId()))
                    result.add(video);
            }
        }
        return result;
    }

    /**
     * 根据视频id查询视频评论列表
     */
    public List<VideoComment> getVideoCommentList(ArmorDTO dto) {
        List<VideoComment> videoComments = new ArrayList<>();
        for (Map.Entry<String, List<VideoComment>> entry : videoCommentList.entrySet()) {
            if (Objects.equals(entry.getKey(), dto.getVideoId())) {
                videoComments = entry.getValue();
            }
        }

        //降序排列
        videoComments.sort(Comparator.comparing(VideoComment::getTime).reversed());
        return videoComments;
    }

    /**
     * 获取 视频列表
     */
    public List<VideoBasicInfoVO> getVideoList(VideoTypeEnum type) {
        //根据不同查询方式排序
        int total = 10;
        switch (type) {
            case Latest:
                videos.sort(Comparator.comparing(ArmorVideoInfo::getCreateDate).reversed());
                break;
            case Hottest:
                videos.sort(Comparator.comparing(ArmorVideoInfo::getPlaySum).reversed());
                break;
            case ULikes:
                videos.sort(Comparator.comparing(ArmorVideoInfo::getTitle));
                break;
            case Featured:
                videos.sort(Comparator.comparing(ArmorVideoInfo::getTimeLen).reversed());
                total = 6; //编辑精选视频只展示6部
                break;
            case Relevant:
                videos.sort(Comparator.comparing(ArmorVideoInfo::getId).reversed());
                total = 6; //相关视频只展示6部
                break;
        }

        List<VideoBasicInfoVO> voList = new ArrayList<>(total);
        for (int i = 0; i < videos.size() && i < total; i++) {
            ArmorVideoInfo video = videos.get(i);
            voList.add(new VideoBasicInfoVO().setId(video.getId()).setCover(video.getCover())
                    .setTitle(video.getTitle()).setPlaySum(video.getPlaySum()).setTimeLen(video.getTimeLen())
                    .setCreateDate(video.getCreateDate()));
        }

        return voList;
    }


}


@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
class ArmorDTO {

    private String macAddr;

    private String videoId;

    private User user;

    private String videoType;
}


@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
class ArmorVideoInfo extends VideoBasicInfoVO {

    /**
     * 视频播放路径
     */
    private String playUrl;

    /**
     * 视频总播放量
     */
    private Long playSum;

}

enum VideoTypeEnum {

    Featured(1, "编辑精选视频"),
    ULikes(2, "猜你喜欢视频"),
    Latest(3, "最新视频"),
    Hottest(4, "最热视频"),
    Relevant(5, "相关视频");

    private int type;

    private String name;

    VideoTypeEnum(int type, String name) {
        this.type = type;
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }
}
