package com.k365.manager_service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.k365.manager_service.IpRestrictService;
import com.k365.video_base.common.ProjectEnum;
import com.k365.video_base.mapper.IpRestrictMapper;
import com.k365.video_base.model.dto.IpRestrictDTO;
import com.k365.video_base.model.po.IpRestrict;
import com.k365.video_base.model.vo.BaseListVO;
import com.k365.video_common.constant.Constants;
import com.k365.video_common.util.HttpUtil;
import com.k365.video_common.util.IPUtil;
import com.k365.video_common.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.ListUtils;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * ip黑白名单限制表 服务实现类
 * </p>
 *
 * @author Gavin
 * @since 2019-09-11
 */
@Service
@Slf4j
public class IpRestrictServiceImpl extends ServiceImpl<IpRestrictMapper, IpRestrict> implements IpRestrictService {

    @Autowired
    private RedisUtil cache;

    @Override
    public List<IpRestrict> findAll() {
        //ip先取缓存
        if (cache.lGetListSize(Constants.CACHE_IP_RESTRICT) > 0) {
            return JSONArray.parseArray(JSON.toJSONString(cache.lGet(Constants.CACHE_IP_RESTRICT, 0, -1)),IpRestrict.class);
        }

        List ipList = this.list(new QueryWrapper<IpRestrict>().eq("status", true));
        if(!ListUtils.isEmpty(ipList)){
            cache.lSetList(Constants.CACHE_IP_RESTRICT, ipList);
        }
        return ipList;
    }

    @Override
    public BaseListVO<IpRestrict> getPage(IpRestrictDTO ipRestrictDTO) {
        QueryWrapper<IpRestrict> queryWrapper = new QueryWrapper<>();
        if (ipRestrictDTO.getStatus() != null) {
            //状态查询
            queryWrapper.eq("status", ipRestrictDTO.getStatus());
        }
        if (StringUtils.isNotBlank(ipRestrictDTO.getIp())) {
            //IP查询
            queryWrapper.like("ip", ipRestrictDTO.getIp());
        }
        if (ipRestrictDTO.getProject() != null) {
            //系统
            queryWrapper.eq("project", ipRestrictDTO.getProject());
        }
        if (ipRestrictDTO.getRestrictType() != null) {
            //黑白名单查询
            queryWrapper.eq("restrict_type", ipRestrictDTO.getRestrictType());
        }
        if (StringUtils.isNotBlank(ipRestrictDTO.getCountry())) {
            //地区
            queryWrapper.like("country", ipRestrictDTO.getCountry());
        }
        if (StringUtils.isNotBlank(ipRestrictDTO.getRegion())) {
            //省份
            queryWrapper.like("region", ipRestrictDTO.getRegion());
        }
        if (StringUtils.isNotBlank(ipRestrictDTO.getCity())) {
            //城市
            queryWrapper.like("city", ipRestrictDTO.getCity());
        }

        IPage<IpRestrict> page = this.page(new Page<IpRestrict>().setCurrent(ipRestrictDTO.getPage())
                .setSize(ipRestrictDTO.getPageSize()), queryWrapper.orderByDesc("id"));

        return new BaseListVO<IpRestrict>().setTotal(page.getTotal()).setList(page.getRecords());
    }

    @Override
    public void add(IpRestrict ipRestrict) {
        boolean saved = this.save(ipRestrict);
        if (saved) {
            cache.lSet(Constants.CACHE_IP_RESTRICT, ipRestrict);
        }
    }

    @Override
    public void update(IpRestrict ipRestrict) {
        boolean updated = this.updateById(ipRestrict);
        if (updated) {
            long count = cache.lRemove(Constants.CACHE_IP_RESTRICT, 1, ipRestrict);
            if (count > 0) {
                cache.lSet(Constants.CACHE_IP_RESTRICT, ipRestrict);
            }
        }
    }

    @Override
    public void remove(IpRestrict ipRestrict) {
        boolean removed = this.removeById(ipRestrict.getId());
        if (removed) {
            cache.lRemove(Constants.CACHE_IP_RESTRICT, 1, ipRestrict);
        }
    }

    @Override
    public Boolean ipCheck(ServletRequest servletRequest) {
        System.out.println("----进来了 IP限制");
        HttpServletRequest request = HttpUtil.getHttpReq(servletRequest);
        //客户端ip 字符串
        String ipStr = IPUtil.getClientIp(request);
        //客户端ip 数值
        Long ipNumber = IPUtil.getIpNumber(ipStr);

        //项目
        ProjectEnum projectEnum = ProjectEnum.getByPath(request.getContextPath());
        //ip所在地

        String[] location = null;
        if (cache.get(ipStr) != null) {
          String  locations = cache.get(ipStr).toString();
            location = locations.split(",");
        } else{
            try {
                String locationStr = IPUtil.getLocationOfIP(ipStr);
                if (StringUtils.isNotBlank(locationStr)) {
                    String[] split = locationStr.split("-");
                    if (split.length == 3) {
                        location = split;
                        String locations=location[0]+","+location[1]+","+location[2];
                        cache.set(ipStr,locations,7200);
                    }
                }
            } catch (Exception e) {
                log.error("IP拦截，获取IP所在地失败，ip:{},Exception:{}", ipStr, e.getMessage());
            }
       }


       List<IpRestrict> list = this.findAll();
        if (!ListUtils.isEmpty(list)) {
            boolean inBlacklist = false;
            boolean inWhitelist = false;
            for (IpRestrict ipRestrict : list) {
                if (projectEnum != null && (Objects.equals(ProjectEnum.ALL_PROJECT.code(), ipRestrict.getProject()) || Objects.equals(projectEnum.code(), ipRestrict.getProject()))) {
                    if (Objects.equals(ipRestrict.getRestrictType(), IpRestrict.WHITE_LIST)) {
                        //白名单验证 范围由小到大
                        if (StringUtils.isNotBlank(ipRestrict.getIp())) {
                            //判断IP
                            inWhitelist = Objects.equals(ipRestrict.getIp(), ipStr);
                        }
                        if (!inWhitelist && StringUtils.isNotBlank(ipRestrict.getIpFrom()) && StringUtils.isNotBlank(ipRestrict.getIpEnd())) {
                            //判断网段
                            inWhitelist = ipNumber >= IPUtil.getIpNumber(ipRestrict.getIpFrom()) && ipNumber <= IPUtil.getIpNumber(ipRestrict.getIpEnd());
                        }
                        if (!inWhitelist && StringUtils.isNotBlank(ipRestrict.getCountry()) && location != null) {
                            //国家
                            inWhitelist = Objects.equals(ipRestrict.getCountry(), location[0]);
                            //省份
                            inWhitelist = inWhitelist && (StringUtils.isBlank(ipRestrict.getRegion()) || Objects.equals(ipRestrict.getRegion(), location[1]));
                            //城市
                            inWhitelist = inWhitelist && (StringUtils.isBlank(ipRestrict.getCity()) || Objects.equals(ipRestrict.getCity(), location[2]));
                        }

                    } else if (Objects.equals(ipRestrict.getRestrictType(), IpRestrict.BLACKLIST)) {
                        //黑名单验证 范围由大到小
                        if (StringUtils.isNotBlank(ipRestrict.getCountry()) && location != null) {
                            //国家
                            inBlacklist = Objects.equals(ipRestrict.getCountry(), location[0]);
                            //省份
                            inBlacklist = inBlacklist && (StringUtils.isBlank(ipRestrict.getRegion()) || Objects.equals(ipRestrict.getRegion(), location[1]));
                            //城市
                            inBlacklist = inBlacklist && (StringUtils.isBlank(ipRestrict.getCity()) || Objects.equals(ipRestrict.getCity(), location[2]));
                        }
                        if (!inBlacklist && StringUtils.isNotBlank(ipRestrict.getIpFrom()) && StringUtils.isNotBlank(ipRestrict.getIpEnd())) {
                            inBlacklist = ipNumber >= IPUtil.getIpNumber(ipRestrict.getIpFrom()) && ipNumber <= IPUtil.getIpNumber(ipRestrict.getIpEnd());
                        }
                        if (!inBlacklist && StringUtils.isNotBlank(ipRestrict.getIp())) {
                            inBlacklist = Objects.equals(ipRestrict.getIp(), ipStr);
                        }

                    }
                }

            }

            return inWhitelist || !inBlacklist;

        }
        return true;
    }
}
