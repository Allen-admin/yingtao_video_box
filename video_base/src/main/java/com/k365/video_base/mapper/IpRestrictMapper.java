package com.k365.video_base.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.k365.video_base.model.po.IpRestrict;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * ip黑白名单限制表 Mapper 接口
 * </p>
 *
 * @author Gavin
 * @since 2019-09-11
 */
@Mapper
public interface IpRestrictMapper extends BaseMapper<IpRestrict> {

}
