package com.k365.new_manager_service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.k365.new_manager_service.ManagerEmployeeService;
import com.k365.new_manager_service.ManagerGroupService;
import com.k365.user_service.UserService;
import com.k365.video_base.common.LevelEnum;
import com.k365.video_base.mapper.ManagerEmployeeMapper;
import com.k365.video_base.model.dto.ManagerEmployeeDTO;
import com.k365.video_base.model.po.ManagerEmployee;
import com.k365.video_base.model.po.User;
import com.k365.video_base.model.vo.BaseListVO;
import com.k365.video_base.model.vo.ManagerEmployeeVO;
import com.k365.video_base.model.vo.UserNewVO;
import com.k365.video_common.util.AESCipher;
import com.k365.video_common.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.ListUtils;
import org.thymeleaf.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class ManagerEmployeeServiceImpl extends ServiceImpl<ManagerEmployeeMapper, ManagerEmployee>
        implements ManagerEmployeeService {

    @Autowired
    private RedisUtil cache;

    @Autowired
    private UserService userService;

    @Autowired
    private ManagerGroupService managerGroupService;


    @Override
    public ManagerEmployee signIn(ManagerEmployeeDTO managerEmployeeDTO) {
        try {
            ManagerEmployee one = this.getOne(new QueryWrapper<ManagerEmployee>().
                    eq("username", managerEmployeeDTO.getUsername()));
            if (one != null) {
                if (managerEmployeeDTO.getPassword().equals(AESCipher.aesDecryptString(one.getPassword()))) {
                    //登录绑定手机号
                    if(!StringUtils.isEmpty(one.getMacAddr())){
                        User u= userService.getOne(new QueryWrapper<User>().
                                eq("mac_addr", one.getMacAddr()));
                        if(u!=null&&!StringUtils.isEmpty(u.getPhone())){
                            one.setPhone(u.getPhone());
                        }
                        this.updateById(one);
                    }
                    return one;
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }


    @Override
    public BaseListVO<ManagerEmployeeVO> getLevel1List(ManagerEmployeeDTO managerEmployeeDTO) {

        //获取用户名
        String username = managerEmployeeDTO.getUsername();

        //通过用户名查询用户
        ManagerEmployee managerEmployee = this.getOne(new QueryWrapper<ManagerEmployee>().
                eq("username", username));

        //获取级别
        Integer level = managerEmployee.getLevel();

        if (level == 0) {
            //查询所有组长list
            IPage<ManagerEmployee> page = this.page(new Page<ManagerEmployee>().setSize(managerEmployeeDTO.getPageSize()).
                            setCurrent(managerEmployeeDTO.getPage()),
                    new QueryWrapper<ManagerEmployee>().eq("level", LevelEnum.LEVEL1.key()).
                            orderByAsc("recommend_count"));

            List<ManagerEmployee> list = page.getRecords();
            List<ManagerEmployeeVO> voList = new ArrayList<>();
            if (!ListUtils.isEmpty(list)) {
                list.forEach(user -> voList.add(ManagerEmployeeVO.builder().id(user.getId()).macAddr(user.getMacAddr())
                        .phone(user.getPhone()).groupId(user.getGroupId()).
                                name(user.getName()).crttime(user.getCrttime()).
                                upttime(user.getUpttime()).recommendCount(user.getRecommendCount()).
                                remark(user.getRemark()).build()));
            }

            return new BaseListVO<ManagerEmployeeVO>().setList(voList).setTotal(page.getTotal());

        }

        return null;
    }



    @Override
    public BaseListVO<ManagerEmployeeVO> getLevel2List(ManagerEmployeeDTO managerEmployeeDTO) {


        //获取组id
        Integer groupId = managerEmployeeDTO.getGroupId();



        //通过groupid查询组员list
        IPage<ManagerEmployee> page = this.page(new Page<ManagerEmployee>().setSize(managerEmployeeDTO.getPageSize())
                        .setCurrent(managerEmployeeDTO.getPage()),
                new QueryWrapper<ManagerEmployee>().eq("group_id", groupId).
                        orderByDesc("recommend_count"));

        List<ManagerEmployee> list = page.getRecords();
        List<ManagerEmployeeVO> voList = new ArrayList<>();
        if (!ListUtils.isEmpty(list)) {
            for(ManagerEmployee po:list){
                ManagerEmployeeVO vo= ManagerEmployeeVO.builder().build();
                vo.setId(po.getId());
                vo.setName(po.getName());
                vo.setMacAddr(po.getMacAddr());
                vo.setPhone(po.getPhone());
                vo.setGroupId(po.getGroupId());
                vo.setCrttime(po.getCrttime());
                vo.setUpttime(po.getUpttime());
                vo.setLevel(po.getLevel());
                vo.setRecommendCount(po.getRecommendCount());
                vo.setGroupName(managerGroupService.getOneById(po.getGroupId()));
                voList.add(vo);
            }
        }
        return new BaseListVO<ManagerEmployeeVO>().setList(voList).setTotal(page.getTotal());
    }

    @Override
    public List<ManagerEmployee> getListByGroupId(Integer id) {

        return this.list(new QueryWrapper<ManagerEmployee>()
                .eq("group_id",id));
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public String add(ManagerEmployeeDTO managerEmployeeDTO) {
        ManagerEmployee managerEmployee = ManagerEmployee.builder().build();
        if(this.getOne(new QueryWrapper<ManagerEmployee>().
                eq("username", managerEmployeeDTO.getUsername()))!=null){
            return "用户名已存在，添加失败！";
        }
        String appid=managerEmployeeDTO.getAppid();
        //通过appName查询用户
        User users = userService.getOne(new QueryWrapper<User>().
                eq("id", managerEmployeeDTO.getAppid()));
        if(users==null){
            return "该app用户不存在！";
        }
        String macAddr = users.getMacAddr();
        if(this.getOne(new QueryWrapper<ManagerEmployee>().
                eq("mac_addr", macAddr))!=null){
            return "该用户mac地址已被使用，请更换设备！";
        }

        BeanUtils.copyProperties(managerEmployeeDTO, managerEmployee);
        if(!StringUtils.isEmpty(users.getPhone())){
            managerEmployee.setPhone(users.getPhone());
        }
        managerEmployee.setMacAddr(macAddr);
        managerEmployee.setCrttime(new Date());
        managerEmployee.setUpttime(new Date());
        if (users.getRecommendCount() != null) {
            managerEmployee.setRecommendCount(users.getRecommendCount());
        }

        try {
            //用户密码加密
            managerEmployee.setPassword(AESCipher.aesEncryptString(managerEmployeeDTO.getPassword()));
        } catch (Exception e) {
            log.info("加密失败，不晓得啥原因，反正就是失败了！");
            return "添加失败";
        }

        if(users.getRecommendCount()!=null){
            managerEmployee.setRecommendCount(users.getRecommendCount());
        }else{
            managerEmployee.setRecommendCount(0);
        }

        boolean save = this.save(managerEmployee);
        if (save) {
            log.info("新增用户成功" );
            return "添加用户成功！";
        } else {
            log.info("新增用户失败！");
            return "添加失败";
        }


    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public void update(ManagerEmployeeDTO managerEmployeeDTO) {

        ManagerEmployee managerEmployee = this.getById(managerEmployeeDTO.getId());
        if (managerEmployee == null) {
            throw new RuntimeException("员工不存在或已被删除");
        }
        ManagerEmployee zuzhang = this.getOne(new QueryWrapper<ManagerEmployee>().
                eq("level", LevelEnum.LEVEL1.key()).eq("group_id", managerEmployeeDTO.getGroupId()));
        if(zuzhang!=null){
            //如果组长已经存在，则修改组长为普通组员
            zuzhang.setLevel(LevelEnum.LEVEL2.key());
            this.updateById(zuzhang);
        }
        managerEmployee.setLevel(managerEmployeeDTO.getLevel());
        managerEmployee.setGroupId(managerEmployeeDTO.getGroupId());
        managerEmployee.setUpttime(new Date());

        boolean updateSuccess = this.updateById(managerEmployee);
        if (updateSuccess) {
            log.info("修改用户成功，用户id：" + managerEmployee.getId());
        } else {
            log.info("修改用户失败！");
        }
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT)
    public void removeByEmployeeId(Integer id) {
        ManagerEmployee managerEmployee = this.getById(id);
        if (managerEmployee == null) {
            throw new RuntimeException("员工不存在或已被删除");
        }

        boolean del = this.removeById(id);
        if (del) {
            log.info("删除用户成功，用户id：" + managerEmployee.getId());
        } else {
            log.info("删除用户失败！");
        }

    }


    @Override
    public ManagerEmployeeVO getPersonalInfo(Integer id) {
        ManagerEmployee employee = this.getById(id);

        if (employee == null) {
            throw new RuntimeException("员工不存在或已被删除");
        }

        ManagerEmployeeVO vo = ManagerEmployeeVO.builder().build();
        BeanUtils.copyProperties(employee, vo);
        return vo;
    }


    @Override
    public BaseListVO<UserNewVO> getPersonalCommonend(ManagerEmployeeDTO managerEmployeeDTO) {
        User users = userService.getOne(new QueryWrapper<User>().
                eq("mac_addr", managerEmployeeDTO.getMacAddr()));

        //通过推广码查询所有用户
        if (users != null) {
            IPage<User> page = userService.page(new Page<User>().setCurrent(managerEmployeeDTO.getPage())
                            .setSize(managerEmployeeDTO.getPageSize()),
                    new QueryWrapper<User>().eq("recommender_id", users.getRecommendCode()).
                            orderByDesc("register_time"));

            List<User> list = page.getRecords();
            List<UserNewVO> voList = new ArrayList<>();

            if (!ListUtils.isEmpty(list)) {
                list.forEach(user -> voList.add(UserNewVO.builder().name(user.getNickname()).macAddr(user.getMacAddr())
                        .phone(user.getPhone()).registerTime(user.getRegisterTime()).recommendCount(user.getRecommendCount()).build()));
            }
            return new BaseListVO<UserNewVO>().setList(voList).setTotal(page.getTotal());
        }

        return null;
    }

    @Override
    public String getOneByGroupId(Integer id) {
        ManagerEmployee employee = this.getOne(new QueryWrapper<ManagerEmployee>()
                .eq("group_id", id).eq("level",1));
        if(employee==null){
            return "暂无组长";
        }else{
            return employee.getName();
        }
    }

    @Override
    public BaseListVO<ManagerEmployeeVO> getAllUser(ManagerEmployeeDTO managerEmployeeDTO) {


        IPage<ManagerEmployee> page = this.page(new Page<ManagerEmployee>().setCurrent(managerEmployeeDTO.getPage())
                        .setSize(managerEmployeeDTO.getPageSize()),
                new QueryWrapper<ManagerEmployee>().notIn("level",LevelEnum.LEVELS.key(),LevelEnum.LEVEL0.key()));
        List<ManagerEmployee> list = page.getRecords();
        List<ManagerEmployeeVO> voList = new ArrayList<>();
        if (!ListUtils.isEmpty(list)) {
            for(ManagerEmployee po:list){
                ManagerEmployeeVO vo= ManagerEmployeeVO.builder().build();
                vo.setId(po.getId());
                vo.setName(po.getName());
                vo.setMacAddr(po.getMacAddr());
                vo.setUsername(po.getUsername());
                vo.setPhone(po.getPhone());
                vo.setGroupId(po.getGroupId());
                vo.setCrttime(po.getCrttime());
                vo.setUpttime(po.getUpttime());
                vo.setLevel(po.getLevel());
                vo.setRecommendCount(po.getRecommendCount());
                vo.setGroupName( managerGroupService.getOneById(po.getGroupId()));
                voList.add(vo);
            }
        }
        return new BaseListVO<ManagerEmployeeVO>().setList(voList).setTotal(page.getTotal());



    }

    @Override
    public String addAdmin(ManagerEmployeeDTO managerEmployeeDTO) {
        ManagerEmployee managerEmployee = ManagerEmployee.builder().build();
        if(this.getOne(new QueryWrapper<ManagerEmployee>().
                eq("username", managerEmployeeDTO.getUsername()))!=null){
            return "用户名已存在，添加失败！";
        }

        managerEmployee.setLevel(LevelEnum.LEVEL0.key());
        managerEmployee.setCrttime(new Date());
        managerEmployee.setUpttime(new Date());
        managerEmployee.setUsername(managerEmployeeDTO.getUsername());
        managerEmployee.setName(managerEmployeeDTO.getName());

        try {
            //用户密码加密
            managerEmployee.setPassword(AESCipher.aesEncryptString(managerEmployeeDTO.getPassword()));
        } catch (Exception e) {
            log.info("加密失败，不晓得啥原因，反正就是失败了！");
            return "添加管理员失败";
        }
        boolean save = this.save(managerEmployee);
        if (save) {
            log.info("新增管理员成功" );
            return "添加管理员成功！";
        } else {
            log.info("新增管理员失败！");
            return "添加管理员失败";
        }
    }

    @Override
    public ManagerEmployee getByMacAddr(String macAddr) {

        return  this.getOne(new QueryWrapper<ManagerEmployee>().
                eq("mac_addr", macAddr));
    }

    @Override
    public void updateByObj(ManagerEmployee managerEmployee) {
        this.updateById(managerEmployee);
    }

}
