package com.k365.manager_service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.k365.manager_service.SysConfParamService;
import com.k365.video_base.mapper.SysConfParamMapper;
import com.k365.video_base.model.dto.SysConfParamDTO;
import com.k365.video_base.model.po.SysConfParam;
import com.k365.video_common.constant.ExCodeMsgEnum;
import com.k365.video_common.constant.SysParamNameEnum;
import com.k365.video_common.constant.SysParamValueNameEnum;
import com.k365.video_common.exception.ResponsiveException;
import com.k365.video_common.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Gavin
 * @date 2019/7/2 10:09
 * @description：
 */
@Service
@Transactional
public class SysConfParamServiceImpl extends
        ServiceImpl<SysConfParamMapper, SysConfParam> implements SysConfParamService {

    @Autowired
    private RedisUtil cache;

    @Override
    public Map<String, Object> findParamValByName(SysParamNameEnum sysParamName) {
        Map<String, Object> resultMap = new HashMap<>();
        if (sysParamName == null) {
            return resultMap;
        }

        SysConfParam confParam = this.getOne(new QueryWrapper<SysConfParam>().eq(
                "param_name", sysParamName.code()).select("param_value"));

        if (confParam != null && StringUtils.isNotBlank(confParam.getParamValue())) {
            resultMap = JSON.parseObject(confParam.getParamValue(), resultMap.getClass());
        }
        return resultMap;
    }


    @Override
    public Object getValByValName(SysParamValueNameEnum paramValueName) {
        Map<String, Object> paramVal = this.findParamValByName(paramValueName.getSysParamNameEnum());
        if (paramVal == null)
            return null;

        return paramVal.get(paramValueName.code());

    }

    @Override
    public void add(SysConfParamDTO sysConfParamDTO) {
        SysConfParam sysConfParam = this.getOne(new QueryWrapper<SysConfParam>().eq("param_code", sysConfParamDTO.getParamCode())
                .eq("param_name", sysConfParamDTO.getParamName()));

        if (sysConfParam != null) {
            throw new ResponsiveException(ExCodeMsgEnum.SYS_PARAM_EXISTS);
        }

        sysConfParam = SysConfParam.builder().build();
        BeanUtils.copyProperties(sysConfParamDTO, sysConfParam);
        sysConfParam.setParamValue(JSON.toJSONString(sysConfParamDTO.getParamValue(), SerializerFeature.WriteMapNullValue));
        this.save(sysConfParam);
    }

    @Override
    public void updateByIdOrName(SysConfParamDTO sysConfParamDTO) {
        SysConfParam confParam = this.getOne(new QueryWrapper<SysConfParam>().eq("id", sysConfParamDTO.getId()).or()
                .eq("param_name", sysConfParamDTO.getParamName()));

        if (confParam == null)
            throw new ResponsiveException("系统参数不存在或已被删除");

        Map<String, Object> paramValue = sysConfParamDTO.getParamValue();
        //前端更新接口，转JSON对象自动添加下列Key，保存时移除。
        paramValue.remove("isRootInsert");
        paramValue.remove("elm");

        confParam.setParamValue(JSON.toJSONString(paramValue, SerializerFeature.WriteMapNullValue));
        confParam.setParamStatus(sysConfParamDTO.getParamStatus());

        this.update(confParam, new UpdateWrapper<SysConfParam>().eq("id", sysConfParamDTO.getId()).or()
                .eq("param_name", sysConfParamDTO.getParamName()));
    }

    @Override
    public List<SysConfParam> findAll() {
        List<SysConfParam> list = this.list(new QueryWrapper<SysConfParam>().orderByDesc("id"));
        for (SysConfParam p : list) {
            p.setParamValueName(SysParamValueNameEnum.getSysParamValueNameEnumByName(SysParamNameEnum.getSysParamNameEnum(p.getParamName())));
        }

        return list;
    }

    @Override
    public void remove(Integer id) {
        this.removeById(id);
    }


/*
    @Override
    public void updateById(SysConfParamDTO sysConfParamDTO) {
        SysConfParam confParam = this.getById(sysConfParamDTO.getId());
        if (confParam == null)
            throw new ResponsiveException("系统参数不存在或已被删除");

        Map<String, Object> paramValue = sysConfParamDTO.getParamValue();
        //前端更新接口，转JSON对象自动添加下列Key，保存时移除。
        paramValue.remove("isRootInsert");
        paramValue.remove("elm");

        */
/*if (Objects.equals(SysParamNameEnum.MOBILE_DATA_ARMOR.code(), sysConfParamDTO.getParamName()) &&
                Objects.equals(StatusEnum.ENABLE.key(), (int) sysConfParamDTO.getParamStatus())) {

            for (Map.Entry<String, Object> entry : paramValue.entrySet()) {
                String osArmorSwitch = VideoContants.CACHE_OS_ARMOR_SWITCH + entry.getKey();
                if (cache.hasKey(osArmorSwitch)) //覆盖redis中的os armor 开关
                    cache.set(osArmorSwitch, entry.getValue());
            }
        }*//*

        confParam.setParamValue(JSON.toJSONString(paramValue, SerializerFeature.WriteMapNullValue));
        confParam.setParamStatus(sysConfParamDTO.getParamStatus());

        this.updateById(confParam);
    }
*/

    /*@Override
    public void updateByName(SysConfParamDTO sysConfParamDTO) {
        SysConfParam confParam = this.getOne(new QueryWrapper<SysConfParam>().eq("param_name", sysConfParamDTO.getParamName()));
        if (confParam == null)
            throw new ResponsiveException("系统参数不存在或已被删除");

        Map<String, Object> paramValue = sysConfParamDTO.getParamValue();
        //前端更新接口，转JSON对象自动添加下列Key，保存时移除。
        paramValue.remove("isRootInsert");
        paramValue.remove("elm");

        confParam.setParamValue(JSON.toJSONString(paramValue, SerializerFeature.WriteMapNullValue));
        confParam.setParamStatus(sysConfParamDTO.getParamStatus());

        this.update(confParam, new UpdateWrapper<SysConfParam>().eq("param_name", sysConfParamDTO.getParamName()));
    }*/


}
