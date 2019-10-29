package com.k365.new_manager_service;

import com.k365.video_base.model.dto.ManagerEmployeeDTO;
import com.k365.video_base.model.po.ManagerEmployee;
import com.k365.video_base.model.vo.BaseListVO;
import com.k365.video_base.model.vo.ManagerEmployeeVO;
import com.k365.video_base.model.vo.UserNewVO;

import java.util.List;

public interface ManagerEmployeeService {

    /**
     * 员工登录操作
     */
    ManagerEmployee signIn(ManagerEmployeeDTO managerEmployeeDTO);

    /**
     *
     * 查询组长list
     * */
    BaseListVO<ManagerEmployeeVO> getLevel1List(ManagerEmployeeDTO managerEmployeeDTO);

    /**
     *
     * 查询普通员工list
     * */
    BaseListVO<ManagerEmployeeVO> getLevel2List(ManagerEmployeeDTO managerEmployeeDTO);

    /**
     *
     * 通过id查询员工list
     * */
    List<ManagerEmployee> getListByGroupId(Integer id);

    /**
     *
     * 新增员工
     * */
    String add(ManagerEmployeeDTO managerEmployeeDTO);

    /**
     *
     * 修改员工信息
     * */
    void update(ManagerEmployeeDTO managerEmployeeDTO);

    /**
     *
     * 删除员工信息
     * */
    void removeByEmployeeId(Integer id);

    /**
     *
     * 查询员工个人基础信息
     * */
    ManagerEmployeeVO getPersonalInfo(Integer id);

    /**
     *
     * 查询员工个人推广信息
     * */
    BaseListVO<UserNewVO> getPersonalCommonend(ManagerEmployeeDTO managerEmployeeDTO);

    /**
     *
     * 通过groupid查询员工姓名
     * */
    String getOneByGroupId(Integer id);

    /**
     *
     * 通过所有用户列表
     * */
    BaseListVO<ManagerEmployeeVO> getAllUser(ManagerEmployeeDTO managerEmployeeDTO);

    /**
     *
     * 新增管理员
     * */
    String addAdmin(ManagerEmployeeDTO managerEmployeeDTO);


    /**
     *
     * 通过mac地址查询用户
     * */
    ManagerEmployee getByMacAddr(String macAddr);


    /**
     *
     * 更新
     * */
    void updateByObj(ManagerEmployee managerEmployee);
}
