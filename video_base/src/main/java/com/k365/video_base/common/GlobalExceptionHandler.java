package com.k365.video_base.common;

import com.k365.video_common.constant.HttpStatusEnum;
import com.k365.video_common.exception.GeneralException;
import com.k365.video_common.exception.ResponsiveException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

/**
 * @author Gavin
 * @date 2019/6/19 16:45
 * @description：Controller层异常处理器
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler /*extends ResponseEntityExceptionHandler */ {

    /**
     * 覆盖handleExceptionInternal这个汇总处理方法，将响应数据替换为我们的
     *
     * @param ex
     * @param body
     * @param headers
     * @param status
     * @param request
     * @return
     */
    /*@Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body,
                                                             HttpHeaders headers, HttpStatus status, WebRequest request) {
        return new ResponseEntity<>(
                ackTransfer(null, ex.getMessage(), status.value(), ex, true), status);

    }*/

    /**
     * 601 缺失参数
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public String handleMissingServletRequestParameterException(MissingServletRequestParameterException e,
                                                                HttpServletRequest request) {
        StringBuilder message = new StringBuilder();
        message.append("缺失请求参数:[[");
        message.append(e.getParameterName());
        message.append("]]");
        return ackTransfer(request, message.toString(), HttpStatusEnum.MISSING_PARAM.value(), e);
    }

    /**
     * 602 参数类型错误异常
     */
    @ExceptionHandler(value = {MethodArgumentTypeMismatchException.class,
            HttpMessageConversionException.class})
    public String handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e,
                                                            HttpServletRequest request) {
        StringBuilder message = new StringBuilder();
        message.append("请求参数:[[");
        message.append(e.getName());
        message.append("]]类型错误");
        return ackTransfer(request, message.toString(), HttpStatusEnum.WRONG_PARAM_TYPE.value(), e);
    }

    /**
     * 603 参数校验错误异常
     */
    @ExceptionHandler(value = {MethodArgumentNotValidException.class,
            BindException.class})
    public String methodArgumentValidExceptionHandler(MethodArgumentNotValidException e,
                                                      HttpServletRequest request) {
        String message = "";
        if (e.getBindingResult().getErrorCount() > 0) {
            message = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        }
        return ackTransfer(request, message, HttpStatusEnum.ILLEGAL_PARAM.value(), e);
    }

    /**
     * 604 处理请求失败
     */
    @ExceptionHandler(GeneralException.class)
    public String handleGeneralException(GeneralException e,
                                         HttpServletRequest request) {
        return ackTransfer(request, e.getMessage(), HttpStatusEnum.HANDLE_REQUEST_FAIL.value(),
                e, true);
    }

    /**
     * 604 生成token签名失败
     */
    @ExceptionHandler(UnsupportedEncodingException.class)
    public String unsupportedEncodingExceptionException(UnsupportedEncodingException e,
                                                        HttpServletRequest request) {
        return ackTransfer(request, e.getMessage(), HttpStatusEnum.HANDLE_REQUEST_FAIL.value(),
                e, true);
    }

    /**
     * 605 前端显示提示信息
     */
    @ExceptionHandler(ResponsiveException.class)
    public String handleResponsiveException(ResponsiveException e,
                                            HttpServletRequest request) {
        return ackTransfer(request, e.getMessage(), HttpStatusEnum.SHOW_TIPS.value(), e);
    }

    /**
     * 500 Default Exception Handler
     * 兜底异常
     */
    @ExceptionHandler(Exception.class)
    public String handleException(Exception e, HttpServletRequest request) {
        log.error("服务器内部错误!"+request.getHeaderNames());
        return ackTransfer(request, "服务器内部错误!" ,
                HttpStatusEnum.INTERNAL_SERVER_ERROR.value(), e, true);
    }

    /**
     * transfer ACK
     */
    private String ackTransfer(HttpServletRequest request, String message, int code, Exception e) {
        return ackTransfer(request, message, code, e, false);
    }

    private String ackTransfer(HttpServletRequest request, String message, int code, Exception e,
                               boolean printStackTrace) {

        if (printStackTrace) {
            log.error(message + e.getMessage(), e);
        } else {
            log.error(message);
        }

        return ResultFactory.buidResult(code, message);
    }

}
