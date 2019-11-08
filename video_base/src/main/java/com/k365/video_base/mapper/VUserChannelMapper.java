package com.k365.video_base.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.k365.video_base.model.po.VUserChannel;
import com.k365.video_base.model.so.VUserChannelSO;
import com.k365.video_base.model.vo.VUserChannelVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * VIEW Mapper 接口
 * </p>
 *
 * @author Allen
 * @since 2019-11-6
 */
@Mapper
public interface VUserChannelMapper  extends BaseMapper<VUserChannel> {

    /**
     * 查询渠道和数量
     * @param
     * @return
     */
    List<VUserChannelVO> find(VUserChannelSO vUserChannelSO);

    /**
     * 查询用户和渠道对应的总条数
      * @param
     * @return
     */
    List<VUserChannelVO> count(VUserChannelSO vUserChannelSO);


    /**
     * 檢索用戶信息
     * @param
     * @return
     */
    List<VUserChannelVO> search(VUserChannelSO vUserChannelSO);


    /**
     *根据时间段查询
     * @param
     * @return
     */
    List<VUserChannelVO> findPage(VUserChannelSO vUserChannelSO);

    /**
     *根据渠道名,编号，时间搜索
     * @param
     * @return
     */
    List<VUserChannelVO> list(VUserChannelSO vUserChannelSO);

}
