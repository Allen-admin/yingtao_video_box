package com.k365.video_base.mybatis.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PerformanceInterceptor;
import com.k365.video_base.common.CustomAttr;
import com.k365.video_base.mybatis.CommonMetaObjectHandler;
import com.k365.video_base.mybatis.MybatisPlusSqlInjector;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Gavin
 * @date 2019/6/16 19:31
 * @description：定义多个数据源
 */
@Configuration
@MapperScan("com.k365.video_base.mapper.*_db.**")
@EnableTransactionManagement
public class DataSourceConfig {

    @Autowired
    private CustomAttr attr;

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.druid")
    public DataSource druid() {
        return new DruidDataSource();
    }


    /**
     * 配置一个管理后台的Servlet
     *
     * @return
     */
    @Bean
    public ServletRegistrationBean statViewServlet() {
        ServletRegistrationBean bean = new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
        Map<String, String> initParams = new HashMap<>();
        initParams.put("loginUsername", attr.getDruidViewName());//登录druid监控的账户
        initParams.put("loginPassword", attr.getDruidViewPwd());//登录druid监控的密码
        initParams.put("allow", attr.getDruidViewIpAllow());//默认就是允许所有访问
        initParams.put("deny", attr.getDruidViewIpDeny());//黑名单的IP

        bean.setInitParameters(initParams);
        return bean;
    }

    /**
     * mybatis-plus SQL执行效率插件
     */
    @Bean
    public PerformanceInterceptor performanceInterceptor() {
        return new PerformanceInterceptor();
    }

    /**
     * 分页
     *
     * @return
     */
   /* @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }*/

    /**
     * 乐观锁
     *
     * @return
     */
//    @Bean
//    public OptimisticLockerInterceptor optimisticLockerInterceptor() {
//        return new OptimisticLockerInterceptor();
//    }

    /**
     * 自动填充
     *
     * @return
     */
//    @Bean
//    public CommonMetaObjectHandler commonMetaObjectHandler() {
//        return new CommonMetaObjectHandler();
//    }

    /**
     * 自定义注入语句
     *
     * @return
     */
//    @Bean
//    public MybatisPlusSqlInjector mybatisPlusSqlInjector() {
//        return new MybatisPlusSqlInjector();
//    }

}
