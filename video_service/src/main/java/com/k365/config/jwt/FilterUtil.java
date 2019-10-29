package com.k365.config.jwt;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.k365.video_base.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Gavin
 * @date 2019/7/7 20:26
 * @description：
 */
@Slf4j
public class FilterUtil {

    /**
     * 401非法请求
     *
     * @param req
     * @param res
     */
    public static void response401(ServletRequest req, ServletResponse res, String msg, int code) {
        response( res,HttpStatus.UNAUTHORIZED,msg, code);
    }

    /**
     * 401非法请求
     *
     * @param req
     * @param res
     */
    public static void response502(ServletRequest req, ServletResponse res, String msg, int code) {
        response( res,HttpStatus.BAD_GATEWAY,msg, code);
    }


    private static void response(ServletResponse res,HttpStatus status,String msg, int code){
        HttpServletResponse httpServletResponse = (HttpServletResponse) res;
        httpServletResponse.setStatus(status.value());
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json; charset=utf-8");
        PrintWriter out = null;
        try {
            out = httpServletResponse.getWriter();

            Result result = Result.builder()
                    .code(code)
                    .message(msg)
                    .data(null).build();

            out.append(JSON.toJSONString(result, SerializerFeature.WriteMapNullValue));
        } catch (IOException e) {
            log.error("【{}】返回Response信息出现IOException异常:{}",status.value(),e.getMessage());
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }
}
