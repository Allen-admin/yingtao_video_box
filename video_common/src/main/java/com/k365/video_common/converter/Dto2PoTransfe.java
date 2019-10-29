package com.k365.video_common.converter;

import org.springframework.beans.BeanUtils;
import org.thymeleaf.util.ListUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Gavin
 * @date 2019/6/22 15:57
 * @description：
 */
public class Dto2PoTransfe<DTO,PO> {


    /**
     * DTO转换PO列表
     * @return
     */
    public List<PO> cast(List<DTO> source,Class<PO> target) throws IllegalAccessException, InstantiationException {

        if(ListUtils.isEmpty(source) || target == null)
            return null;

        List<PO> resultList = new ArrayList<>();
        for (DTO dto : source) {

            PO instance = target.newInstance();
            // source target
            BeanUtils.copyProperties(dto,instance);
            resultList.add(instance);
        }
        return resultList;
    }

    /**
     * DTO转换PO
     * @return
     */
    public PO cast(DTO source, PO target){

        if(source == null || target == null)
            return null;

        BeanUtils.copyProperties(source,target);
        return target;
    }
}
