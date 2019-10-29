package com.k365.video_service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.k365.video_base.mapper.VideoLabelMapper;
import com.k365.video_base.model.dto.VideoLabelDTO;
import com.k365.video_base.model.po.*;
import com.k365.video_base.model.vo.BaseListVO;
import com.k365.video_base.model.vo.VideoLabelTypeVO;
import com.k365.video_base.model.vo.VideoLabelVO;
import com.k365.video_common.constant.StatusEnum;
import com.k365.video_common.exception.ResponsiveException;
import com.k365.video_service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.ListUtils;

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
public class VideoLabelServiceImpl extends ServiceImpl<VideoLabelMapper, VideoLabel> implements VideoLabelService {

    @Autowired
    private VideoLabelTypeService videoLabelTypeService;

    @Autowired
    private VideoLabelVideoService videoLabelVideoService;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public void add(VideoLabelDTO videoLabelDTO) {

        VideoLabel videoLabel = this.getOne(new QueryWrapper<VideoLabel>().eq("name", videoLabelDTO.getName()));
        if (videoLabel != null)
            throw new ResponsiveException("同名视频标签已存在");

        VideoLabelType labelType = videoLabelTypeService.getById(videoLabelDTO.getVideoLabelTypeId());
        if (labelType == null)
            throw new ResponsiveException("标签类型不存在或已被删除");

        VideoLabel videoLabel2 = VideoLabel.builder().name(videoLabelDTO.getName()).status(StatusEnum.ENABLE.key())
                .sort(videoLabelDTO.getSort()).typeId(labelType.getId()).typeSort(labelType.getSort()).typeName(labelType.getName()).build();

        this.save(videoLabel2);

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public void update(VideoLabelDTO videoLabelDTO) {
        VideoLabelType labelType = videoLabelTypeService.getById(videoLabelDTO.getVideoLabelTypeId());
        if (labelType == null)
            throw new ResponsiveException("标签类型不存在或已被删除");

        VideoLabel label = this.getById(videoLabelDTO.getId());
        if (label == null)
            throw new ResponsiveException("标签不存在或已被删除");

        VideoLabel videoLabel = VideoLabel.builder().id(label.getId()).name(videoLabelDTO.getName())
                .typeId(labelType.getId()).typeName(labelType.getName()).typeSort(labelType.getSort()).sort(videoLabelDTO.getSort()).build();

        this.updateById(videoLabel);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public void remove(Integer id) {
        boolean removeSuccess = this.removeById(id);
        if (removeSuccess) {
            videoLabelVideoService.removeByVideoLabelId(id);
        }
    }

    @Override
    public  BaseListVO<VideoLabel> search(VideoLabelDTO videoLabelDTO){

        IPage<VideoLabel> page=this.page(new Page<VideoLabel>().setSize(videoLabelDTO.getPageSize()).setCurrent(videoLabelDTO.getPage()),
                new QueryWrapper<VideoLabel>().eq("status",StatusEnum.ENABLE.key()).
                        like("name",videoLabelDTO.getName()).orderByAsc("sort"));

       return new BaseListVO<VideoLabel>().setTotal(page.getTotal()).setList(page.getRecords());
    }

    @Override
    public List<VideoLabel> findAll() {
        return  this.list(new QueryWrapper<VideoLabel>()
                .eq("status", StatusEnum.ENABLE.key()).orderByAsc("sort"));
    }

    @Override
    public BaseListVO<VideoLabel> findPage(VideoLabelDTO videoLabelDTO){

        IPage<VideoLabel> page=this.page(new Page<VideoLabel>().setSize(videoLabelDTO.getPageSize()).setCurrent(videoLabelDTO.getPage()),
                new QueryWrapper<VideoLabel>().eq("status",StatusEnum.ENABLE.key()).orderByAsc("sort"));

        return  new BaseListVO<VideoLabel>().setTotal(page.getTotal()).setList(page.getRecords());

    }


    @Override
    public Collection<VideoLabelTypeVO> findLabelAndType() {
        List<VideoLabel> list = this.list(new QueryWrapper<VideoLabel>().eq("status",StatusEnum.ENABLE.key())
                .orderByAsc("type_sort").orderByAsc("sort"));

        Map<Integer,VideoLabelTypeVO> map = new HashMap<>();
        if(!ListUtils.isEmpty(list)){
            Set<Integer> vlIds = new HashSet<>();
            list.forEach(vl -> {
                if(vl.getId() != null){
                    if(map.get(vl.getTypeId()) == null){
                        map.put(vl.getTypeId(),VideoLabelTypeVO.builder().vltId(vl.getTypeId()).vltName(vl.getTypeName())
                                .vlList(new ArrayList<>()).build());

                    }
                    if(vl.getId() != null && !vlIds.contains(vl.getId())){
                        map.get(vl.getTypeId()).getVlList().add(VideoLabelVO.builder().vlId(vl.getId())
                                .vlName(vl.getName()).build());

                        vlIds.add(vl.getId());
                    }
                }

            });
        }

        return map.values();

    }


}
