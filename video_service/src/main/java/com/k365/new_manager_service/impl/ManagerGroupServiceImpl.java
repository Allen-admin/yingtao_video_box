package com.k365.new_manager_service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.k365.new_manager_service.ManagerEmployeeService;
import com.k365.new_manager_service.ManagerGroupService;
import com.k365.video_base.mapper.ManagerGroupMapper;
import com.k365.video_base.model.dto.ManagerEmployeeDTO;
import com.k365.video_base.model.dto.ManagerGroupDTO;
import com.k365.video_base.model.po.ManagerEmployee;
import com.k365.video_base.model.po.ManagerGroup;
import com.k365.video_base.model.vo.ManagerGroupVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class ManagerGroupServiceImpl extends ServiceImpl<ManagerGroupMapper, ManagerGroup> implements ManagerGroupService {

    @Autowired
    @Lazy(true)
    private ManagerEmployeeService managerEmployeeService;

    @Override
    public String add(ManagerGroupDTO managerGroupDTO) {
        if(this.getOne(new QueryWrapper<ManagerGroup>().
                eq("name", managerGroupDTO.getName()))!=null){
            return "组名已存在，请重新输入";
        }

        ManagerGroup managerGroup = ManagerGroup.builder().build();
        BeanUtils.copyProperties(managerGroupDTO, managerGroup);

        managerGroup.setCrttime(new Date());
        managerGroup.setUpttime(new Date());

        boolean save = this.save(managerGroup);
        if (save) {
            return "新增组成功";
        } else {
            return "新增组失败";
        }
    }

    @Override
    public String update(ManagerGroupDTO managerGroupDTO) {

        ManagerGroup managerGroup = this.getById(managerGroupDTO.getId());
        if (managerGroup == null)
            throw new RuntimeException("组不存在或已被删除");


        if(this.getOne(new QueryWrapper<ManagerGroup>().
                eq("name", managerGroupDTO.getName())).getName().equals(managerGroupDTO.getName())){
            return "该组名已存在，请重新输入！";
        }

        ManagerGroup managerGroup1 = ManagerGroup.builder().id(managerGroupDTO.getId()).
                name(managerGroupDTO.getName()).build();
        managerGroup1.setUpttime(new Date());
        boolean result = this.updateById(managerGroup1);
        if(result){
            return "修改成功!";
        }else {
            return "修改失败!";
        }

    }

    @Override
    public void removeByGroupId(Integer id) {
        ManagerGroup managerGroup = this.getById(id);
        if (managerGroup == null) {
            throw new RuntimeException("组不存在或已被删除");
        }

        boolean removeSuccess = this.removeById(id);
        if (removeSuccess) {
            ManagerEmployeeDTO managerEmployeeDTO = ManagerEmployeeDTO.builder().build();
            managerEmployeeDTO.setGroupId(id);
            managerEmployeeDTO.setPage(1);
            managerEmployeeDTO.setPageSize(Integer.MAX_VALUE);

            List<ManagerEmployee> list = managerEmployeeService.getListByGroupId(id);
            if(list!=null){

                for (ManagerEmployee po : list) {
                    managerEmployeeService.removeByEmployeeId(po.getId());
                }
            }

            log.info("删除组成功，用户id：" + managerGroup.getId());
        } else {
            log.info("删除组失败！");
        }
    }


    @Override
    public List<ManagerGroupVO> findAll() {
        List<ManagerGroup> list = this.list(new QueryWrapper<ManagerGroup>());
        List<ManagerGroupVO> newList=new ArrayList<>();
        for (ManagerGroup managerGroup:list){
            ManagerGroupVO vo=ManagerGroupVO.builder().build();
            vo.setCrttime(managerGroup.getCrttime());
            vo.setId(managerGroup.getId());
            vo.setUpttime(managerGroup.getUpttime());
            vo.setName(managerGroup.getName());
            //获取组长名字
            String leaderName = managerEmployeeService.getOneByGroupId(managerGroup.getId());
            vo.setLeaderName(leaderName);
            newList.add(vo);
        }

        return newList;
    }

    @Override
    public String getOneById(Integer groupId) {
        ManagerGroup managerGroup = this.getOne(new QueryWrapper<ManagerGroup>()
                .eq("id", groupId));
        if(managerGroup==null){
            return "数据查询出错";
        }else{
            return managerGroup.getName();
        }
    }

}
