package com.k365.channel_service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.k365.channel_service.VUserChannelService;
import com.k365.user_service.UserLevelService;
import com.k365.video_base.mapper.VUserChannelMapper;
import com.k365.video_base.model.po.UserLevel;
import com.k365.video_base.model.po.VUserChannel;
import com.k365.video_base.model.so.VUserChannelSO;
import com.k365.video_base.model.vo.VUserChannelVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

       if(vUserChannelSO.getEndTime() != null &&vUserChannelSO.getBeginTime() != null&& !vUserChannelSO.getEndTime().equals("")&& !vUserChannelSO.getBeginTime().equals("")){
           long dataDetail1 = Long.valueOf(vUserChannelSO.getEndTime());
           long dataDetail = Long.valueOf(vUserChannelSO.getBeginTime());
           SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
           Date date = new Date(dataDetail);
           Date date1 = new Date(dataDetail1);
           vUserChannelSO.setBeginTime(simpleDateFormat.format(date));
           vUserChannelSO.setEndTime(simpleDateFormat.format(date1));

           return this.baseMapper.findPage(vUserChannelSO);
       }

       if(vUserChannelSO.getSearchValue() != null && vUserChannelSO.getSearchValue() != null){
           vUserChannelSO.setSearchValue(StringUtils.join(vUserChannelSO.getSearchValue(),
                    StringUtils.isNotBlank(vUserChannelSO.getSearchValue()) ? "*" : ""));

            return this.baseMapper.search(vUserChannelSO);
        }

        return this.baseMapper.find(vUserChannelSO);

    }


    @Override
    public List<VUserChannelVO> searchPage(VUserChannelSO vUserChannelSO) {

      /*  QueryWrapper<User> queryWrapper = new QueryWrapper<User>().orderByDesc("register_time");
        if(StringUtils.isNotBlank(vUserChannelSO.getBeginTime()) && StringUtils.isNotBlank(vUserChannelSO.getEndTime())){
            queryWrapper.between("create_date",new Date(Long.valueOf(vUserChannelSO.getBeginTime())),new Date(Long.valueOf(vUserChannelSO.getEndTime())));
        }*/

        /*vUserChannelSO.setPage((vUserChannelSO.getPage() - 1) * vUserChannelSO.getPageSize());*/

        long dataDetail1 = Long.valueOf(vUserChannelSO.getEndTime());
        long dataDetail = Long.valueOf(vUserChannelSO.getBeginTime());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(dataDetail);
        Date date1 = new Date(dataDetail1);
        vUserChannelSO.setBeginTime(simpleDateFormat.format(date));
        vUserChannelSO.setEndTime(simpleDateFormat.format(date1));

        return this.baseMapper.findPage(vUserChannelSO);

//        IPage<User> page = userService.page(new Page<User>().setCurrent(vUserChannelSO.getPage()).
//                setSize(vUserChannelSO.getPageSize()),queryWrapper);

        /*return new BaseListVO<User>().setTotal(page.getTotal()).setList(page.getRecords());

        return null;*/
    }


    @Override
    public List<UserLevel> findAll() {
        List<UserLevel> list =userLevelService.list(new QueryWrapper<UserLevel>().orderByDesc("level").select("id,level"));

        List<UserLevel> voList = new ArrayList<>();
        list.forEach(userLevel ->
                voList.add(UserLevel.builder().id(userLevel.getId())
                        .level(userLevel.getLevel())
                        .build())
        );

      return voList;
     }

    @Override
    public List<VUserChannelVO> searchList(VUserChannelSO vUserChannelSO) {
        vUserChannelSO.setPage((vUserChannelSO.getPage() - 1) * vUserChannelSO.getPageSize());

        if(vUserChannelSO.getEndTime() != null &&vUserChannelSO.getBeginTime() != null&& !vUserChannelSO.getEndTime().equals("")&& !vUserChannelSO.getBeginTime().equals("")){
            long dataDetail1 = Long.valueOf(vUserChannelSO.getEndTime());
            long dataDetail = Long.valueOf(vUserChannelSO.getBeginTime());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date(dataDetail);
            Date date1 = new Date(dataDetail1);
            vUserChannelSO.setBeginTime(simpleDateFormat.format(date));
            vUserChannelSO.setEndTime(simpleDateFormat.format(date1));

            return this.baseMapper.list(vUserChannelSO);
        }

            vUserChannelSO.setSearchValue(StringUtils.join(vUserChannelSO.getSearchValue(),
                    StringUtils.isNotBlank(vUserChannelSO.getSearchValue()) ? "*" : ""));

        return this.baseMapper.list(vUserChannelSO);

    }

}
