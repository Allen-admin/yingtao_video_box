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
public class Po2VoTransfer<PO,VO> {

    /**
     * PO转换VO列表
     * @return
     */
    public List<VO> cast(List<PO> source, Class<VO> target) throws IllegalAccessException, InstantiationException {

        if(ListUtils.isEmpty(source) || target == null)
            return null;

        List<VO> resultList = new ArrayList<>();
        for (PO PO : source) {
            VO instance = target.newInstance();
            // source target
            BeanUtils.copyProperties(PO,instance);
            resultList.add(instance);
        }
        return resultList;
    }

    /**
     * PO转换VO
     * @return
     */
    public VO cast(PO source, VO target){

        if(source == null || target == null)
            return null;

        BeanUtils.copyProperties(source,target);
        return target;
    }

}
