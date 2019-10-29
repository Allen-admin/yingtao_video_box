package com.k365.manager_service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.k365.manager_service.VSysUserRoleService;
import com.k365.video_base.mapper.VSysUserRoleMapper;
import com.k365.video_base.model.dto.SplitPageDTO;
import com.k365.video_base.model.po.VSysUserRole;
import com.k365.video_base.model.vo.BaseListVO;
import com.k365.video_base.model.vo.SysUserVO;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.ListUtils;

import java.util.*;

/**
 * <p>
 * VIEW 服务实现类
 * </p>
 *
 * @author Gavin
 * @since 2019-07-17
 */
@Service
public class VSysUserRoleServiceImpl
        extends ServiceImpl<VSysUserRoleMapper, VSysUserRole> implements VSysUserRoleService {

    @Override
    public BaseListVO<SysUserVO> findPage(SplitPageDTO pageDTO) {

        IPage<VSysUserRole> page = this.page(new Page<VSysUserRole>().setCurrent(pageDTO.getPage())
                        .setSize(pageDTO.getPageSize()),
                new QueryWrapper<VSysUserRole>().orderByDesc("user_create_date")
                        .orderByAsc("role_sort"));

        long total = page.getTotal();
        List<VSysUserRole> records = page.getRecords();

        List<SysUserVO> sysUserVOSet = new ArrayList<>();
        if (!ListUtils.isEmpty(records)) {

            Map<String, Set<String>> roleMap = new HashMap<>();
            Map<String, Set<Integer>> roleIDsMap = new HashMap<>();

            records.forEach(vSysUserRole -> {
                //添加用户角色
                if (!roleMap.containsKey(vSysUserRole.getUserId())) {
                    roleMap.put(vSysUserRole.getUserId(), new HashSet<>());
                    roleIDsMap.put(vSysUserRole.getUserId(), new HashSet<>());

                    //创建用户VO
                    SysUserVO sysUserVO = SysUserVO.builder()
                            .id(vSysUserRole.getUserId())
                            .icon(vSysUserRole.getUserIcon())
                            .createDate(vSysUserRole.getUserCreateDate())
                            .username(vSysUserRole.getUsername())
                            .nickname(vSysUserRole.getUserNickname())
                            .status(vSysUserRole.getUserStatus())
                            .roleNames(roleMap.get(vSysUserRole.getUserId()))
                            .roleIds(roleIDsMap.get(vSysUserRole.getUserId()))
                            .build();

                    sysUserVOSet.add(sysUserVO);
                }

                if (vSysUserRole.getRoleName()!=null)
                    roleMap.get(vSysUserRole.getUserId()).add(vSysUserRole.getRoleName());
                if (vSysUserRole.getRoleId()!=null)
                    roleIDsMap.get(vSysUserRole.getUserId()).add(vSysUserRole.getRoleId());

            });
        }
        return new BaseListVO<SysUserVO>().setList(sysUserVOSet).setTotal(total);
    }

    @Override
    public List<VSysUserRole> findByUserId(String uid) {
        List<VSysUserRole> list = this.list(new QueryWrapper<VSysUserRole>().eq("user_id", uid));
        return list;
    }
}
