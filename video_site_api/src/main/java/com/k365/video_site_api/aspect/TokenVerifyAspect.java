package com.k365.video_site_api.aspect;

import com.k365.config.jwt.FilterUtil;
import com.k365.video_base.common.UserContext;
import com.k365.user_service.UserService;
import com.k365.video_base.model.po.User;
import com.k365.video_common.annotation.TokenVerify;
import com.k365.video_common.constant.AppTypeEnum;
import com.k365.video_common.constant.ExCodeMsgEnum;
import com.k365.video_common.util.HttpUtil;
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

/**
 * @author Gavin
 * @date 2019/8/8 20:39
 * @description：
 */
@Component
@Aspect
public class TokenVerifyAspect {

    @Autowired
    private UserService userService;

    @Pointcut(value = "execution(* com.k365.video_site_api.controller..*.*(..)) && " +
            "@annotation(com.k365.video_common.annotation.TokenVerify)")
    public void tokenPoint() {
    }

    @Around(value = "tokenPoint()")
    public Object tokenVerifyAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();

        boolean hasTokenVerifyAnno = false;
        if (method.isAnnotationPresent(TokenVerify.class)) {
            //获取方法上的注解
            hasTokenVerifyAnno = method.getAnnotation(TokenVerify.class) != null
                    && !method.getAnnotation(TokenVerify.class).skip();
        }
        if (!hasTokenVerifyAnno) {
            //获取类上的注解
            TokenVerify tokenVerify = joinPoint.getTarget().getClass().getAnnotation(TokenVerify.class);
            hasTokenVerifyAnno = tokenVerify != null && !tokenVerify.skip();
        }

        if (hasTokenVerifyAnno) {
            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
            ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
            HttpServletRequest request = servletRequestAttributes.getRequest();
            HttpServletResponse response = servletRequestAttributes.getResponse();

            User currentUser = userService.getCurrentUser(request, response);
            if(currentUser == null){
                FilterUtil.response401(request,response,ExCodeMsgEnum.IDENTITY_HAS_EXPIRED.msg(), ExCodeMsgEnum.IDENTITY_HAS_EXPIRED.code());
                return null;
            }
            new UserContext(currentUser);
        }
        return joinPoint.proceed();
    }

}
