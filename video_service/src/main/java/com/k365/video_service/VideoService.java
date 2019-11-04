package com.k365.video_service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.k365.video_base.model.dto.VideoDTO;
import com.k365.video_base.model.po.Video;
import com.k365.video_base.model.so.VideoSO;
import com.k365.video_base.model.vo.BaseListVO;
import com.k365.video_base.model.vo.VideoBasicInfoVO;
import com.k365.video_base.model.vo.VideoListVO;
import com.k365.video_base.model.vo.VideoVO;
import com.k365.video_common.constant.StatusEnum;

import javax.servlet.ServletRequest;
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
public interface VideoService extends IService<Video> {

    /**
     * 新增视频信息
     */
    void add(VideoDTO videoDTO);


    /**
     * 根据id查询视频
     */
    VideoListVO findById(String vId);

    /**
     * 修改视频信息
     */
    void update(VideoDTO videoDTO);

    /**
     * 删除视频信息
     */
    void removeByVideoId(String id);

    /**
     * 全文检索视频标题和车牌号
     */
    List<VideoBasicInfoVO> searchByKeyword(VideoSO videoSO,ServletRequest request);

    /**
     * 更改视频状态
     */
    void updateVideoStatusById(Integer id, StatusEnum statusEnum);

    /**
     * 视频列表查询
     */
    BaseListVO<VideoVO> getByPage(VideoDTO videoDTO,ServletRequest request);

    /**
     * 根据类型筛选
     */
    BaseListVO<VideoVO> searchByVideoType(VideoSO videoSO,ServletRequest request);

    /**
     * 分页查询智能推荐
     */
    List<VideoBasicInfoVO> findULikes(VideoDTO videoDTO,ServletRequest request);

    /**
     * 查询猜编辑精选（播放量最高6部视频）
     */
    List<VideoBasicInfoVO> findFeatured(VideoDTO videoDTO,ServletRequest request);

    /**
     * 分页查询最新视频
     */
    List<VideoBasicInfoVO> findLatest(VideoDTO videoDTO,ServletRequest request);

    /**
     * 分页查询最热视频
     */
    List<VideoBasicInfoVO> findHottest(VideoDTO videoDTO,ServletRequest request);

    /**
     * 随机获取6部相关视频
     */
    List<VideoBasicInfoVO> findRandomRelevant(VideoDTO videoDTO,ServletRequest request);

    /**
     * 根据频道类型分页查询
     */
    List<VideoBasicInfoVO> findByChannelType(VideoSO videoSO,ServletRequest request);

    /**
     * 根据标签ids筛选视频
     */
    List<VideoBasicInfoVO> findByLabels(VideoSO videoSO,ServletRequest request);

    /**
     * 根据主题查询视频
     */
    List<VideoBasicInfoVO> findBySubject(VideoSO videoSO,ServletRequest request);

    /**
     * 播放视频
     */
    Map<String,Object> findPlayVideo(VideoSO videoSO,Boolean allowViewing,ServletRequest request);

    /**
     * 请求下载视频路径
     */
    String getSaveVideoUrl(VideoSO videoSO);

    /**
     * 修改视频播放量
     */
    void updateVideoPlaySum(String videoId);

    /**
     * 修改视频下载量
     */
    void updateVideoSaveSum(String videoId);

    /**
     * 播放VIP视频
     */
    Map<String,Object> findVipPlayVideo(VideoSO videoSO,ServletRequest request);

    /**
     * 获取VIP视频列表
     */
    List<VideoBasicInfoVO> findVipVideo(VideoSO videoSO,ServletRequest request);


    /**
     *
     * 查询视频相关简略信息
     * */
    VideoBasicInfoVO getViderSimpInfo(String id);
}


