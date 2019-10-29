package com.k365.video_base.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.k365.video_base.model.dto.VideoSubjectDTO;
import com.k365.video_base.model.po.VVideoSubject;
import com.k365.video_base.model.ro.VVideoSubjectRO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * VIEW Mapper 接口
 * </p>
 *
 * @author Gavin
 * @since 2019-08-24
 */
@Mapper
public interface VVideoSubjectMapper extends BaseMapper<VVideoSubject> {

    List<VVideoSubjectRO> findVideoSubject(VideoSubjectDTO videoSubjectDTO);

}
