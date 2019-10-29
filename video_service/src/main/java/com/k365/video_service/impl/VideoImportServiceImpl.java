package com.k365.video_service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.k365.video_base.mapper.VideoImportMapper;
import com.k365.video_base.model.dto.VideoDTO;
import com.k365.video_base.model.dto.VideoExcelImportDTO;
import com.k365.video_base.model.dto.VideoInfoDTO;
import com.k365.video_base.model.po.*;
import com.k365.video_base.model.vo.BaseListVO;
import com.k365.video_common.exception.GeneralException;
import com.k365.video_common.exception.ResponsiveException;
import com.k365.video_common.util.ExcelUtil;
import com.k365.video_common.util.PinYinUtil;
import com.k365.video_common.util.TranslateUtil;
import com.k365.video_service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.ListUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

/**
 * @author Gavin
 * @date 2019/8/26 10:47
 * @description：
 */
@Slf4j
@Service
public class VideoImportServiceImpl extends ServiceImpl<VideoImportMapper, VideoImport> implements VideoImportService {

    @Autowired
    private VideoActorService videoActorService;

    @Autowired
    private ActorService actorService;

    @Autowired
    private VideoLabelService videoLabelService;

    @Autowired
    private VideoChannelService videoChannelService;

    @Autowired
    private VideoLabelVideoService videoLabelVideoService;

    @Autowired
    private VideoSubjectService videoSubjectService;

    @Autowired
    private VideoService videoService;

    @Override
    public <T> VideoImport importExcel(MultipartFile file, Class<T> type) {
        if (file == null)
            throw new ResponsiveException("未获取到您所上传的文件");

        String fileName = file.getOriginalFilename();
        Workbook workbook = null;
        int importOkCount = 0;
        int importCount = 0;
        Set<String> failTitles = new HashSet<>();
        try {
            workbook = ExcelUtil.getWorkbook(fileName, file.getInputStream());
            if (workbook == null) {
                throw new ResponsiveException("文件格式不对！");
            }
            // 获取 EXCEL Sheet 页
            Sheet sheet = workbook.getSheetAt(0);
            importCount = sheet.getLastRowNum();
            for (int ri = sheet.getFirstRowNum() + 1; ri <= sheet.getLastRowNum(); ri++) {
                Row row = sheet.getRow(ri);
                T object = type.newInstance();
                Field[] fields = object.getClass().getDeclaredFields();
                Field.setAccessible(fields, true);
                for (int ci = row.getFirstCellNum(); ci < row.getLastCellNum(); ci++) {
                    Cell cell = row.getCell(ci);
                    Object value = ExcelUtil.getCellValue(cell);//fields[ci]
                    fields[ci].set(object, value);
                }
                Field.setAccessible(fields, false);
                //视频导入
                Boolean importVideo = this.importVideo(object);
                if (BooleanUtils.isTrue(importVideo)) {
                    importOkCount++;
                } else if (object instanceof VideoExcelImportDTO) {
                    VideoExcelImportDTO dto = (VideoExcelImportDTO) object;
                    failTitles.add(dto.getVideoTitle());
                }

            }

        } catch (Exception e) {
            throw new GeneralException("Excel导入视频失败！", e);
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (IOException e) {
                    log.error("Excel导入视频，释放资源失败! e:{}", e.getMessage());
                }
            }
        }

        VideoImport videoImport = VideoImport.builder().createDate(new Date()).failTitles(JSON.toJSONString(failTitles))
                .fileName(fileName).importCount(importCount).importOkCount(importOkCount).build();
        this.save(videoImport);
        return videoImport;
    }


    @Override
    public <T> Boolean importVideo(T param) {
        if (param instanceof VideoExcelImportDTO) {
            VideoExcelImportDTO videoInfo = (VideoExcelImportDTO) param;
            if(StringUtils.isBlank(videoInfo.getVideoTitle())){
                return false;
            }
            //播放时长
            Integer timeLen = 0;
            if (StringUtils.isNotBlank(videoInfo.getVideoTimeLen())) {
                String[] split = videoInfo.getVideoTimeLen().split(":");
                timeLen = Integer.valueOf(split[0]) * 3600 + Integer.valueOf(split[1]) * 60 + Integer.valueOf(split[2]);
            }

            VideoDTO videoDTO = VideoDTO.builder().code(videoInfo.getVideoCode()).cover(videoInfo.getVideoCover())
                    .playUrl(videoInfo.getVideoPlayUrl()).saveUrl(videoInfo.getVideoSaveUrl()).timeLen(timeLen)
                    .title(videoInfo.getVideoTitle()).build();
            //处理VideoLabelIds
            if (StringUtils.isNotBlank(videoInfo.getLabel())) {
                String[] labelNames = videoInfo.getLabel().split(",");
                List<Integer> labelIds =
                        JSONArray.parseArray(JSON.toJSONString(videoLabelService.listObjs(new QueryWrapper<VideoLabel>().in("name", labelNames))), Integer.class);
                videoDTO.setVideoLabelIds(labelIds.toArray(new Integer[labelIds.size()]));
            }
            //处理VideoChannelIds
            if (StringUtils.isNotBlank(videoInfo.getChannel())) {
                String[] channelNames = videoInfo.getChannel().split(",");
                List<Integer> channelIds =
                        JSONArray.parseArray(JSON.toJSONString(videoChannelService.listObjs(new QueryWrapper<VideoChannel>().in("name", channelNames))), Integer.class);
                videoDTO.setVideoChannelIds(channelIds.toArray(new Integer[channelIds.size()]));
            }
            //处理VideoActorIds
            if (StringUtils.isNotBlank(videoInfo.getActor())) {
                String[] actorNames = videoInfo.getActor().split(",");
                List<Integer> actorIds =
                        JSONArray.parseArray(JSON.toJSONString(actorService.listObjs(new QueryWrapper<Actor>().in("name", actorNames))), Integer.class);
                videoDTO.setVideoActorIds(actorIds.toArray(new Integer[actorIds.size()]));
            }
            //处理VideoSubjectIds
            if (StringUtils.isNotBlank(videoInfo.getSubject())) {
                String[] subjectNames = videoInfo.getSubject().split(",");
                List<Integer> subjectIds =
                        JSONArray.parseArray(JSON.toJSONString(videoSubjectService.listObjs(new QueryWrapper<VideoSubject>().in("name", subjectNames))), Integer.class);
                videoDTO.setVideoActorIds(subjectIds.toArray(new Integer[subjectIds.size()]));
            }

            videoService.add(videoDTO);
            return true;

        }
        return false;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public String importVideo(VideoInfoDTO videoInfoDTO) {
        List<HashMap> actorInfoOrigin =
                JSON.parseArray(JSON.toJSONString(videoInfoDTO.getActorInfo(), SerializerFeature.WriteMapNullValue), HashMap.class);

        List<Actor> actorInfoTarget = new ArrayList<>();
        List<Actor> oldActor = new ArrayList<>();

        //从视频信息中获取演员信息并将日文翻译为中文
        getActorByVideoInfo(actorInfoOrigin, actorInfoTarget, oldActor);

        //保存视频和演员数据
        String videoId = saveActorAndVideo(videoInfoDTO, actorInfoTarget, oldActor);

        List<String> labelList = JSON.parseArray(
                JSON.toJSONString(videoInfoDTO.getLabel(), SerializerFeature.WriteMapNullValue), String.class);
        saveVideoLabel(videoId, labelList);
        return videoId;
    }

    @Override
    public BaseListVO<VideoImport> findAll(VideoInfoDTO videoInfoDTO) {
        QueryWrapper<VideoImport> queryWrapper = new QueryWrapper<VideoImport>().orderByDesc("create_date");
        if(StringUtils.isNotBlank(videoInfoDTO.getBeginTime()) && StringUtils.isNotBlank(videoInfoDTO.getEndTime())){
            queryWrapper.between("create_date",new Date(Long.valueOf(videoInfoDTO.getBeginTime())),new Date(Long.valueOf(videoInfoDTO.getEndTime())));
        }
        IPage<VideoImport> page = this.page(new Page<VideoImport>().setCurrent(videoInfoDTO.getPage()).setSize(videoInfoDTO.getPageSize()),queryWrapper);

        return new BaseListVO<VideoImport>().setTotal(page.getTotal()).setList(page.getRecords());
    }

    /**
     * 从视频信息中获取演员数据
     */
    private void getActorByVideoInfo(List<HashMap> origin, List<Actor> target, List<Actor> oldActor) {
        //翻译女优姓名为中文
        if (!ListUtils.isEmpty(origin)) {
            origin.forEach(actorInfo -> {
                String actorName = String.valueOf(actorInfo.get("actor_name"));
                try {
                    actorName = Optional.ofNullable(TranslateUtil.jaToZh_cn(actorName)).orElse("匿名");
                } catch (Exception e) {
                    log.error("日文翻译中文失败,演员【{}】,e={}", actorName, e);
                }

                String acronym = "匿名".equals(actorName) ? "" :
                        PinYinUtil.getPinYinHeadChar(actorName.substring(0, 1)).toUpperCase();

                Actor findActor = actorService.getOne(new QueryWrapper<Actor>().eq("name", actorName));
                if (findActor == null) {
                    Actor actor = Actor.builder().actorIcon((String) actorInfo.get("actor_photo"))
                            .name(actorName).acronym(acronym).build();

                    target.add(actor);
                } else {
                    oldActor.add(findActor);
                }

            });
        }
    }

    /**
     * 保存视频和批量保存演员数据
     */
    private String saveActorAndVideo(VideoInfoDTO videoInfoDTO, List<Actor> target, List<Actor> oldActor) {
        try {
            videoInfoDTO.setTitle(Optional.ofNullable(TranslateUtil.jaToZh_cn(videoInfoDTO.getTitle())).orElse("一部神秘的视频"));
            videoInfoDTO.setSynopsis(Optional.ofNullable(TranslateUtil.jaToZh_cn(videoInfoDTO.getSynopsis())).orElse("抱歉，该视频未获取到相关简介"));
            videoInfoDTO.setDirector(Optional.ofNullable(TranslateUtil.jaToZh_cn(videoInfoDTO.getDirector())).orElse("匿名"));
        } catch (Exception e) {
            log.error("日文翻译中文失败,视频标题【{}】,e={}", videoInfoDTO.getTitle(), e);
        }

        Video video = Video.builder().build();
        BeanUtils.copyProperties(videoInfoDTO, video);
        video.setCreateDate(new Date());

        //保存视频数据
        boolean videoSaved = videoService.save(video);
        if (videoSaved) {
            List<VideoActor> videoActors = new ArrayList<>();
            if (!ListUtils.isEmpty(target)) {
                //保存演员数据
                boolean actorSaved = actorService.saveBatch(target);
                if (actorSaved) {
                    //添加视频演员关系数据
                    for (Actor actor : target) {
                        videoActors.add(VideoActor.builder().actorId(actor.getId()).videoId(video.getId()).build());
                    }
                }
            }

            if (!ListUtils.isEmpty(oldActor)) {
                for (Actor actor : oldActor) {
                    videoActors.add(VideoActor.builder().actorId(actor.getId()).videoId(video.getId()).build());
                }
            }

            videoActorService.saveBatch(videoActors);
        }

        return video.getId();
    }

    /**
     * 保存视频标签数据
     */
    private void saveVideoLabel(String videoId, List<String> labelList) {
        if (StringUtils.isNotBlank(videoId) && !ListUtils.isEmpty(labelList)) {

            List<VideoLabel> labels =
                    videoLabelService.list(new QueryWrapper<VideoLabel>().in("name", labelList));


            Map<String, Integer> labelIdMap = new HashMap<>();
            if (!ListUtils.isEmpty(labels)) {
                //整理数据 标签名和id一一对应
                labels.forEach(label ->
                        labelIdMap.put(label.getName(), label.getId())
                );

                List<VideoLabel> labelPos = new ArrayList<>();
                List<Integer> oldLabelIds = new ArrayList<>();
                labelList.forEach(labelName -> {
                    Integer id = labelIdMap.get(labelName);
                    if (id == null) {
                        labelPos.add(VideoLabel.builder().name(labelName).build());
                    } else {
                        oldLabelIds.add(id);
                    }
                });

                List<VideoLabelVideo> videoLabelVideos = new ArrayList<>();
                //批量保存标签数据
                if (!ListUtils.isEmpty(labelPos)) {
                    boolean videoLabelSaved = videoLabelService.saveBatch(labelPos);
                    if (videoLabelSaved) {
                        for (VideoLabel videoLabel : labelPos) {
                            videoLabelVideos.add(VideoLabelVideo.builder().videoId(videoId).videoLabelId(videoLabel.getId()).build());
                        }
                    }
                }

                if (!ListUtils.isEmpty(oldLabelIds)) {
                    oldLabelIds.forEach(id -> videoLabelVideos.add(VideoLabelVideo.builder().videoId(videoId).videoLabelId(id).build()));
                }

                //批量保存视频-标签关系数据
                videoLabelVideoService.saveBatch(videoLabelVideos);

            }
        }
    }
}
