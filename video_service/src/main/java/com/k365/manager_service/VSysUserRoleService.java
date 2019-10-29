package com.k365.manager_service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.k365.video_base.model.dto.SplitPageDTO;
import com.k365.video_base.model.dto.SysUserDTO;
import com.k365.video_base.model.po.VSysUserRole;
import com.k365.video_base.model.vo.BaseListVO;
import com.k365.video_base.model.vo.SysUserVO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * VIEW 服务类
 * </p>
 *
 * @author Gavin
 * @since 2019-07-17
 */
public interface VSysUserRoleService extends IService<VSysUserRole> {

    /**
     * 分页查询系统用户
     */
    BaseListVO<SysUserVO> findPage(SplitPageDTO dto);

    /**
     *根据id查询系统用户
     */
    List<VSysUserRole> findByUserId(String uid);

}
