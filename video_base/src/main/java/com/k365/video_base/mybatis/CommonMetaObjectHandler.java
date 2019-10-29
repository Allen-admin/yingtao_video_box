package com.k365.video_base.mybatis;


import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;

import java.util.Date;

/**
 * @author Gavin
 * @date 2019/6/30 12:24
 * @description：
 */
public class CommonMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("create_date", new Date(), metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {

    }

}
