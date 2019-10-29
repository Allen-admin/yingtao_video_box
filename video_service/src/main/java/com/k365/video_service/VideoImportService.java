package com.k365.video_service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.k365.video_base.model.dto.VideoInfoDTO;
import com.k365.video_base.model.po.VideoImport;
import com.k365.video_base.model.vo.BaseListVO;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Gavin
 * @date 2019/8/26 10:47
 * @description：
 */
public interface VideoImportService extends IService<VideoImport> {

    /**
     * 视频导入
     */
    String importVideo(VideoInfoDTO videoInfoDTO);

    /**
     * 分页查询视频导入列表
     */
    BaseListVO<VideoImport> findAll(VideoInfoDTO videoInfoDTO);

    /**
     * Excel 文档导入视频数据
     */
    <T> VideoImport importExcel(MultipartFile file, Class<T> type);

    /**
     * 视频导入
     */
    <T> Boolean importVideo(T param);
}
