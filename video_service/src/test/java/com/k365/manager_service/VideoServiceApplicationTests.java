package com.k365.manager_service;

import com.k365.global.RedisService;
import com.k365.video_common.constant.SysParamNameEnum;
import com.k365.video_common.constant.SysParamValueNameEnum;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

/**
 * @author Gavin
 * @date 2019/6/27 15:30
 * @description：
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class VideoServiceApplicationTests {

    @Autowired(required = false)
    private RedisService redisService;

    @Autowired(required = false)
    private SysConfParamService sysConfParamService;

    @Test
    public void contextLoads() {
    }


    @Test
    public void testRedisAnnotation(){
        Map<String, Object> paramValByName =  (Map<String, Object>)findParamByName();
        paramValByName.entrySet().forEach(entry->{
            System.out.println(entry.getKey() +" : "+entry.getValue());

        });
    }

    @Cacheable("cache1")
    public Object findParamByName(){
        Map<String, Object> paramValByName =
                sysConfParamService.findParamValByName(SysParamNameEnum.SYS_GOOGLE_AUTHENTICATOR);
        System.out.println("执行了方法findParamByName");
        return paramValByName;
    }

    @Test
    public void testFindAll(){

        redisService.setString("哈喽","沃德");
    }

}
