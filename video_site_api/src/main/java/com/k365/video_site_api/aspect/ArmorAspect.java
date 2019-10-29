package com.k365.video_site_api.aspect;

import com.k365.manager_service.AppVersionService;
import com.k365.video_base.common.VideoContants;
import com.k365.video_base.model.po.AppVersion;
import com.k365.video_common.annotation.Armor;
import com.k365.video_common.constant.AppTypeEnum;
import com.k365.video_common.constant.OSEnum;
import com.k365.video_common.util.HttpUtil;
import com.k365.video_common.util.RedisUtil;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @author Gavin
 * @date 2019/7/31 13:01
 * @description：IOS防弹衣
 */
@Component
@Aspect
public class ArmorAspect {

    @Autowired
    private AppVersionService appVersionService;

    @Autowired
    private RedisUtil cache;


    @Pointcut(value = "execution(* com.k365.video_site_api.controller..*.*(..)) && " +
            "@annotation(com.k365.video_common.annotation.Armor)")
    public void armorPoint() {
    }


    @Around(value = "armorPoint()")
    public Object armorDataAround(ProceedingJoinPoint joinPoint) throws Throwable {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
        HttpServletRequest request = servletRequestAttributes.getRequest();
        HttpServletResponse response = servletRequestAttributes.getResponse();

        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        if (method.isAnnotationPresent(Armor.class)) {
            //获取方法上注解中表明的权限
            Armor armor = method.getAnnotation(Armor.class);
            OSEnum[] target = armor.target();

            OSEnum os = HttpUtil.getOS(request);
            String appVersionName = HttpUtil.getAppVersion(request);
            AppTypeEnum appType = HttpUtil.getAppType(request);
            //默认小爱
            appType = appType == null ? AppTypeEnum.XIAO_AI : appType;

            if (os == null || appVersionName == null) {
                return joinPoint.proceed();
            }

            boolean contains = Arrays.asList(target).contains(os);

            if (!contains)
                return joinPoint.proceed();

            String osSwitchCacheKey = StringUtils.join(VideoContants.CACHE_OS_ARMOR_SWITCH, os.getName(), ":",
                    appType.getCode(), ":", appVersionName);

            Object val = null;
            //查询是否开启伪装数据开关
            if (cache.hasKey(osSwitchCacheKey)) {
                val = cache.get(osSwitchCacheKey);
            } else {
                AppVersion appVersion = appVersionService.findByVersionAndOs(os, appVersionName, appType);

                if (appVersion != null) {
                    val = appVersion.getArmorData();
                    cache.set(osSwitchCacheKey, val);
                }
            }

            if (BooleanUtils.isTrue((Boolean) val)) {
                String armorPath = "/armor-data";
                String uri = request.getRequestURI().replace(request.getContextPath(), armorPath);
                //重定向到伪装数据接口
                //response.sendRedirect(request.getRequestURL().toString().replace(request.getRequestURI(),"m-wap" + request.getContextPath() +  uri));

                request.setAttribute("params", joinPoint.getArgs());
                request.getRequestDispatcher(uri).forward(request, response);
                return null;
            }

        }
        return joinPoint.proceed();
    }

}
