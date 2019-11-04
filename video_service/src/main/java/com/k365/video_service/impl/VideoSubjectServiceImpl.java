package com.k365.video_service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.k365.manager_service.AdService;
import com.k365.manager_service.DomainService;
import com.k365.video_base.common.SubjectTypeEnum;
import com.k365.video_base.mapper.VideoSubjectMapper;
import com.k365.video_base.model.dto.VideoSubjectDTO;
import com.k365.video_base.model.po.VideoSubject;
import com.k365.video_base.model.ro.VVideoSubjectRO;
import com.k365.video_base.model.vo.AdVO;
import com.k365.video_base.model.vo.BaseListVO;
import com.k365.video_common.exception.ResponsiveException;
import com.k365.video_common.util.Trim;
import com.k365.video_service.VVideoSubjectService;
import com.k365.video_service.VideoSubjectService;
import com.k365.video_service.VideoSubjectVideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.ListUtils;

import javax.servlet.ServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Gavin
 * @since 2019-08-14
 */
@Service
public class VideoSubjectServiceImpl extends ServiceImpl<VideoSubjectMapper, VideoSubject> implements VideoSubjectService {

    @Autowired
    private VideoSubjectVideoService videoSubjectVideoService;

    @Autowired
    private VVideoSubjectService vVideoSubjectService;

    @Autowired
    private AdService adService;

    @Autowired
    @Lazy
    private DomainService domainService;

    @Override
    public void add(VideoSubject videoSubject) {
        VideoSubject videoSubject2 = this.getOne(new QueryWrapper<VideoSubject>().eq("name", videoSubject.getName()));
        if (videoSubject2 != null)
            throw new ResponsiveException("同名视频专题已存在");

        this.save(videoSubject);

    }

    @Override
    public void update(VideoSubject videoSubject) {
        VideoSubject videoSubject2 = this.getById(videoSubject.getId());
        if (videoSubject2 == null)
            throw new ResponsiveException("视频标题不存在或已被删除");

        this.updateById(videoSubject);

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public void remove(Integer id) {
        boolean removeSuccess = this.removeById(id);
        if (removeSuccess) {
            videoSubjectVideoService.removeByVideoSubjectId(id);
        }
    }

    @Override
    public List<VideoSubject> findAll(VideoSubjectDTO videoSubjectDTO) {
        List<VVideoSubjectRO> ros = vVideoSubjectService.findVideoSubjects(videoSubjectDTO);
        List<VideoSubject> result = new ArrayList<>();
        for (VVideoSubjectRO ro : ros) {
            if (ro.getVsId() != null) {
                result.add(VideoSubject.builder().content(ro.getVsContent()).cover(ro.getVsCover()).videoTotal(ro.getVideoTotal())
                        .id(Integer.valueOf(String.valueOf(ro.getVsId()))).name(ro.getVsName())
                        .icon(ro.getVsIcon()).status(ro.getVsStatus()).sort(ro.getVsSort()).build());
            }
        }
        return result;
    }

    @Override
    public BaseListVO<VideoSubject> findPage(VideoSubjectDTO videoSubjectDTO) {

        IPage<VideoSubject> page=this.page(new Page<VideoSubject>().setSize(videoSubjectDTO.getPageSize()).
                        setCurrent(videoSubjectDTO.getPage()),
                new QueryWrapper<VideoSubject>().orderByAsc("sort"));






        return new BaseListVO<VideoSubject>().setTotal(page.getTotal()).setList(page.getRecords());

    }

    @Override
    public List<VVideoSubjectRO> pageList(VideoSubjectDTO videoSubjectDTO,ServletRequest request) {

        if(videoSubjectDTO.getSubjectType()==null){
            videoSubjectDTO.setSubjectType(SubjectTypeEnum.LEVEL1.key());
        }
        String domain2 = domainService.getAppPicDomain();//图片封面域名
        List<VVideoSubjectRO> ros = vVideoSubjectService.findVideoSubjects(videoSubjectDTO);
        for (VVideoSubjectRO v:ros){
            v.setVsCover(domain2 + Trim.custom_ltrim(v.getVsCover(), "group"));
            if(v.getVsIcon()!=null){
                v.setVsIcon(domain2+Trim.custom_ltrim(v.getVsIcon(), "group"));
            }
        }
        AdVO adVO = adService.getOneVAdBy4User(request);
        if (adVO != null&&videoSubjectDTO.getSubjectType()!=SubjectTypeEnum.LEVEL2.key()) {
            ros = ListUtils.isEmpty(ros) ? new ArrayList<>() : ros;
            ros.add(new VVideoSubjectRO().setVsCover(domain2 + Trim.custom_ltrim(adVO.getCover(), "group")).setVsId(adVO.getId()).setVsName(adVO.getTitle())
                    .setAdUrl(adVO.getDetailsUrl()).setIsAd(true));
        }
        return ros;
    }


}
