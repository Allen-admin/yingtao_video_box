package com.k365.video_base.mybatis;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.AbstractSqlInjector;

import java.util.List;

/**
 * @author Gavin
 * @date 2019/8/11 19:37
 * @description：MybatisPlusSql注入器
 */
public class MybatisPlusSqlInjector extends AbstractSqlInjector {
    @Override
    public List<AbstractMethod> getMethodList(Class<?> mapperClass) {
        return null;
    }
}
