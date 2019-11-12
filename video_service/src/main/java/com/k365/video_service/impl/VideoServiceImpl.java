package com.k365.video_service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.k365.manager_service.AdService;
import com.k365.manager_service.DomainService;
import com.k365.manager_service.SysConfParamService;
import com.k365.user_service.UserService;
import com.k365.user_service.UserVideoFabulousService;
import com.k365.user_service.UserViewingRecordService;
import com.k365.video_base.common.*;
import com.k365.video_base.mapper.VideoMapper;
import com.k365.video_base.model.dto.UserActionAnaylzeDTO;
import com.k365.video_base.model.dto.VideoDTO;
import com.k365.video_base.model.dto.VideoSubjectDTO;
import com.k365.video_base.model.po.*;
import com.k365.video_base.model.so.VideoSO;
import com.k365.video_base.model.vo.*;
import com.k365.video_common.constant.StatusEnum;
import com.k365.video_common.constant.SysParamNameEnum;
import com.k365.video_common.constant.SysParamValueNameEnum;
import com.k365.video_common.exception.GeneralException;
import com.k365.video_common.exception.ResponsiveException;
import com.k365.video_common.handler.fastdfs.FastDFSHelper;
import com.k365.video_common.util.*;
import com.k365.video_service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.ListUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.ServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
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
@Slf4j
public class VideoServiceImpl extends ServiceImpl<VideoMapper, Video> implements VideoService {

    @Autowired
    private VideoActorService videoActorService;

    @Autowired
    private VideoLabelVideoService videoLabelVideoService;

    @Autowired
    private VideoChannelVideoService videoChannelVideoService;

    @Autowired
    private VideoSubjectVideoService videoSubjectVideoService;

    @Autowired
    private VideoCommentService videoCommentService;

    @Autowired
    private VVideoChannelLabelService vVideoChannelLabelService;

    @Autowired
    private UserViewingRecordService userViewingRecordService;

    @Autowired
    private AdService adService;

    @Autowired
    private RedisUtil cache;

    @Autowired
    private UserService userService;

    @Autowired
    private SysConfParamService sysConfParamService;

    @Autowired
    private VVideoSubjectService vVideoSubjectService;

    @Autowired
    private VVideoLabelChannelSubjectActorService vVideoLabelChannelSubjectActorService;

    @Autowired
    @Lazy
    private DomainService domainService;

    @Autowired
    private UserActionAnalyzeService userActionAnalyzeService;

    @Autowired
    private UserVideoFabulousService userVideoFabulousService;


    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public void add(VideoDTO videoDTO) {
        //保存视频信息
        Video video = Video.builder().build();
        BeanUtils.copyProperties(videoDTO, video);
        video.setCreateDate(new Date());
        video.setSaveSum(0L);
        video.setPlaySum(0L);
        video.setPlayCountForMonth(0L);
        video.setPlayCountForWeek(0L);
        video.setPlayCountForDay(0L);
        video.setStatus(StatusEnum.ENABLE.key());
        boolean vSaved = this.save(video);
        if (vSaved) {
            String vId = video.getId();
            //C.新增视频标签关系
            if (videoDTO.getVideoLabelIds() != null && videoDTO.getVideoLabelIds().length != 0) {
                VideoLabelVideo[] vlvs = new VideoLabelVideo[videoDTO.getVideoLabelIds().length];
                for (int i = 0; i < videoDTO.getVideoLabelIds().length; i++) {
                    vlvs[i] = VideoLabelVideo.builder().videoId(vId).videoLabelId(videoDTO.getVideoLabelIds()[i]).build();
                }
                videoLabelVideoService.saveBatch(Arrays.asList(vlvs));
            }

            //D.新增视频频道关系
            if (videoDTO.getVideoChannelIds() != null && videoDTO.getVideoChannelIds().length != 0) {
                VideoChannelVideo[] vcvs = new VideoChannelVideo[videoDTO.getVideoChannelIds().length];
                for (int i = 0; i < videoDTO.getVideoChannelIds().length; i++) {
                    vcvs[i] = VideoChannelVideo.builder().videoId(vId).videoChannelId(videoDTO.getVideoChannelIds()[i]).build();
                }
                videoChannelVideoService.saveBatch(Arrays.asList(vcvs));
            }

            //E.新增视频女优关系
            if (videoDTO.getVideoActorIds() != null && videoDTO.getVideoActorIds().length != 0) {
                VideoActor[] vas = new VideoActor[videoDTO.getVideoActorIds().length];
                for (int i = 0; i < videoDTO.getVideoActorIds().length; i++) {
                    vas[i] = VideoActor.builder().videoId(vId).actorId(videoDTO.getVideoActorIds()[i]).build();
                }
                videoActorService.saveBatch(Arrays.asList(vas));
            }

            //F.新增视频主题关系
            if (videoDTO.getVideoSubjectIds() != null && videoDTO.getVideoSubjectIds().length != 0) {
                VideoSubjectVideo[] vsvs = new VideoSubjectVideo[videoDTO.getVideoSubjectIds().length];
                for (int i = 0; i < videoDTO.getVideoSubjectIds().length; i++) {
                    vsvs[i] = VideoSubjectVideo.builder().videoId(vId).videoSubjectId(videoDTO.getVideoSubjectIds()[i]).build();
                }
                videoSubjectVideoService.saveBatch(Arrays.asList(vsvs));
            }
        }
    }

    @Override
    public VideoListVO findById(String vId) {
        Video video = this.getById(vId);
        if (video == null) {
            throw new RuntimeException("视频不存在或已被删除");
        }

        List<VVideoLabelChannelSubjectActor> vvlcsaList = vVideoLabelChannelSubjectActorService
                .list(new QueryWrapper<VVideoLabelChannelSubjectActor>().eq("v_id", video.getId()));

        VideoListVO resultVo = new VideoListVO();
        BeanUtils.copyProperties(video, resultVo);

        if (!ListUtils.isEmpty(vvlcsaList)) {
            resultVo.setVideoChannels(new ArrayList<>());
            resultVo.setVideoLabels(new ArrayList<>());
            resultVo.setActors(new ArrayList<>());
            resultVo.setVideoSubjects(new ArrayList<>());

            Set<String> idSet = new HashSet<>();
            vvlcsaList.forEach(vvlcsa -> {
                if (vvlcsa.getVcId() != null && !idSet.contains("vc-" + vvlcsa.getVcId())) {
                    resultVo.getVideoChannels().add(VideoChannelVO.builder().name(vvlcsa.getVcName()).id(vvlcsa.getVcId()).build());
                    idSet.add("vc-" + vvlcsa.getVcId());
                }

                if (vvlcsa.getVlId() != null && !idSet.contains("vl-" + vvlcsa.getVlId())) {
                    resultVo.getVideoLabels().add(VideoLabelVO.builder().vlId(vvlcsa.getVlId()).vlName(vvlcsa.getVlName()).build());
                    idSet.add("vl-" + vvlcsa.getVlId());
                }

                if (vvlcsa.getAId() != null && !idSet.contains("a-" + vvlcsa.getAId())) {
                    resultVo.getActors().add(ActorVO.builder().id(vvlcsa.getAId()).name(vvlcsa.getAName()).build());
                    idSet.add("a-" + vvlcsa.getAId());
                }

                if (vvlcsa.getVsId() != null && !idSet.contains("vs-" + vvlcsa.getVsId())) {
                    resultVo.getVideoSubjects().add(VideoSubjectVO.builder().id(vvlcsa.getVsId()).name(vvlcsa.getVsName()).build());
                    idSet.add("vs-" + vvlcsa.getVsId());
                }
            });
        }

        return resultVo;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public void update(VideoDTO videoDTO) {

        Video video = this.getById(videoDTO.getId());
        if (video == null) {
            throw new RuntimeException("视频不存在或已被删除");
        }

        videoDTO.setId(video.getId());
        BeanUtils.copyProperties(videoDTO, video);
        boolean updateSuccess = this.updateById(video);

        String id = videoDTO.getId();
        if (updateSuccess) {
            //A.修改视频标签关系
            videoLabelVideoService.updateByVideoId(id, videoDTO.getVideoLabelIds());

            //B.修改视频频道关系
            videoChannelVideoService.updateByVideoId(id, videoDTO.getVideoChannelIds());

            int length = videoDTO.getVideoActorIds().length;
            //C.修改视频女优关系
            if (length == 0) {
                videoActorService.removeByVideoId(id);
            } else {
                videoActorService.updateByVideoId(id, videoDTO.getVideoActorIds());
            }

            //D.修改视频主题关系
            videoSubjectVideoService.updateByVideoId(id, videoDTO.getVideoSubjectIds());

        }
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public void removeByVideoId(String id) {
        Video video = this.getById(id);
        if (video == null) {
            throw new RuntimeException("视频不存在或已被删除");
        }

        //TODO 删除视频资源
        //删除视频
        boolean vRemoved = this.removeById(id);

        if (vRemoved) {
            //A.删除视频标签关系
            videoLabelVideoService.removeByVideoId(id);

            //B.删除视频频道关系
            videoChannelVideoService.removeByVideoId(id);

            //C.删除视频女优关系
            videoActorService.removeByVideoId(id);

            //D.删除视频主题关系
            videoSubjectVideoService.removeByVideoId(id);

            //E.删除视频评论信息
            videoCommentService.removeByVideoId(id);

            //删除视频封面图片
            if (StringUtils.isNotBlank(video.getCover())) {
                String[] dfsPath = video.getCover().split("/", 5);
                FastDFSHelper.deleteFile(dfsPath[3], dfsPath[4]);
            }
        }

    }

    @Override
    public List<VideoBasicInfoVO> searchByKeyword(VideoSO videoSO, ServletRequest request) {

        String domain = domainService.getDomain(request);
        String domain2 = domainService.getAppPicDomain();//图片封面域名
        videoSO.setSearchValue(StringUtils.join(videoSO.getSearchValue(), "*"));
        videoSO.setPage((videoSO.getPage() - 1) * videoSO.getPageSize());
        List<Video> videos = this.baseMapper.searchMatchVideoPage(videoSO);

        List<VideoBasicInfoVO> voList = new ArrayList<>();
        if (!ListUtils.isEmpty(videos)) {
            for (Video video : videos) {
                VideoBasicInfoVO videoBasicInfoVO = new VideoBasicInfoVO().setTimeLen(video.getTimeLen()).setPlaySum(video.getPlaySum())
                        .setCover(video.getCover()).setTitle(video.getTitle()).setCreateDate(video.getCreateDate())
                        .setId(video.getId()).setIsVip(video.getIsVip());

                if (videoBasicInfoVO.getCover() != null && !videoBasicInfoVO.getCover().equals("")) {
                    videoBasicInfoVO.setCover(domain2 + Trim.custom_ltrim(videoBasicInfoVO.getCover(), "group"));
                }
                voList.add(videoBasicInfoVO);
            }

            /* videos.forEach(video ->
                    voList.add(new VideoBasicInfoVO().setTimeLen(video.getTimeLen()).setPlaySum(video.getPlaySum())
                            .setCover(video.getCover()).setTitle(video.getTitle()).setCreateDate(video.getCreateDate())
                            .setId(video.getId()))

            );*/
        }

        return voList;
    }

    @Override
    public void updateVideoStatusById(Integer id, StatusEnum statusEnum) {
        Video video = this.getById(id);
        if (video == null)
            throw new ResponsiveException("视频不存在或已被删除");

        video.setStatus(statusEnum.key());
        this.updateById(video);
    }

    @Override
    public BaseListVO<VideoVO> getByPage(VideoDTO videoDTO, ServletRequest request) {

        String domain = domainService.getDomain(request);
        String domain2 = domainService.getAppPicDomain();//图片封面域名
        IPage<Video> page = this.page(new Page<Video>().setCurrent(videoDTO.getPage()).setSize(videoDTO.getPageSize()),
                new QueryWrapper<Video>().orderByDesc("create_date"));

        List<VideoVO> voList = new ArrayList<>();
        if (!ListUtils.isEmpty(page.getRecords())) {
            page.getRecords().forEach(video -> {
                VideoVO vo = new VideoVO();
                BeanUtils.copyProperties(video, vo);

                if (vo.getCover() != null && !vo.getCover().equals("")) {
                    vo.setCover(domain2 + Trim.custom_ltrim(vo.getCover(), "group"));
                }
                if (vo.getPlayUrl() != null && !vo.getPlayUrl().equals("")) {
                    vo.setPlayUrl(domain + Trim.custom_ltrim(vo.getPlayUrl(), "group"));
                }
                voList.add(vo);
            });
        }

        BaseListVO<VideoVO> baseListVO = new BaseListVO<VideoVO>().setTotal(page.getTotal());
        baseListVO.setList(voList);
        return baseListVO;
    }

    @Override
    public BaseListVO<VideoVO> searchByVideoType(VideoSO videoSO, ServletRequest request) {

        String domain = domainService.getDomain(request);
        String domain2 = domainService.getAppPicDomain();//图片封面域名
        BaseListVO<VVideoChannelLabelVO> vVideoChannelLabelVO =
                vVideoChannelLabelService.searchByKeyWordAndType(videoSO);

        BaseListVO<VideoVO> result = new BaseListVO<>();
        if (CollectionUtils.isNotEmpty(vVideoChannelLabelVO.getList())) {
            result.setTotal(vVideoChannelLabelVO.getTotal());
            List<VideoVO> voList = new ArrayList<>();
            for (VVideoChannelLabelVO vvcl : vVideoChannelLabelVO.getList()) {
                VideoVO vo = VideoVO.builder()
                        .code(vvcl.getVCode()).cover(vvcl.getVCover()).createDate(vvcl.getVCreateDate())
                        .id(vvcl.getVId()).title(vvcl.getVTitle()).build();

                if (vo.getCover() != null && !vo.getCover().equals("")) {
                    vo.setCover(domain2 + Trim.custom_ltrim(vo.getCover(), "group"));
                }

                if (vo.getPlayUrl() != null && !vo.getPlayUrl().equals("")) {
                    vo.setPlayUrl(domain + Trim.custom_ltrim(vo.getPlayUrl(), "group"));
                }
                voList.add(vo);
            }

           /* vVideoChannelLabelVO.getList().forEach(vvcl -> voList.add(VideoVO.builder()
                    .code(vvcl.getVCode()).cover(vvcl.getVCover()).createDate(vvcl.getVCreateDate())
                    .id(vvcl.getVId()).title(vvcl.getVTitle()).build()));*/


            result.setList(voList);
        }

        return result;
    }


    @Override
    public List<VideoBasicInfoVO> findULikes(VideoDTO videoDTO, ServletRequest request) {
        User currentUser = UserContext.getCurrentUser();

        String macAddr = currentUser.getMacAddr();

        //根据macAddr查询videoLabelId
        List<UserActionAnalyze> userActionAnalyzeList = userActionAnalyzeService.findUserActionAnaylzeListByMacAddr(macAddr);

        List<VideoBasicInfoVO> resultList = new ArrayList<>();
        List<Video> videos=null;
        //查询置顶视频   置换前两部视频.
        if(videoDTO.getPage()==1) {
            List<Video> topLike = this.list(new QueryWrapper<Video>().eq("top_like", 1).eq("status", StatusEnum.ENABLE.key()));
            String domain2 = domainService.getAppPicDomain();//图片封面域名
            if (topLike.size() <= 2 && topLike.size() > 0) {
                for (Video po : topLike) {
                    VideoBasicInfoVO vo = new VideoBasicInfoVO().setId(po.getId())
                            .setCover(po.getCover()).setTitle(po.getTitle()).setPlaySum(po.getPlaySum())
                            .setTimeLen(po.getTimeLen()).setCreateDate(po.getCreateDate()).setIsVip(po.getIsVip()).setTopLike(po.getTopLike());

                    if (vo.getCover() != null && !vo.getCover().equals("")) {
                        vo.setCover(domain2 + Trim.custom_ltrim(vo.getCover(), "group"));
                    }
                    resultList.add(vo);
                }
            }
        }

        if (userActionAnalyzeList.size() > 0){
            if(cache.get(currentUser.getId()+"v")==null){
            Map<Integer,Integer> map=new HashMap<>();

            for (int i = 0; i < userActionAnalyzeList.size(); i++) {
                if(null==map.get(userActionAnalyzeList.get(i).getVideoLabelId())){
                    map.put(userActionAnalyzeList.get(i).getVideoLabelId(),1);
                }else{
                    map.put(userActionAnalyzeList.get(i).getVideoLabelId(),map.get(userActionAnalyzeList.get(i).getVideoLabelId())+1);

                }
            }
            //map值降序
            map= MapSortUtil.sortDescend(map);
            //判断map中key的个数,创建一个list用来装标签id
            List<Integer> labelList=new ArrayList<>();
            if(map.size()<=4){
                for (Integer key : map.keySet()) {
                    labelList.add(key);
                }
            }else{
                int i=0;
                for (Integer key : map.keySet()) {
                    if(i<4){
                        labelList.add(key);
                        i=i+1;
                    }else{
                        break;
                    }
                }
            }
            //通过标签查询视频ids
            videos = videoLabelVideoService.getVideoVoByLabels(labelList);
            if(videos.size()<100){
                Integer need=100-videos.size();
                //查询最新视频列表
                IPage<Video> page = this.page(new Page<Video>().setCurrent(1).setSize(need),
                        new QueryWrapper<Video>().eq("status", StatusEnum.ENABLE.key()).notIn("top_like",1).orderByDesc("create_date"));
                List<Video> records = page.getRecords();
                //补全100个视频列表
                for(Video v:records){
                    videos.add(v);
                }
            }
                cache.set(currentUser.getId() + "v",videos,3600);
            }else{
                videos = (List<Video>) cache.get(currentUser.getId() + "v");
            }

            


            //给100个视频分页
            List<Video> pageList=new ArrayList<>();
            //如果传过来的查询是在100条结果以外，那么返回null
            if(videoDTO.getPage()*videoDTO.getPageSize()>videos.size()){
                return null;
            }
            //计算角标
            Integer i=videoDTO.getPageSize()*(videoDTO.getPage()-1);
            //计算最大页码
            Integer maxPage=videos.size()/videoDTO.getPage()+1;
            //计算最大页码的视频个数
            Integer maxPageCount=videos.size()-(maxPage-1)*videoDTO.getPage();
            //计算最大页码角标起始值
            Integer maxPageStart=videoDTO.getPageSize()*(maxPage-1)+1;
            if(i<maxPageStart){
                for(int j=0;j<videoDTO.getPageSize();j++){
                    pageList.add(videos.get(i));
                    i=i+1;
                }
            }else{
                for(int j=0;j<maxPageCount;j++){
                    pageList.add(videos.get(i));
                    i=i+1;
                }
            }
            //po转vo
            String domain2 = domainService.getAppPicDomain();//图片封面域名
            for (Video po : pageList) {
                VideoBasicInfoVO vo = new VideoBasicInfoVO().setId(po.getId())
                        .setCover(po.getCover()).setTitle(po.getTitle()).setPlaySum(po.getPlaySum())
                        .setTimeLen(po.getTimeLen()).setCreateDate(po.getCreateDate()).setIsVip(po.getIsVip());

                if (vo.getCover() != null && !vo.getCover().equals("")) {
                    vo.setCover(domain2 + Trim.custom_ltrim(vo.getCover(), "group"));
                }
                if(po.getTopLike()!=1){
                    resultList.add(vo);
                }
            }

        }else{
            //查询最新视频
            String domain = domainService.getDomain(request);
            String domain2 = domainService.getAppPicDomain();//图片封面域名
            IPage<Video> page = this.page(new Page<Video>().setCurrent(videoDTO.getPage()).setSize(videoDTO.getPageSize()),
                    new QueryWrapper<Video>().eq("status", StatusEnum.ENABLE.key()).notIn("top_like",1).orderByDesc("create_date"));

            List<Video> records = page.getRecords();
            for (Video vo : records) {
                if (vo.getCover() != null && !vo.getCover().equals("")) {
                    vo.setCover(domain2 + Trim.custom_ltrim(vo.getCover(), "group"));
                }

                if (vo.getPlayUrl() != null && !vo.getPlayUrl().equals("")) {
                    vo.setPlayUrl(domain + Trim.custom_ltrim(vo.getPlayUrl(), "group"));
                }
            }

            List<VideoBasicInfoVO> videoBasicInfoVOS = videoDataConverter(page, true, request);
            videoBasicInfoVOS.addAll(0, resultList);
            return videoBasicInfoVOS;
        }

        if (resultList.size() > 0) {
            adService.getAd4User(resultList, request);
        }


        return resultList;
    }

    @Override
    public List<VideoBasicInfoVO> findFeatured(VideoDTO videoDTO, ServletRequest request) {
       /* String domain = domainService.getDomain(request);
        String domain2 = domainService.getAppPicDomain();//图片封面域名
        List<VideoBasicInfoVO> voList = new ArrayList<>();

        if(videoDTO.getPage()==1) {
            //查询置顶视频   置换前两部视频
            List<Video> topLike = this.list(new QueryWrapper<Video>().eq("top_perfect", 1).eq("status", StatusEnum.ENABLE.key()));
            List<VideoBasicInfoVO> topList = new ArrayList<>();
            if (topLike.size() <= 2 && topLike.size() > 0) {
                for (Video po : topLike) {
                    VideoBasicInfoVO vo = new VideoBasicInfoVO().setId(po.getId())
                            .setCover(po.getCover()).setTitle(po.getTitle()).setPlaySum(po.getPlaySum())
                            .setTimeLen(po.getTimeLen()).setCreateDate(po.getCreateDate()).setIsVip(po.getIsVip()).setTopPerfect(po.getTopPerfect());

                    if (vo.getCover() != null && !vo.getCover().equals("")) {
                        vo.setCover(domain2 + Trim.custom_ltrim(vo.getCover(), "group"));
                    }
                    voList.add(vo);
                }
            }
        }


        List<AdVO> adsByType = adService.findAdsByType(AppDisplayTypeEnum.AD_DISPLAY_FOR_FEATURED_VIDEO, request);
        if (!ListUtils.isEmpty(adsByType)) {
            AdVO vo = adsByType.get(0);
            //添加一个广告
            voList.add(new VideoBasicInfoVO().setCover(vo.getCover()).setIsAd(true)
                    .setTitle(vo.getTitle()).setAdUrl(vo.getDetailsUrl()));

            videoDTO.setPageSize(videoDTO.getPageSize() - 1);
        }
        if(videoDTO.getPage()==1){
            videoDTO.setPageSize(6-voList.size());
        }

        List<Video> list = this.page(new Page<Video>().setCurrent(videoDTO.getPage()).setSize(videoDTO.getPageSize()),
                new QueryWrapper<Video>().eq("status", StatusEnum.ENABLE.key())
                        .select("id,cover,title,play_sum,time_len,create_date,is_vip").notIn("top_perfect",1)
                        .orderByDesc("play_sum")).getRecords();

        if (!ListUtils.isEmpty(list)) {
            for (Video video : list) {
                VideoBasicInfoVO vo = new VideoBasicInfoVO().setId(video.getId()).setCover(video.getCover())
                        .setTitle(video.getTitle()).setPlaySum(video.getPlaySum()).setTimeLen(video.getTimeLen())
                        .setCreateDate(video.getCreateDate()).setIsVip(video.getIsVip());

                if (vo.getCover() != null && !vo.getCover().equals("")) {
                    vo.setCover(domain2 + Trim.custom_ltrim(vo.getCover(), "group"));
                }
                voList.add(vo);

            }

           *//* list.forEach(video -> voList.add(new VideoBasicInfoVO().setId(video.getId()).setCover(video.getCover())
                    .setTitle(video.getTitle()).setPlaySum(video.getPlaySum()).setTimeLen(video.getTimeLen())
                    .setCreateDate(video.getCreateDate())));*//*
        }*/
        List<VideoBasicInfoVO> voList=new ArrayList<>();

        //查询顶部视频
        IPage<Video> page1 = this.page(new Page<Video>().setSize(2).setCurrent(videoDTO.getPage()), new QueryWrapper<Video>().eq("top_perfect",1).orderByDesc("create_date"));
        List<Video> topList = page1.getRecords();
        //查询中间两部最新视频
        IPage<Video> page2 = this.page(new Page<Video>().setSize(6).setCurrent(videoDTO.getPage()), new QueryWrapper<Video>().notIn("top_perfect",1).notIn("is_vip",1).notIn("is_svip",1).orderByDesc("create_date"));
        List<Video> midList = page2.getRecords();
        //查询VIP视频
        IPage<Video> page3 = this.page(new Page<Video>().setSize(1).setCurrent(videoDTO.getPage()), new QueryWrapper<Video>().notIn("top_perfect",1).eq("is_vip",1).orderByDesc("create_date"));
        List<Video> buttomLeft = page3.getRecords();
        //查询SVIP视频
        IPage<Video> page4 = this.page(new Page<Video>().setSize(1).setCurrent(videoDTO.getPage()), new QueryWrapper<Video>().notIn("top_perfect",1).eq("is_svip",1).orderByDesc("create_date"));
        List<Video> buttomRight = page4.getRecords();

        if(topList.size()==1){
            midList.set(0,topList.get(0));
        }
        if(topList.size()==2){
            midList.set(0,topList.get(0));
            midList.set(1,topList.get(1));
        }
        if(buttomLeft.size()==1){
            midList.set(4,buttomLeft.get(0));
        }
        if(buttomRight.size()==1){
            midList.set(5,buttomRight.get(0));
        }

        String domain2 = domainService.getAppPicDomain();//图片封面域名
        for (Video video : midList) {
            VideoBasicInfoVO vo = new VideoBasicInfoVO().setId(video.getId()).setCover(video.getCover())
                    .setTitle(video.getTitle()).setPlaySum(video.getPlaySum()).setTimeLen(video.getTimeLen())
                    .setCreateDate(video.getCreateDate()).setIsVip(video.getIsVip()).setIsSvip(video.getIsSvip()).setTopPerfect(video.getTopPerfect());

            if (video.getCover() != null && !video.getCover().equals("")) {
                vo.setCover(domain2 + Trim.custom_ltrim(video.getCover(), "group"));
            }

            voList.add(vo);

        }
        return voList;
    }

    @Override
    public List<VideoBasicInfoVO> findLatest(VideoDTO videoDTO, ServletRequest request) {

        String domain = domainService.getDomain(request);
        String domain2 = domainService.getAppPicDomain();//图片封面域名
        IPage<Video> page = this.page(new Page<Video>().setCurrent(videoDTO.getPage()).setSize(videoDTO.getPageSize()),
                new QueryWrapper<Video>().eq("status", StatusEnum.ENABLE.key()).orderByDesc("create_date"));

        List<Video> records = page.getRecords();

        for (Video vo : records) {
            if (vo.getCover() != null && !vo.getCover().equals("")) {
                vo.setCover(domain2 + Trim.custom_ltrim(vo.getCover(), "group"));
            }

            if (vo.getPlayUrl() != null && !vo.getPlayUrl().equals("")) {
                vo.setPlayUrl(domain + Trim.custom_ltrim(vo.getPlayUrl(), "group"));
            }
        }
        return videoDataConverter(page, true, request);
    }

    @Override
    public List<VideoBasicInfoVO> findHottest(VideoDTO videoDTO, ServletRequest request) {
        String domain2 = domainService.getAppPicDomain();//图片封面域名
        IPage<Video> page = this.page(new Page<Video>().setCurrent(videoDTO.getPage()).setSize(videoDTO.getPageSize()),
                new QueryWrapper<Video>().eq("status", StatusEnum.ENABLE.key()).orderByDesc("play_sum"));
        List<Video> records = page.getRecords();
        for(Video video:records){
            video.setCover(domain2 + Trim.custom_ltrim(video.getCover(), "group"));
        }
        return videoDataConverter(page, true, request);
    }


    protected List<VideoBasicInfoVO> videoDataConverter(IPage<Video> page, boolean addAd, ServletRequest request) {

        String domain = domainService.getDomain(request);
        String domain2 = domainService.getAppPicDomain();//图片封面域名
        List<VideoBasicInfoVO> result = new ArrayList<>();
        if (page != null && !ListUtils.isEmpty(page.getRecords())) {

            for (Video video : page.getRecords()) {
                VideoBasicInfoVO vo = new VideoBasicInfoVO().setId(video.getId()).setCover(video.getCover())
                        .setTitle(video.getTitle()).setPlaySum(video.getPlaySum()).setTimeLen(video.getTimeLen())
                        .setCreateDate(video.getCreateDate()).setIsVip(video.getIsVip());

                if (vo.getCover() != null && !vo.getCover().equals("")) {
                    vo.setCover(domain2 + Trim.custom_ltrim(vo.getCover(), "group"));
                }
                result.add(vo);
            }

          /*  page.getRecords().forEach(video ->
                    result.add(new VideoBasicInfoVO().setId(video.getId()).setCover(video.getCover())
                            .setTitle(video.getTitle()).setPlaySum(video.getPlaySum()).setTimeLen(video.getTimeLen())
                            .setCreateDate(video.getCreateDate()))
            );*/
            if (addAd)
                adService.getAd4User(result, request);

        }

        return result;
    }


    @Override
    public List<VideoBasicInfoVO> findRandomRelevant(VideoDTO videoDTO, ServletRequest request) {


        //获取视频所有标签
        List<VideoLabelVideo> labelVideoList = videoLabelVideoService.list(new QueryWrapper<VideoLabelVideo>().eq("video_id", videoDTO.getId()));
        if (labelVideoList.size() == 0) {
            IPage<Video> pagess = this.page(new Page<Video>().setCurrent(videoDTO.getPage()).setSize(videoDTO.getPageSize()),
                    new QueryWrapper<Video>().notIn("status", 2).orderByDesc("play_count_for_week").orderByAsc("play_count_for_month"));
            return videoDataConverter(pagess, false, request);
        }
        IPage<Video> pages = null;
        if (labelVideoList != null) {
            IPage<Video> page = this.page(new Page<Video>().setCurrent(videoDTO.getPage()).setSize(videoDTO.getPageSize()),
                    new QueryWrapper<Video>().notIn("status", 2));
            //获取标签中的随机一个标签
            VideoLabelVideo videoLabelVideo = labelVideoList.get((int) (0 + Math.random() * (labelVideoList.size() - 1 - 0 + 1)));
            System.out.println(videoLabelVideo.getVideoLabelId());
            //根据标签ID查询视频
            List<VideoLabelVideo> VideoLabelVideos = videoLabelVideoService.list(new QueryWrapper<VideoLabelVideo>().eq("video_label_id", videoLabelVideo.getVideoLabelId()));
            List<Video> list = new ArrayList<>();
            for (VideoLabelVideo v : VideoLabelVideos) {
                Video video = this.getById(v.getVideoId());
                if (video.getStatus() != 2) {
                    list.add(video);
                }
            }
            //创建一个map用于去重
            Map<String, Integer> map = new HashMap();
            List<Video> list2 = new ArrayList<>();

            if (list.size() >= videoDTO.getPageSize()) {
                for (int i = 0; i < videoDTO.getPageSize(); i++) {
                    int r1 = new Random().nextInt(list.size());
                    list2.add(list.get(r1));
                    list.remove(r1);
                }
            } else {
                for (int i = 0; i < list.size(); i++) {
                    list2.add(list.get(i));
                    map.put(list.get(i).getId(), 0);
                }
                IPage<Video> page2 = this.page(new Page<Video>().setCurrent(videoDTO.getPage()).setSize(videoDTO.getPageSize()),
                        new QueryWrapper<Video>().notIn("status", 2).orderByDesc("play_count_for_week").orderByAsc("play_count_for_month"));
                List<Video> list1 = page2.getRecords();
                for (Video vvv : list1) {
                    if (map.get(vvv.getId()) != null) {

                    } else {
                        list2.add(vvv);
                        map.put(vvv.getId(), 0);
                    }
                }
            }
            //最后取前随机pagesize部
            List<Video> list3 = new ArrayList<>();
            for (int i = 0; i < videoDTO.getPageSize(); i++) {
                int r2 = new Random().nextInt(list2.size());
                list3.add(list2.get(r2));
                list2.remove(r2);
            }
            page.setRecords(list3);

            pages = page;
        } else {
            IPage<Video> page = this.page(new Page<Video>().setCurrent(videoDTO.getPage()).setSize(videoDTO.getPageSize()),
                    new QueryWrapper<Video>().notIn("status", 2).orderByDesc("play_count_for_week").orderByAsc("play_count_for_month"));
            pages = page;
        }
        return videoDataConverter(pages, false, request);
    }

    @Override
    public List<VideoBasicInfoVO> findByChannelType(VideoSO videoSO, ServletRequest request) {
        String domain = domainService.getDomain(request);
        String domain2 = domainService.getAppPicDomain();//图片封面域名
        List<VVideoChannelLabel> vvcls = vVideoChannelLabelService.findVideoByChannel(videoSO);

        List<VideoBasicInfoVO> result = new ArrayList<>();
        if (!ListUtils.isEmpty(vvcls)) {
            for (VVideoChannelLabel vvcl : vvcls) {
                VideoBasicInfoVO vo = new VideoBasicInfoVO().setId(vvcl.getVId()).setCover(vvcl.getVCover())
                        .setTitle(vvcl.getVTitle()).setPlaySum(vvcl.getVPlaySum()).setTimeLen(vvcl.getVTimeLen())
                        .setCreateDate(vvcl.getVCreateDate()).setIsVip(vvcl.getVIsVip());

                if (vo.getCover() != null && !vo.getCover().equals("")) {
                    vo.setCover(domain2 + Trim.custom_ltrim(vo.getCover(), "group"));
                }
                result.add(vo);
            }

            /*vvcls.forEach(vvcl ->
                    result.add(new VideoBasicInfoVO().setId(vvcl.getVId()).setCover(vvcl.getVCover())
                            .setTitle(vvcl.getVTitle()).setPlaySum(vvcl.getVPlaySum()).setTimeLen(vvcl.getVTimeLen())
                            .setCreateDate(vvcl.getVCreateDate()))
            );*/

            adService.getAd4User(result, request);

        }
        return result;
    }

    @Override
    public List<VideoBasicInfoVO> findByLabels(VideoSO videoSO, ServletRequest request) {
        String domain = domainService.getDomain(request);
        String domain2 = domainService.getAppPicDomain();//图片封面域名
        List<VideoBasicInfoVO> resultList = new ArrayList<>();
        List<VVideoChannelLabel> vvclros = vVideoChannelLabelService.findVideosByLabelIds(videoSO);
        if (!ListUtils.isEmpty(vvclros)) {
            for (VVideoChannelLabel ro : vvclros) {
                VideoBasicInfoVO vo = new VideoBasicInfoVO().setId(ro.getVId())
                        .setCover(ro.getVCover()).setTitle(ro.getVTitle()).setPlaySum(ro.getVPlaySum())
                        .setTimeLen(ro.getVTimeLen()).setCreateDate(ro.getVCreateDate()).setIsVip(ro.getVIsVip());

                if (vo.getCover() != null && !vo.getCover().equals("")) {
                    vo.setCover(domain2 + Trim.custom_ltrim(vo.getCover(), "group"));
                }
                resultList.add(vo);
            }

            /*  vvclros.forEach(ro -> resultList.add(new VideoBasicInfoVO().setId(ro.getVId())
                    .setCover(ro.getVCover()).setTitle(ro.getVTitle()).setPlaySum(ro.getVPlaySum())
                    .setTimeLen(ro.getVTimeLen()).setCreateDate(ro.getVCreateDate())));*/
        }

        adService.getAd4User(resultList, request);
        return resultList;
    }

    @Override
    public List<VideoBasicInfoVO> findBySubject(VideoSO videoSO, ServletRequest request) {
        String domain = domainService.getDomain(request);
        String domain2 = domainService.getAppPicDomain();//图片封面域名
        VideoSubjectDTO videoSubjectDTO = VideoSubjectDTO.builder().id(videoSO.getVideoSubjectId()).build();
        videoSubjectDTO.setPage(videoSO.getPage()).setPageSize(videoSO.getPageSize());
        List<VVideoSubject> videoBySubjectList = vVideoSubjectService.findVideoBySubject(videoSubjectDTO);

        List<VideoBasicInfoVO> result = new ArrayList<>();
        if (!ListUtils.isEmpty(videoBySubjectList)) {
            for (VVideoSubject vvs : videoBySubjectList) {
                VideoBasicInfoVO vo = new VideoBasicInfoVO().setCreateDate(vvs.getVCreateDate()).setTimeLen(vvs.getVTimeLen())
                        .setPlaySum(vvs.getVPlaySum()).setTitle(vvs.getVTitle()).setCover(vvs.getVCover()).setId(vvs.getVId()).setIsVip(vvs.getVIsVip());

                if (vo.getCover() != null && !vo.getCover().equals("")) {
                    vo.setCover(domain2 + Trim.custom_ltrim(vo.getCover(), "group"));
                }
                result.add(vo);
            }

           /* videoBySubjectList.forEach(vvs ->
                    result.add(new VideoBasicInfoVO().setCreateDate(vvs.getVCreateDate()).setTimeLen(vvs.getVTimeLen())
                            .setPlaySum(vvs.getVPlaySum()).setTitle(vvs.getVTitle()).setCover(vvs.getVCover()).setId(vvs.getVId()))
            );*/

        }
        adService.getAd4User(result, request);
        return result;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public Map<String, Object> findPlayVideo(VideoSO videoSO, Boolean allowViewing, ServletRequest request) {
        User currentUser = UserContext.getCurrentUser();
        Map<String, Object> result = new HashMap<>();

        //获取视频相关信息
        VideoLabelListVO labelsByVId = vVideoChannelLabelService.findLabelsByVId(videoSO.getVideoId());
        if (labelsByVId == null) {
            throw new GeneralException("视频信息获取失败，请稍后重试", new NullPointerException());
        }
        boolean entitleViewing = true;

        //查看观影次数 从系统参数表中获取标识 判断是否为1   为1则返回-1无限
        Object values = sysConfParamService.getValByValName(SysParamValueNameEnum.INFINITE_VALUE);
        String value = values.toString();
        if (!value.equals("1")) {
            if (allowViewing || (currentUser.getViewingCount() != -1 && currentUser.getUsedViewingCount() >= currentUser.getViewingCount())) {
                labelsByVId.setPlayUrl(null);
                entitleViewing = false;
            }
        }

        /*if(currentUser.getSaveCount() != -1 && currentUser.getUsedSaveCount() >= currentUser.getSaveCount()){
            labelsByVId.setSaveUrl(null);
        }*/

        //播放页面不返回下载路径
        labelsByVId.setSaveUrl(null);

        String domain = domainService.getDomain(request);
        String domain2 = domainService.getAppPicDomain();//图片封面域名
        if (labelsByVId.getPlayUrl() != null && !labelsByVId.getPlayUrl().equals("")) {
            labelsByVId.setPlayUrl(domain + Trim.custom_ltrim(labelsByVId.getPlayUrl(), "group"));
        }

        //视频播放路径加密
        if (labelsByVId.getPlayUrl() != null && !labelsByVId.getPlayUrl().equals("")) {
            String url = labelsByVId.getPlayUrl();

            try {
                url = AESCipher.aesEncryptString(url);
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            } catch (InvalidAlgorithmParameterException e) {
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            } catch (BadPaddingException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            labelsByVId.setPlayUrl(url);
        }

        result.put("videoInfo", labelsByVId);

        //获取广告相关信息
        result.put("playPageAd", adService.getPlayPageAd(request));

        if (entitleViewing) {
            //用户使用观影次数加一
            userService.doUpdateUser(User.builder().id(currentUser.getId()).usedViewingCount(currentUser.getUsedViewingCount() + 1).build());

            //视频播放量加一
            this.updateVideoPlaySum(videoSO.getVideoId());

            //添加用户观影记录
            userViewingRecordService.add(videoSO.getVideoId());
        }

        //调用用户行为分析接口
        UserActionAnaylzeDTO userActionAnaylzeDTO = new UserActionAnaylzeDTO();
        userActionAnaylzeDTO.setVideoId(videoSO.getVideoId());
        userActionAnaylzeDTO.setMacAddr(currentUser.getMacAddr());
        int actionType = 1;
        userActionAnalyzeService.add(userActionAnaylzeDTO, actionType);
        return result;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public String getSaveVideoUrl(VideoSO videoSO) {
        User currentUser = UserContext.getCurrentUser();
        if (currentUser.getSaveCount() != -1 && currentUser.getUsedSaveCount() >= currentUser.getSaveCount()) {
            return null;
        }

        //用户使用观影次数加一
        userService.doUpdateUser(User.builder().id(currentUser.getId()).usedSaveCount(currentUser.getUsedSaveCount() + 1).build());

        //视频下载量加一
        this.updateVideoSaveSum(videoSO.getVideoId());

        Video video = this.getOne(new QueryWrapper<Video>().eq("id", videoSO.getVideoId()).eq("status", StatusEnum.ENABLE.key()));
        //返回结果数据加密
        String saveUrl = video.getSaveUrl();

        try {
            saveUrl = AESCipher.aesEncryptString(saveUrl);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println("get download video url:" + saveUrl);

        //调用用户行为分析接口
        UserActionAnaylzeDTO userActionAnaylzeDTO = new UserActionAnaylzeDTO();
        userActionAnaylzeDTO.setVideoId(videoSO.getVideoId());
        userActionAnaylzeDTO.setMacAddr(currentUser.getMacAddr());
        userActionAnalyzeService.add(userActionAnaylzeDTO, 3);
        return saveUrl;
    }

    @Override
    public void updateVideoPlaySum(String videoId) {
        Video video = this.getById(videoId);
        if (video == null) {
            throw new ResponsiveException("视频不存在或已被删除");
        }

        Long playCountForDay = BasicUtil.getNumber(video.getPlayCountForDay());

        Long playCountForWeek = BasicUtil.getNumber(video.getPlayCountForWeek());

        Long playCountForMonth = BasicUtil.getNumber(video.getPlayCountForMonth());

        Long playSum = BasicUtil.getNumber(video.getPlaySum());

        video.setPlayCountForDay(playCountForDay + 1);
        video.setPlayCountForWeek(playCountForWeek + 1);
        video.setPlayCountForMonth(playCountForMonth + 1);
        video.setPlaySum(playSum + 1);
        this.updateById(video);
    }

    @Override
    public void updateVideoSaveSum(String videoId) {
        Video video = this.getById(videoId);
        if (video == null) {
            throw new ResponsiveException("视频不存在或已被删除");
        }
        Long saveSum = BasicUtil.getNumber(video.getSaveSum());

        video.setSaveSum(saveSum + 1);
        this.updateById(video);
    }

    @Override
    public Map<String, Object> findVipPlayVideo(VideoSO videoSO, ServletRequest request) {
        User currentUser = UserContext.getCurrentUser();

        //查看用户剩余观影
        //查看观影次数 从系统参数表中获取标识 判断是否为1   为1则返回-1无限
        Object values = sysConfParamService.getValByValName(SysParamValueNameEnum.INFINITE_VALUE);
        String value = values.toString();
        if (!value.equals("1")) {
            if (currentUser.getViewingCount() != -1 && currentUser.getUsedViewingCount() >= currentUser.getViewingCount()) {
                return null;
            }
        }

        //查询用户已使用观影次数
        String usedVipViewingKey = StringUtils.join(VideoContants.USER_USED_VIP_VIEWING_COUNT, currentUser.getId());
        Integer usedVipViewingCount = 0;
        if (cache.hasKey(usedVipViewingKey)) {
            usedVipViewingCount = Integer.valueOf(cache.get(usedVipViewingKey).toString());
        }

        //查询用户等级
        Map<String, Object> paramMap = sysConfParamService.findParamValByName(SysParamNameEnum.VIP_VIEWING_COUNT);
        Integer level = paramMap == null || paramMap.get(SysParamValueNameEnum.VIP_VIEWING_LEVEL.code()) == null ? 1
                : Integer.valueOf(paramMap.get(SysParamValueNameEnum.VIP_VIEWING_LEVEL.code()).toString());

        //判断用户剩余VIP观影次数
        if (currentUser.getUserLevel() < level) {
            //只能试看vip视频
            Integer tryViewingCount = paramMap == null || paramMap.get(SysParamValueNameEnum.VIP_TRY_VIEWING_COUNT.code()) == null ? 0
                    : Integer.valueOf(paramMap.get(SysParamValueNameEnum.VIP_TRY_VIEWING_COUNT.code()).toString());

            if (usedVipViewingCount >= tryViewingCount) {
                return null;
            }
        }

        //更新缓存
        cache.set(usedVipViewingKey, ++usedVipViewingCount, DateUtil.getSurplusSecondOfToday());

        //是否允许观影
        return this.findPlayVideo(videoSO, true, request);
    }

    @Override
    public List<VideoBasicInfoVO> findVipVideo(VideoSO videoSO, ServletRequest request) {
        String domain = domainService.getDomain(request);
        String domain2 = domainService.getAppPicDomain();//图片封面域名
        IPage<Video> page = this.page(new Page<Video>().setCurrent(videoSO.getPage()).setSize(videoSO.getPageSize()),
                new QueryWrapper<Video>().eq("status", StatusEnum.ENABLE.key()).eq("is_vip", true)
                        .orderBy(true, videoSO.getIsAsc(), videoSO.getSortName()));

        List<Video> records = page.getRecords();
        for (Video video : records) {
            if (video.getPlayUrl() != null && !video.getPlayUrl().equals("")) {
                video.setPlayUrl(domain + Trim.custom_ltrim(video.getPlayUrl(), "group"));
            }
            if (video.getCover() != null && !video.getCover().equals("")) {
                video.setCover(domain2 + Trim.custom_ltrim(video.getCover(), "group"));
            }
        }

        return videoDataConverter(page, true, request);
    }

    @Override
    public VideoBasicInfoVO getViderSimpInfo(String id) {
        Video video = this.getById(id);
        VideoBasicInfoVO vo = new VideoBasicInfoVO();
        String domain2 = domainService.getAppPicDomain();//图片封面域名
        if (video != null) {
            vo.setCover(domain2 + Trim.custom_ltrim(video.getCover(), "group"));
            vo.setTitle(video.getTitle());
        }
        return vo;
    }

    @Override
    public boolean setVideoTop(String vid,Integer type,Integer location) {
        //设置置顶
        Video video = this.getById(vid);
        if(type== TopTypeEnum.SETTOP.key()){
            List<Video> topLikes = this.list(new QueryWrapper<Video>().eq("top_like", TopLikeEnum.ISTOP.key()));
            List<Video> topPerfects = this.list(new QueryWrapper<Video>().eq("top_perfect", TopPerfectEnum.ISTOP.key()));
            if(location==TopLocationEnum.TOPPERFECT.key()&&topPerfects.size()<2){
                video.setTopPerfect(TopPerfectEnum.ISTOP.key());
            }
            if(location==TopLocationEnum.TOPLIKE.key()&&topLikes.size()<2){
                video.setTopLike(TopLikeEnum.ISTOP.key());
            }
            if(topPerfects.size()==2&&location==TopLocationEnum.TOPPERFECT.key()){
                return false;
            }
            if(topLikes.size()==2&&location==TopLocationEnum.TOPLIKE.key()){
                return false;
            }

        }
        //取消置顶
        else if(type==TopTypeEnum.NOSETTOP.key()){
            if(location==TopLocationEnum.TOPPERFECT.key()){
                video.setTopPerfect(TopPerfectEnum.NOTTOP.key());
            }else if(location==TopLocationEnum.TOPLIKE.key()){
                video.setTopLike(TopLikeEnum.NOTTOP.key());
            }
        }

        this.update(video,new UpdateWrapper<Video>().eq("id",vid));

        return true;
    }

}
