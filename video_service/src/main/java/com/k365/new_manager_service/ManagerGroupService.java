package com.k365.new_manager_service;

import com.k365.video_base.model.dto.ManagerGroupDTO;
import com.k365.video_base.model.vo.ManagerGroupVO;

import java.util.List;

public interface ManagerGroupService {

    /**
     *
     * 新增组
     * */
    String add(ManagerGroupDTO managerGroupDTO);

    /**
     *
     * 修改组
     * */
    String update(ManagerGroupDTO managerGroupDTO);

    /**
     *
     * 删除组
     * */
    void removeByGroupId(Integer id);

    /**
     * 获取所有组
     */
     List<ManagerGroupVO> findAll();

    /**
     *
     * 通过id查询一条数据
     * */
    String getOneById(Integer groupId);
}
