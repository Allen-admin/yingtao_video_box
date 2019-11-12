package com.k365.channel_service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.k365.channel_service.VUserChannelService;
import com.k365.user_service.UserLevelService;
import com.k365.user_service.UserService;
import com.k365.video_base.mapper.VUserChannelMapper;
import com.k365.video_base.model.po.User;
import com.k365.video_base.model.po.UserLevel;
import com.k365.video_base.model.po.VUserChannel;
import com.k365.video_base.model.so.UserChannelSO;
import com.k365.video_base.model.so.VUserChannelSO;
import com.k365.video_base.model.vo.BaseListVO;
import com.k365.video_base.model.vo.UserChannelVO;
import com.k365.video_base.model.vo.UserLevelVO;
import com.k365.video_base.model.vo.VUserChannelVO;
import com.k365.video_common.util.AddressUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.ListUtils;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


/**
 * <p>
 * 用户渠道 服务实现类
 * </p>
 *
 * @author Allen
 * @since 2019-11--05
 */
@Service
public class VUserChannelServicelmpl extends ServiceImpl<VUserChannelMapper, VUserChannel> implements VUserChannelService {

    @Autowired
    private UserLevelService userLevelService;
    String phoneType;
    AddressUtils addressUtils = new AddressUtils();
    @Autowired
    private UserService userService;

    @Override
    public HashMap<String, Object> findList(VUserChannelSO vUserChannelSO) {

        HashMap<String, Object> map = new HashMap<>();

        vUserChannelSO.setPage((vUserChannelSO.getPage() - 1) * vUserChannelSO.getPageSize());
        map.put("list", this.baseMapper.find(vUserChannelSO));
        map.put("total", this.baseMapper.count(vUserChannelSO));

        return map;

    }

    @Override
    public List<VUserChannelVO> search(VUserChannelSO vUserChannelSO) {
        vUserChannelSO.setPage((vUserChannelSO.getPage() - 1) * vUserChannelSO.getPageSize());

        if (vUserChannelSO.getCName() != null && vUserChannelSO.getCName().equals("")) {
            vUserChannelSO.setCName(vUserChannelSO.getCName());
        }

        return this.baseMapper.search(vUserChannelSO);


    }


    @Override
    public HashMap<String, Object> searchPage(VUserChannelSO vUserChannelSO) {
        vUserChannelSO.setPage((vUserChannelSO.getPage() - 1) * vUserChannelSO.getPageSize());
        HashMap<String, Object> map = new HashMap<>();

        if (vUserChannelSO.getEndTime() != null && !("").equals(vUserChannelSO.getEndTime()) && vUserChannelSO.getBeginTime() != null
                && !("").equals(vUserChannelSO.getBeginTime())) {

            long dataDetail1 = Long.valueOf(vUserChannelSO.getEndTime());
            long dataDetail = Long.valueOf(vUserChannelSO.getBeginTime());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date(dataDetail);
            Date date1 = new Date(dataDetail1);
            vUserChannelSO.setBeginTime(simpleDateFormat.format(date));
            vUserChannelSO.setEndTime(simpleDateFormat.format(date1));

            /**
             * list根据条件查询的结果集
             * total根据条件查询的总数
             */
            map.put("list", this.baseMapper.searchList(vUserChannelSO));
            map.put("total", this.baseMapper.count(vUserChannelSO));

            return map;
        }

        if (vUserChannelSO.getCName() != null && !("").equals(vUserChannelSO.getCName())) {
            vUserChannelSO.setCName(vUserChannelSO.getCName());

            map.put("list", this.baseMapper.searchList(vUserChannelSO));
            map.put("total", this.baseMapper.count(vUserChannelSO));
            return map;
        }

        map.put("list", this.baseMapper.searchList(vUserChannelSO));
        return map;

    }


    @Override
    public List<UserLevelVO> findAll() {
        List<UserLevel> list = userLevelService.list(new QueryWrapper<UserLevel>()
                .orderByDesc("level").select("id,level"));

        List<UserLevelVO> voList = new ArrayList<>();
        list.forEach(userLevel ->
                voList.add(UserLevelVO.builder().id(userLevel.getId())
                        .level(userLevel.getLevel())
                        .build())
        );

        return voList;
    }

    @Override
    public List<VUserChannelVO> searchList(VUserChannelSO vUserChannelSO) {
        vUserChannelSO.setPage((vUserChannelSO.getPage() - 1) * vUserChannelSO.getPageSize());

        if (vUserChannelSO.getEndTime() != null && !vUserChannelSO.getEndTime().equals("")) {
            long dataDetail1 = Long.valueOf(vUserChannelSO.getEndTime());
            long dataDetail = Long.valueOf(vUserChannelSO.getBeginTime());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date(dataDetail);
            Date date1 = new Date(dataDetail1);
            vUserChannelSO.setBeginTime(simpleDateFormat.format(date));
            vUserChannelSO.setEndTime(simpleDateFormat.format(date1));
        }

        vUserChannelSO.setVChannelCode(vUserChannelSO.getVChannelCode());

        return this.baseMapper.list(vUserChannelSO);

    }

    @Override
    public BaseListVO<UserChannelVO> findUser(VUserChannelSO vUserChannelSO) {


        IPage<User> page = userService.page(new Page<User>().setCurrent(vUserChannelSO.getPage()).
                setSize(vUserChannelSO.getPageSize()), new QueryWrapper<User>());

        long total = page.getTotal();
        List<User> records = page.getRecords();
        BaseListVO<UserChannelVO> lists = new BaseListVO<UserChannelVO>();

        List<UserChannelVO> list = new ArrayList<>();
        if (ListUtils.isEmpty(records))
            return null;

        if (!ListUtils.isEmpty(records)) {
            for (int i = 0; i < records.size(); i++) {

                if (records.get(i).getMacAddr().contains("#iOS")) {
                    phoneType = "0";
                } else if (records.get(i).getMacAddr().contains("#android")) {
                    phoneType = "1";
                } else {
                    phoneType = "2";
                }

                String address = "";

                try {
                    address = addressUtils.getAddresses("ip=" + records.get(i).getLastLoginIp(), "utf-8");
                } catch (UnsupportedEncodingException e) {

                    e.printStackTrace();
                }

                list.add(UserChannelVO.builder().nickname(records.get(i).getNickname()).phone(records.get(i).getPhone())
                        .lastLoginTime(records.get(i).getLastLoginTime()).lastTime(records.get(i).getLastTime())
                        .lastLoginIp(records.get(i).getLastLoginIp()).userLevel(records.get(i).getUserLevel())
                        .registerTime(records.get(i).getRegisterTime()).address(address)
                        .terminal(phoneType)
                        .vipEndTime(records.get(i).getVipEndTime()).build());

            }

        }

        lists.setList(list);
        lists.setTotal(total);

        return lists;

    }

    @Override
    public BaseListVO<UserChannelVO> searchUser(UserChannelSO userChannelSO) {

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        BaseListVO<UserChannelVO> baseListVO = new BaseListVO<>();
        List<UserChannelVO> userChannelVOList = new ArrayList<>();

        if (userChannelSO.getRegisterBeginTime() != null && userChannelSO.getRegisterEndTime() != null && !"".equals(userChannelSO.getRegisterBeginTime()) && !"".equals(userChannelSO.getRegisterEndTime())) {
            //创建时间
            queryWrapper.between("register_time", new Date(Long.valueOf(userChannelSO.getRegisterBeginTime())), new Date(Long.valueOf(userChannelSO.getRegisterEndTime())));
        }

        if (userChannelSO.getLastloginBeginTime() != null && userChannelSO.getLastloginEndTime() != null && !"".equals(userChannelSO.getLastloginBeginTime()) && !"".equals(userChannelSO.getLastloginEndTime())) {
            //最后登录时间
            queryWrapper.between("last_login_time", new Date(Long.valueOf(userChannelSO.getLastloginBeginTime())), new Date(Long.valueOf(userChannelSO.getLastloginEndTime())));
        }

        if (userChannelSO.getChannelCode() != null && !"".equals(userChannelSO.getChannelCode())) {
            //推广渠道
            queryWrapper.eq("register_channel", userChannelSO.getChannelCode());
        }

        if (userChannelSO.getUserLevel() != null && !"".equals(userChannelSO.getUserLevel())) {
            //用户等级
            queryWrapper.eq("user_level", userChannelSO.getUserLevel());
        }

        if (userChannelSO.getLastLoginIp() != null && !"".equals(userChannelSO.getLastLoginIp())) {
            //最后登录ip
            queryWrapper.eq("last_login_ip", userChannelSO.getLastLoginIp());
        }

        if (userChannelSO.getLoginIp() != null && !"".equals(userChannelSO.getLoginIp())) {
            //登录ip
            queryWrapper.eq("login_ip", userChannelSO.getLoginIp());
        }

        if (userChannelSO.getLastTime() != null && !"".equals(userChannelSO.getLastTime())) {
            //至少登录时长
            queryWrapper.gt("last_login_ip", userChannelSO.getLastTime());
        }

        if (userChannelSO.getNotlogintDay() != null && !"".equals(userChannelSO.getNotlogintDay())) {
            //未登录超过天数
            queryWrapper.lt("last_login_time", new Date(Long.valueOf(userChannelSO.getNotlogintDay())));
        }

        if (userChannelSO.getVipType() != null && !"".equals(userChannelSO.getVipType())) {

            if (userChannelSO.getVipType() == 1) {

                //VIP类型（0不是VIP 1是vip未到期 2VIP已到期）
                queryWrapper.eq("vip_type", 1);

            }
        }

        if (userChannelSO.getRegistered() != null && !"".equals(userChannelSO.getRegistered())) {

            if (userChannelSO.getRegistered() == 1) {

                //注册会员(1是注册，其他为没注册)
                queryWrapper.gt("phone", 3);

            }

        }

        if (userChannelSO.getTerminal() != null && !"".equals(userChannelSO.getTerminal())) {
            if (userChannelSO.getTerminal() == 1) {
                //终端类型类型（1是ios 2是android 0是全部）
                queryWrapper.like("mac_addr", "#iOS");
            } else if (userChannelSO.getTerminal() == 2) {
                queryWrapper.like("mac_addr", "#android");
            }
        }

        IPage<User> page = userService.page(new Page<User>().setSize(userChannelSO.getPageSize())
                .setCurrent(userChannelSO.getPage()), queryWrapper.orderByDesc("register_time"));

        for (int i = 0; i < page.getRecords().size(); i++) {

            if (page.getRecords().get(i).getMacAddr().contains("#iOS")) {
                phoneType = "0";
            } else if (page.getRecords().get(i).getMacAddr().contains("#android")) {
                phoneType = "1";
            } else {
                phoneType = "2";
            }

            String address = "";

            try {
                address = addressUtils.getAddresses("ip=" + page.getRecords().get(i).getLastLoginIp(), "utf-8");
            } catch (UnsupportedEncodingException e) {

                e.printStackTrace();
            }

            UserChannelVO userChannelVO = new UserChannelVO();
            userChannelVO.setAddress(address);
            userChannelVO.setLastLoginIp(page.getRecords().get(i).getLastLoginIp());
            userChannelVO.setLastLoginTime(page.getRecords().get(i).getLastLoginTime());
            userChannelVO.setLastTime(page.getRecords().get(i).getLastTime());
            userChannelVO.setNickname(page.getRecords().get(i).getNickname());
            userChannelVO.setPhone(page.getRecords().get(i).getPhone());
            userChannelVO.setRegisterTime(page.getRecords().get(i).getRegisterTime());
            userChannelVO.setTerminal(phoneType);
            userChannelVO.setUserLevel(page.getRecords().get(i).getUserLevel());
            userChannelVO.setVipEndTime(page.getRecords().get(i).getVipEndTime());
            userChannelVOList.add(userChannelVO);

        }

        baseListVO.setList(userChannelVOList);

        baseListVO.setTotal(page.getTotal());
        return baseListVO;

    }


}
