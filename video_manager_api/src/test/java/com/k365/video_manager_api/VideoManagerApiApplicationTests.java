package com.k365.video_manager_api;

import com.k365.global.RedisService;
import com.k365.manager_service.SysConfParamService;
import com.k365.manager_service.SysUserService;
import com.k365.video_common.constant.SysParamNameEnum;
import com.k365.video_common.util.Encrypt;
import com.k365.video_common.util.MD5Util;
import com.k365.video_common.util.RandomKeyUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
@EnableCaching
public class VideoManagerApiApplicationTests {

    @Autowired(required = false)
    private RedisService redisService;

    @Autowired(required = false)
    private SysConfParamService sysConfParamService;

    @Autowired
    private SysUserService sysUserService;

    @Test
    public void contextLoads() {
    }


    @Test
    public void testRedisAnnotation() {
        Map<String, Object> paramValByName = (Map<String, Object>) findParamByName();
        System.out.println(paramValByName);
        paramValByName.entrySet().forEach(entry ->
            System.out.println(entry.getKey() + " : " + entry.getValue()));
    }

    @Cacheable(value = "aa", key = "'1'")
    public Object findParamByName() {
        Map<String, Object> paramValByName =
                sysConfParamService.findParamValByName(SysParamNameEnum.SYS_GOOGLE_AUTHENTICATOR);
        System.out.println("执行了方法findParamByName");
        return paramValByName;
    }

    @Test
    public void testFindAll() {

        redisService.setString("哈喽", "沃德");
    }


    @Test
    public void testFindUserPRByName() {
        String name = "Gavin-test11";
//        SysUser sysUser = sysUserService.findUserRPByName(name);

    }

    //测试加解密
    @Test
    public void testEnDeCode() {
        String code = "1234aBc";
        String encode = Encrypt.base64Encode(code);
        String decode = Encrypt.base64Decode(encode);
        System.out.println(code);
        System.out.println(encode);
        System.out.println(decode);

        String key = RandomKeyUtil.createSecretKey();
        System.out.println(key);

        String md5key = MD5Util.md5(key);
        System.out.println(md5key);

        char[] chars = (key + md5key).toCharArray();
        String result = "";
        for (int i = 0; i < 64; i++) {
            int random = (int) (Math.random() * chars.length - 1);
            result += chars[random];

        }
        System.out.println(result);

    }

    public String doST() {
        return "It's True";
    }

    @Test
    public void testIOSArmor() {
        String s = doST();
        System.err.println(s);
    }


}

