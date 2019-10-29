package com.k365.config.resolver;

import com.k365.video_common.annotation.JwtClaim;
import com.k365.video_common.constant.ExCodeMsgEnum;
import com.k365.video_common.exception.ResponsiveException;
import com.k365.video_common.util.JwtUtil;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;

/**
 * @author Gavin
 * @date 2019/6/21 11:31
 * @description：Controller 参数解析器
 */
public class JwtTokenArgumentResolver implements HandlerMethodArgumentResolver {
    /**
     * 判断参数是否满足条件
     * @param parameter
     * @return
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(JwtClaim.class);
    }

    /**
     * 仅当supportsParameter(MethodParameter parameter) 方法返回true是执行
     * @return
     * @throws Exception
     */
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        String authorization = request.getHeader("Authorization");
        String result = null;
        JwtClaim token = null;
        if(authorization!=null){
            Annotation[] methodAnnotations = parameter.getParameterAnnotations();
            for (Annotation methodAnnotation : methodAnnotations) {
                if(methodAnnotation instanceof JwtClaim){
                    token = (JwtClaim) methodAnnotation;
                    break;
                }
            }
            if(token!=null){
                result = JwtUtil.getAccount(authorization);
            }
        }
        if(result!=null){
            return result;
        }
        if(token==null || token.exception()){
            throw new ResponsiveException(ExCodeMsgEnum.NOT_SING_IN);
        }else{
            return null;
        }
    }
}
