package com.k365.video_manager_api.aspect;

import com.k365.manager_service.SysLogService;
import com.k365.video_base.model.po.SysLog;
import com.k365.video_common.annotation.SysLogs;
import com.k365.video_common.util.IPUtil;
import com.k365.video_common.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
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
import java.util.Date;

/**
 * @author Gavin
 * @date 2019/9/11 16:44
 * @description：
 */
@Component
@Aspect
@Slf4j
public class SysLogAspect {

    @Autowired
    private SysLogService sysLogService;


    @Pointcut(value = "execution(* com.k365.video_manager_api.controller..*.*(..)) && " +
            "@annotation(com.k365.video_common.annotation.SysLogs)")
    public void syslogPoint() {
    }


    @Around(value = "syslogPoint()")
    public Object syslogAround(ProceedingJoinPoint joinPoint) throws Exception {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();

        Object result = null;
        Throwable throwable = null;
        if (method.isAnnotationPresent(SysLogs.class)) {

            SysLogs syslog = method.getAnnotation(SysLogs.class);
            String val = syslog.value();

            boolean isSuccess = true;
            try {
                result = joinPoint.proceed();
            } catch (Throwable throwable2) {
                log.error("执行【{}】操作失败，Message：{},Cause:{}", val, throwable2.getMessage(), throwable2.toString());
                isSuccess = false;
                throwable = throwable2;
            }

            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
            ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
            HttpServletRequest request = servletRequestAttributes.getRequest();
            HttpServletResponse response = servletRequestAttributes.getResponse();
            //访问路径
            String accessPath = request.getRequestURL().toString();

            //用户IP
            String userIp = IPUtil.getClientIp(request);

            //用户名
            String authorization = request.getHeader("Authorization");
            String username = authorization == null || JwtUtil.getAccount(authorization) == null
                    ? (response.getHeader("Authorization") != null ? JwtUtil.getAccount(response.getHeader("Authorization"))
                    : "[未知用户 ? No Authorization]") : JwtUtil.getAccount(authorization);

            sysLogService.add(new SysLog().setOperate(val).setStatus(isSuccess).setTime(new Date()).setUserIp(userIp)
                    .setUsername(username).setAccessPath(accessPath));

        }

        if (throwable != null) {
            if (throwable instanceof Exception) {
                throw (Exception) throwable;
            }
        }
        return result;
    }
}
